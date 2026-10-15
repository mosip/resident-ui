package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.residentui.pages.ManageVidPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class ManageVidSteps {

    private ManageVidPage page() {
        return new ManageVidPage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to Manage My VID")
    public void navigateToManageVid() {
        page().navigateTo();
    }

    @When("the user regenerates the Perpetual VID")
    public void regeneratePerpetualVid() {
        page().regeneratePerpetualVid();
        page().assertVidCreatedPopup();
        page().dismissPopup();
    }

    @When("the user declines downloading the Perpetual VID card")
    public void declinePerpetualVidDownload() {
        page().downloadVidCard(0);
        page().cancelVidCard();
    }

    @When("the user generates a Temporary VID")
    public void generateTemporaryVid() {
        page().generateTemporaryVid();
    }

    @Then("a success popup should confirm VID creation")
    public void assertVidCreated() {
        page().assertVidCreatedPopup();
    }

    @When("the user downloads the Temporary VID card")
    public void downloadTemporaryVidCard() {
        page().dismissPopup();
        page().downloadVidCard(1);
        page().confirmDownloadVidCard();
    }

    @Then("a success popup should confirm the download")
    public void assertVidDownloaded() {
        page().assertVidDownloadedPopup();
    }

    @When("the user deletes the Temporary VID")
    public void deleteTemporaryVid() {
        page().dismissPopup();
        page().deleteVid(1);
        page().confirmDeleteVid();
    }

    @Then("a success popup should confirm VID deletion")
    public void assertVidDeleted() {
        page().assertVidDeletedPopup();
        page().dismissPopup();
    }

    @When("the user generates a One-Time VID")
    public void generateOneTimeVid() {
        page().generateOneTimeVid();
    }

    @When("the user downloads the One-Time VID card")
    public void downloadOneTimeVidCard() {
        page().dismissPopup();
        page().downloadVidCard(1);
        page().confirmDownloadVidCard();
    }

    @When("the user deletes the One-Time VID")
    public void deleteOneTimeVid() {
        page().dismissPopup();
        page().deleteVid(1);
        page().confirmDeleteVid();
    }
}
