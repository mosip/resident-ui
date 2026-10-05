package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

public class GetMyUINPage extends BasePage {

    private final VisualValidator visual;

    public GetMyUINPage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        click("#getMyUIN", "Open the Get My UIN service");
        waitForNetworkIdle();
    }

    public void enterVid(String vid) {
        fill("#aidValue", vid, "Enter the VID");
    }

    public void requestOtp() {
        click("#getUinSendOtpBtn", "Click the 'Send OTP' button");
    }

    public void enterOtp(String otp) {
        fill("#otp-input", otp, "Enter the OTP");
    }

    public void submitOtp() {
        click("#getUinsubmitBtn", "Click the 'Submit' button");
        waitForNetworkIdle();
    }

    public boolean isSubmitOtpButtonVisible() {
        return isVisible("#getUinsubmitBtn");
    }

    public boolean isSendOtpButtonEnabled() {
        return page.locator("#getUinSendOtpBtn").isEnabled();
    }

    public void assertDownloadSuccessPopup() {
        visual.assertSuccessPopup();
    }

    public void dismissPopup() {
        click("#dismissBtn", "Dismiss the popup");
    }
}
