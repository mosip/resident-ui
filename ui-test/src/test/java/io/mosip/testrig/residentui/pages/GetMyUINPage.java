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
        // Confirms the OTP panel actually rendered before the caller starts polling SMTP for
        // an OTP that may never have been sent — any failure here (backend error popup, etc.)
        // surfaces immediately with its own screenshot, instead of waiting out the full SMTP
        // poll timeout first.
        waitForVisible("#otp-input", "Wait for the OTP input to appear after Send OTP");
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
