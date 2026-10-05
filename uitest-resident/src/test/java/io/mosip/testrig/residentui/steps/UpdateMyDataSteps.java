package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.apirig.utils.NotificationListener;
import io.mosip.testrig.residentui.config.ResidentConfigManager;
import io.mosip.testrig.residentui.hooks.ResidentTestData;
import io.mosip.testrig.residentui.pages.UpdateMyDataPage;
import io.mosip.testrig.residentui.pages.UpdateMyDataPage.PendingRequestException;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class UpdateMyDataSteps {

    private static final long RETRY_INTERVAL_MILLIS = 10_000;

    private UpdateMyDataPage page() {
        return new UpdateMyDataPage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to Update My Data")
    public void navigateToUpdateMyData() {
        page().navigateTo();
    }

    @When("the user updates their name details")
    public void updateName() {
        retryWhileEarlierRequestPending(() -> {
            page().fillNameFields();
            page().preview();
            page().submit();
            page().confirmUpdate();
        });
    }

    @When("the user updates their address details")
    public void updateAddress() {
        retryWhileEarlierRequestPending(() -> {
            page().selectAddressTab();
            page().fillAddressFields();
            page().preview();
            page().submit();
            page().confirmUpdate();
        });
    }

    // Commits to the new email as the identity's address going forward (no revert) — loginEmail
    // is the single source of truth every OTP-login step reads (CommonSteps, GetMyUINSteps,
    // VerifyEmailPhoneSteps), and previousLoginEmail lets a post-change OTP wait check both
    // addresses until the backend has fully switched over.
    @When("the user updates their email address")
    public void updateEmail() {
        String newEmail = ResidentTestData.generateUniqueEmail();
        retryWhileEarlierRequestPending(() -> changeEmailTo(newEmail));
        ResidentTestData.previousLoginEmail = ResidentTestData.loginEmail;
        ResidentTestData.loginEmail = newEmail;
    }

    private void changeEmailTo(String email) {
        page().selectContactTab();
        page().fillNewEmail(email);
        NotificationListener.markRequestStart();
        page().sendEmailOtp();
        String otp = NotificationListener.getOtp(email);
        NotificationListener.markRequestRemove();
        if (otp == null || otp.isEmpty()) throw new IllegalStateException("OTP not received for email update");

        page().fillOtp(otp);
        page().submitOtp();
    }

    @When("the user updates their preferred language")
    public void updateLanguage() {
        retryWhileEarlierRequestPending(() -> {
            page().selectLanguageTab();
            page().selectPreferredLanguage();
        });
    }

    @Then("the data update should be confirmed")
    public void assertUpdateConfirmed() {
        page().assertUpdateSuccessPopup();
        page().dismissPopup();
    }

    /**
     * Runs the update; when the "Existing request found" popup blocks it, the popup is
     * cancelled, Update My Data is reopened, and the update retried every 10s for up to
     * packetUpdateWait minutes. Catches any RuntimeException, not just PendingRequestException —
     * the popup's content is backend-fetched and can take longer to attach than a field click's
     * own timeout, so a plain click failure can still be this same popup, just not yet
     * detectable. Re-checks for it on any failure before retrying.
     */
    private void retryWhileEarlierRequestPending(Runnable update) {
        int maxMinutes = ResidentConfigManager.getPacketUpdateWaitMinutes();
        long deadline = System.currentTimeMillis() + maxMinutes * 60_000L;
        boolean firstAttempt = true;
        while (true) {
            try {
                if (!firstAttempt) page().navigateTo();
                page().cancelPendingRequestPopupIfShown(true);
                update.run();
                return;
            } catch (RuntimeException e) {
                RuntimeException cause = e;
                if (!(e instanceof PendingRequestException)) {
                    try {
                        page().cancelPendingRequestPopupIfShown(true);
                    } catch (PendingRequestException pre) {
                        cause = pre;
                    }
                }
                if (System.currentTimeMillis() >= deadline) {
                    throw new IllegalStateException("Update My Data kept failing — possibly an earlier request "
                            + "still pending — after " + maxMinutes + " minute(s). Increase packetUpdateWait if "
                            + "this is expected.", cause);
                }
                firstAttempt = false;
                PlaywrightFactory.getPage().waitForTimeout(RETRY_INTERVAL_MILLIS);
            }
        }
    }
}
