package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.residentui.pages.GetInformationPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class GetInformationSteps {

    private final GetInformationPage page = new GetInformationPage(PlaywrightFactory.getPage());

    @When("the user navigates to Get Information")
    public void navigate() {
        page.navigateTo();
    }

    @When("the user searches and downloads registration centers for all hierarchy levels")
    public void searchAllHierarchyLevels() {
        page.searchAndDownloadAllHierarchyLevels();
    }

    @Then("all registration centers should be downloaded successfully")
    public void assertAllDownloadsSucceeded() {

        page.assertAllDownloadsSucceeded();
    }
}
