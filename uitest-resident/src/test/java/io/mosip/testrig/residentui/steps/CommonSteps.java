package io.mosip.testrig.residentui.steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.apirig.utils.NotificationListener;
import io.mosip.testrig.residentui.hooks.ResidentTestData;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;
import io.mosip.testrig.uitestrig.core.UiConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared Cucumber step definitions: login, logout, popup dismissal.
 */
public class CommonSteps {

    private static final Logger logger = LoggerFactory.getLogger(CommonSteps.class);

    // The environment rate-limits OTP sends per identity, so logging in fresh for every
    // scenario can exhaust it mid-run. Real login happens once; every other "logged in with
    // their VID" reuses the saved session via Playwright storageState instead.
    private static final String STORAGE_STATE_PATH = "target/perpetual-vid-storage-state.json";
    private static volatile boolean storageStateReady = false;

    protected Page page() {
        return PlaywrightFactory.getPage();
    }

    // ── Login steps ───────────────────────────────────────────────────────────

    // Only this login path saves/reuses state — performOtpLogin() is also used by the
    // temporary/one-time VID logins below, which must always be real logins.
    @Given("the user is logged in with their VID")
    public void loginWithPerpetualVid() {
        if (storageStateReady && tryReuseSession()) {
            return;
        }
        performOtpLogin(ResidentTestData.perpetualVid);
        PlaywrightFactory.saveStorageState(STORAGE_STATE_PATH);
        storageStateReady = true;
    }

    /**
     * Loads the saved session into a fresh browser context/page and checks it landed on the
     * authenticated dashboard rather than the public landing page — an OIDC token can expire
     * mid-suite, in which case the app shows the login screen despite the cookies.
     */
    private boolean tryReuseSession() {
        PlaywrightFactory.initPageWithStorageState(STORAGE_STATE_PATH);
        page().navigate(UiConfigManager.getBaseUrl());
        page().waitForLoadState(LoadState.NETWORKIDLE);

        if (!isOnAuthenticatedDashboard()) {
            logger.warn("Saved session no longer valid (likely expired) — falling back to a fresh OTP login");
            return false;
        }
        // The post-login welcome popup may or may not appear for a reused session.
        try {
            Locator dismiss = page().locator("#dismissBtn");
            dismiss.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5_000));
            dismiss.click();
        } catch (Exception e) {
            logger.debug("No post-login popup this time (reused session) — continuing");
        }
        logger.info("Reused saved session — skipped OTP login");
        return true;
    }

    // #UINservices is the login trigger on the PUBLIC landing page only (see performOtpLogin) —
    // if it's still there after navigating with a saved session, that session wasn't valid.
    private boolean isOnAuthenticatedDashboard() {
        try {
            page().locator("#UINservices").waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(5_000));
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    @Given("the user is logged in with a perpetual VID")
    public void loginWithPerpetualVidExplicit() {
        loginWithPerpetualVid();
    }

    @Given("the user is logged in with a temporary VID")
    public void loginWithTemporaryVid() {
        performOtpLogin(ResidentTestData.temporaryVid);
    }

    @Given("the user is logged in with a one-time VID")
    public void loginWithOneTimeVid() {
        performOtpLogin(ResidentTestData.oneTimeVid);
    }

    // Always a real login — for scenarios that specifically verify this login flow works,
    // which must not silently turn into a cookie replay via the cache.
    @Given("the user performs a fresh login with their VID")
    public void freshLoginWithVid() {
        performOtpLogin(ResidentTestData.perpetualVid);
        PlaywrightFactory.saveStorageState(STORAGE_STATE_PATH);
        storageStateReady = true;
    }

    @Given("the user performs a fresh login with their UIN")
    public void freshLoginWithUin() {
        performOtpLogin(ResidentTestData.uin);
    }

    @Then("the login should be confirmed")
    public void assertLoginConfirmed() {
        if (!isOnAuthenticatedDashboard()) {
            throw new AssertionError("Expected to land on the authenticated dashboard after login");
        }
    }

    // ── Popup steps ───────────────────────────────────────────────────────────

    @Then("a success popup should appear")
    public void assertSuccessPopup() {
        new VisualValidator(page()).assertSuccessPopup();
    }

    @When("the user dismisses the popup")
    public void dismissPopup() {
        page().locator("#dismissBtn").click();
    }

    // ── Internal OTP login ────────────────────────────────────────────────────

    void performOtpLogin(String vid) {
        logger.info("Performing OTP login with VID ...{}", vid.length() > 4 ? vid.substring(vid.length() - 4) : "****");
        String email = ResidentTestData.loginEmail;

        page().locator("#UINservices").click();
        page().locator("#login_with_otp").click();
        page().locator("#Otp_mosip-vid").fill(vid);
        NotificationListener.markRequestStart();
        requestOtpWithRetry();

        String otp = NotificationListener.getOtp(email);
        NotificationListener.markRequestRemove();
        if (otp == null || otp.isEmpty()) throw new IllegalStateException("OTP not received for " + email);

        Locator inputs = page().locator("#otp_verify_input div input");
        for (int i = 0; i < otp.length() && i < inputs.count(); i++) {
            inputs.nth(i).fill(String.valueOf(otp.charAt(i)));
        }

        page().locator("#verify_otp").click();
        page().waitForLoadState(LoadState.NETWORKIDLE);

        // First-time OAuth consent (shown only once per VID+app pair). The checkboxes use a
        // Tailwind peer-toggle pattern: a sibling <div> overlays the real <input> and intercepts
        // clicks, so evaluate("el => el.click()") is used instead of a regular click(). Waits
        // for the checkbox rather than an instant isVisible() check, since the consent page can
        // still be mid-render right after NETWORKIDLE.
        Locator consentCheckbox = page().locator("#authorize_scope");
        try {
            consentCheckbox.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(10_000));
            consentCheckbox.evaluate("el => el.click()");
            page().locator("#voluntary_claims").evaluate("el => el.click()");
            page().locator("#continue").click();
            page().waitForLoadState(LoadState.NETWORKIDLE);
        } catch (Exception e) {
            logger.debug("No first-time consent screen this run — continuing");
        }

        // The post-login popup (#dismissBtn) can take a while to open, especially for a
        // freshly-generated identity's first login — wait generously, since missing it leaves
        // it open to block whatever loads next.
        Locator dismiss = page().locator("#dismissBtn");
        try {
            dismiss.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(60_000));
            dismiss.click();
        } catch (Exception e) {
            logger.warn("Login-success popup (#dismissBtn) did not appear within 60s — continuing");
        }

        logger.info("Login successful");
    }

    /**
     * "Get OTP" can be rejected with a "Request could not be processed" banner (OTP-send rate
     * limiting). Checks for that specific error and retries with backoff instead of hanging on
     * an OTP panel that will never appear.
     */
    private void requestOtpWithRetry() {
        Locator otpPanel = page().locator("#otp_verify_input");
        Locator errorBanner = page().locator("text=Request could not be processed");
        int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            page().locator("#get_otp").click();
            for (int i = 0; i < 10; i++) {
                if (otpPanel.isVisible()) return;
                if (errorBanner.isVisible()) break;
                page().waitForTimeout(1_000);
            }
            if (otpPanel.isVisible()) return;
            logger.warn("Get OTP attempt {}/{} was rejected — retrying", attempt, maxAttempts);
            page().waitForTimeout(5_000);
        }
        throw new IllegalStateException("Get OTP kept being rejected ('Request could not be processed') after "
                + maxAttempts + " attempts");
    }
}
