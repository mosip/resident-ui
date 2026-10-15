package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.residentui.pages.ManageVidPage;
import io.mosip.testrig.residentui.pages.TrackRequestsPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class TrackRequestsSteps {

    private TrackRequestsPage trackPage() {
        return new TrackRequestsPage(PlaywrightFactory.getPage());
    }

    private ManageVidPage vidPage() {
        return new ManageVidPage(PlaywrightFactory.getPage());
    }

    @When("the user generates a Temporary VID to get an event ID")
    public void generateTempVidForEid() {
        vidPage().navigateTo();
        vidPage().generateTemporaryVid();
    }

    @When("the user tracks the generated event ID")
    public void trackGeneratedEid() {
        String eid = vidPage().extractEidFromPopupHeader();
        vidPage().dismissPopup();
        PlaywrightFactory.getPage().goBack();
        PlaywrightFactory.getPage().waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        trackPage().navigateTo();
        trackPage().enterEventId(eid);
        trackPage().search();
    }

    @Then("the event ID details should be visible")
    public void assertEventIdVisible() {
        trackPage().assertResultVisible();
    }

    @When("the user downloads the acknowledgement")
    public void downloadAcknowledgement() {
        trackPage().downloadAcknowledgement();
    }

    @When("the user tracks an invalid event ID")
    public void trackInvalidEid() {
        trackPage().navigateTo();
        // #appIdValue requires a 16-digit numeric EID — pad the (13-digit) epoch millis with zeros.
        trackPage().enterEventId(String.format("%016d", System.currentTimeMillis()));
        trackPage().search();
        trackPage().dismissPopup();
    }

    @Then("the event ID details should not be visible")
    public void assertEventIdNotVisible() {
        trackPage().assertResultNotVisible();
    }
}
