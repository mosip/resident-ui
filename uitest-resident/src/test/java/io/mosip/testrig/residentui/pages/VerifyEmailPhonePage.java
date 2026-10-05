package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

public class VerifyEmailPhonePage extends BasePage {

    private final VisualValidator visual;

    public VerifyEmailPhonePage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        click("#verifyPhoneNumber\\/EmailID", "Open the Verify Phone Number / Email ID service");
        waitForNetworkIdle();
        page.waitForTimeout(1_500);
    }

    public void selectEmailChannel()    { click("#emailChannelBtn-button", "Select the 'Email' channel"); }
    public void selectPhoneChannel()    { click("#phoneChannelBtn", "Select the 'Phone' channel"); }

    public void enterVid(String vid)    { fill("#uin", vid, "Enter the VID"); }

    public void sendOtp()               { click("#sendOtpBtn", "Click the 'Send OTP' button"); }

    public void enterOtp(String otp) {
        // Submit's enabled state is driven by a (keyup) handler — fill() bypasses it.
        textBox("#otp-input").type(otp);
    }

    public void submitOtp() {
        click("#submitOtpBtn", "Click the 'Submit OTP' button");
        waitForNetworkIdle();
    }

    public boolean isSubmitOtpButtonVisible()  { return isVisible("#submitOtpBtn"); }

    public void goBack()               { click("#backBtn", "Click the 'Back' button"); }

    public void assertSuccessPopup()   { visual.assertSuccessPopup(); }

    public void dismissPopup()         { click("#dismissBtn", "Dismiss the popup"); }
}
