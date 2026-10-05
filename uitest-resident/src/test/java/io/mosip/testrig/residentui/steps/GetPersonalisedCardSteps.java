package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.residentui.pages.GetPersonalisedCardPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class GetPersonalisedCardSteps {

    private GetPersonalisedCardPage page() {
        return new GetPersonalisedCardPage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to Get Personalised Card")
    public void navigate() {
        page().navigateTo();
    }

    @When("the user selects basic card fields")
    public void selectBasicFields() {
        page().selectFullName();
        page().selectDateOfBirth();
        page().selectUin();
    }

    @When("the user selects all card fields")
    public void selectAllFields() {
        page().selectFullName();
        page().selectDateOfBirth();
        page().selectUin();
        page().selectPerpetualVid();
        page().selectPhone();
        page().selectEmail();
        page().selectFullAddress();
        page().selectGender();
        page().selectPhoto();
    }

    @When("the user downloads the personalised card")
    public void downloadCard() {
        page().downloadCard();
    }

    @Then("the personalised card download should be confirmed")
    public void assertDownloadConfirmed() {
        page().assertDownloadSuccessPopup();
        page().dismissPopup();
    }
}
