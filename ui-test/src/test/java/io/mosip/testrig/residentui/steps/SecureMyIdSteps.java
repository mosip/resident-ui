package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.residentui.pages.SecureMyIdPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class SecureMyIdSteps {

    private SecureMyIdPage page() {
        return new SecureMyIdPage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to Secure My ID")
    public void navigateToSecureMyId() {
        page().navigateTo();
    }

    @When("the user toggles auth type {int}")
    public void toggleAuthType(int typeIndex) {
        page().toggleAuthType(typeIndex);
    }

    @Then("the auth lock change should be confirmed")
    public void assertLockChangeConfirmed() {
        page().assertSuccessPopup();
        page().dismissPopup();
    }
}
