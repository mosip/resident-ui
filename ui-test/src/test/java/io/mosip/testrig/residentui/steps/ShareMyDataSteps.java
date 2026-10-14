package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.residentui.hooks.ResidentTestData;
import io.mosip.testrig.residentui.pages.ShareMyDataPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class ShareMyDataSteps {

    private ShareMyDataPage page() {
        return new ShareMyDataPage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to Share My Data")
    public void navigateToShareMyData() {
        page().navigateTo();
    }

    @When("the user selects basic data fields and a partner")
    public void selectBasicFieldsAndPartner() {
        page().selectFullName();
        page().selectDateOfBirth();
        page().selectUin();
        page().selectPartner();
        page().fillSharingReason(ResidentTestData.testLabel());
    }

    @When("the user selects all data fields and a partner")
    public void selectAllFieldsAndPartner() {
        page().selectFullName();
        page().selectDateOfBirth();
        page().selectUin();
        page().selectPerpetualVid();
        page().selectPhone();
        page().selectEmail();
        page().selectFullAddress();
        page().selectGender();
        page().selectPhoto();
        page().selectPartner();
        page().fillSharingReason(ResidentTestData.testLabel());
    }

    @When("the user confirms the data share")
    public void confirmShare() {
        page().clickShare();
        page().confirmShare();
    }

    @Then("the data share should be confirmed")
    public void assertShareConfirmed() {
        page().assertShareSuccessPopup();
        page().dismissPopup();
    }
}
