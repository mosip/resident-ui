package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.apirig.utils.NotificationListener;
import io.mosip.testrig.residentui.hooks.ResidentTestData;
import io.mosip.testrig.residentui.pages.GetMyUINPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class GetMyUINSteps {

    private GetMyUINPage page() {
        return new GetMyUINPage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to Get My UIN")
    public void navigate() {
        page().navigateTo();
    }

    @When("the user requests the UIN card with their VID and OTP")
    public void requestUinCard() {
        page().enterVid(ResidentTestData.perpetualVid);
        NotificationListener.markRequestStart();
        page().requestOtp();
        // Poll both emails — Update My Data may have already switched loginEmail this run.
        String otp = ResidentTestData.getOtpFromCurrentOrPreviousEmail();
        NotificationListener.markRequestRemove();
        if (otp == null || otp.isEmpty()) throw new IllegalStateException("OTP not received for Get My UIN");
        page().enterOtp(otp);
        page().submitOtp();
    }

    @When("the user attempts to get UIN without entering a VID")
    public void requestUinWithoutVid() {
        // #getUinSendOtpBtn is validly disabled with no VID entered — click() waits (up to 30s)
        // for it to become enabled, which it never will, so assert the disabled state directly.
        if (page().isSendOtpButtonEnabled()) {
            throw new AssertionError("Send OTP button should stay disabled when no VID is entered");
        }
    }

    @When("the user requests UIN with an invalid OTP")
    public void requestUinWithInvalidOtp() {
        // Only the backend's rejection matters here, not a real OTP — submit a fixed bogus
        // value straight away instead of waiting on NotificationListener for one to arrive.
        page().enterVid(ResidentTestData.perpetualVid);
        page().requestOtp();
        page().enterOtp("000000");
        page().submitOtp();
    }

    @Then("the UIN card download should be confirmed")
    public void assertUinDownloadConfirmed() {
        page().assertDownloadSuccessPopup();
        page().dismissPopup();
    }

    @Then("the get UIN submission button should not appear")
    public void assertSubmitNotVisible() {
        if (page().isSubmitOtpButtonVisible()) {
            throw new AssertionError("Submit OTP button should not be visible when VID is not entered");
        }
    }

    @When("the portal is dismissed after UIN error")
    public void dismissAfterError() {
        page().dismissPopup();
    }
}
