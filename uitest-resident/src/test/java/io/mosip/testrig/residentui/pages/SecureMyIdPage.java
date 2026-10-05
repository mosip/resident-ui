package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

public class SecureMyIdPage extends BasePage {

    private final VisualValidator visual;

    public SecureMyIdPage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        click("#uinservices\\/lockunlockauth", "Open the Secure My ID service");
        waitForNetworkIdle();
        // Angular renders the auth toggles from the backend response — wait for them explicitly
        waitForVisible("#setAuthlockStatus1");
    }

    /** Toggles an auth type by index (1–5) and submits. Caller must navigate to the page first.
     *  The toggle uses a Tailwind peer-toggle pattern — a sibling div overlays the input and
     *  intercepts pointer events, so evaluate("el => el.click()") is used to bypass the overlay. */
    public void toggleAuthType(int typeIndex) {
        page.locator("#setAuthlockStatus" + typeIndex).evaluate("el => el.click()");
        click("#updateAuthlockStatusBtn", "Click the 'Update' button");
        click("#lockunlockauthBtn", "Confirm the lock/unlock action");
        waitForNetworkIdle();
    }

    public void assertSuccessPopup() {
        visual.assertSuccessPopup();
    }

    public void dismissPopup() {
        click("#dismissBtn", "Dismiss the popup");
    }
}
