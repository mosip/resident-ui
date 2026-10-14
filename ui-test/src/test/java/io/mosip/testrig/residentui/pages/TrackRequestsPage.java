package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

public class TrackRequestsPage extends BasePage {

    private final VisualValidator visual;

    public TrackRequestsPage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        click("#uinservices\\/trackservicerequest", "Open the Track My Request service");
        waitForNetworkIdle();
    }

    public void enterEventId(String eid) {
        fill("#appIdValue", eid, "Enter the event ID");
    }

    public void search() {
        click("#getEIDStatusbtn", "Click the 'Check Status' button");
        waitForNetworkIdle();
    }

    public void downloadAcknowledgement() {
        click("#downloadAcknowledgementbtn", "Click the 'Download Acknowledgement' button");
        waitForNetworkIdle();
    }

    public void assertResultVisible() {
        visual.assertVisible("#downloadAcknowledgementbtn");
    }

    public void assertResultNotVisible() {
        visual.assertNotVisible("#downloadAcknowledgementbtn");
    }

    public void dismissPopup() {
        click("#dismissBtn", "Dismiss the popup");
    }
}
