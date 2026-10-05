package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.apirig.utils.NotificationListener;
import io.mosip.testrig.residentui.hooks.ResidentTestData;
import io.mosip.testrig.residentui.pages.VerifyEmailPhonePage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

public class VerifyEmailPhoneSteps {

    private VerifyEmailPhonePage page() {
        return new VerifyEmailPhonePage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to Verify Phone Number and Email ID")
    public void navigate() {
        page().navigateTo();
    }

    // Polls both the current and previous registered email, in case the backend hasn't fully
    // switched over yet after an Update My Data email change.
    @When("the user verifies their email with a valid OTP")
    public void verifyEmailWithValidOtp() {
        page().selectEmailChannel();
        page().enterVid(ResidentTestData.perpetualVid);
        NotificationListener.markRequestStart();
        page().sendOtp();
        String otp = ResidentTestData.getOtpFromCurrentOrPreviousEmail();
        NotificationListener.markRequestRemove();
        if (otp == null || otp.isEmpty()) throw new IllegalStateException("OTP not received for email verification");
        page().enterOtp(otp);
        page().submitOtp();
    }

    @When("the user tries to verify email with an invalid VID")
    public void verifyEmailWithInvalidVid() {
        page().selectEmailChannel();
        page().enterVid("6534906793542111");
        page().sendOtp();
    }

    @When("the user tries to verify phone with an invalid VID")
    public void verifyPhoneWithInvalidVid() {
        page().selectPhoneChannel();
        page().enterVid("6534906793542111");
        page().sendOtp();
    }

    @Then("the OTP submit button should not appear for email")
    public void assertSubmitNotVisibleEmail() {
        new VisualValidator(PlaywrightFactory.getPage()).assertNotVisible("#submitOtpBtn");
        page().dismissPopup();
    }

    @Then("the OTP submit button should not appear for phone")
    public void assertSubmitNotVisiblePhone() {
        new VisualValidator(PlaywrightFactory.getPage()).assertNotVisible("#submitOtpBtn");
        page().dismissPopup();
    }

    @Then("the email verification should be confirmed")
    public void assertEmailVerified() {
        page().assertSuccessPopup();
        page().dismissPopup();
    }
}
