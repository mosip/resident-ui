package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.browser.SmartWait;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

import java.time.Duration;

public class ManageVidPage extends BasePage {

    private final VisualValidator visual;

    public ManageVidPage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        click("#uinservices\\/managemyvid", "Open the Manage My VID service");
        waitForNetworkIdle();
    }

    // ── Perpetual VID ─────────────────────────────────────────────────────────

    public void regeneratePerpetualVid() {
        click("#Perpetual", "Select 'Perpetual' VID type");
        click("#vidWarningBtn", "Confirm on the VID warning dialog");
    }

    public void downloadVidCard(int listIndex) {
        click("#download0", "Click the 'Download' icon for VID at row " + listIndex, listIndex);
        waitForNetworkIdle();
    }

    public void cancelVidCard() {
        click("#NoBtn", "Click the 'No' button");
    }

    public void confirmDownloadVidCard() {
        click("#vidDownloadBtn", "Confirm the VID card download");
    }

    public void deleteVid(int listIndex) {
        click("#delete0", "Click the 'Delete' icon for VID at row " + listIndex, listIndex);
    }

    public void confirmDeleteVid() {
        click("#vidDeleteBtn", "Confirm the VID deletion");
    }

    // ── Temporary VID ─────────────────────────────────────────────────────────

    public void generateTemporaryVid() {
        click("#Temporary", "Select 'Temporary' VID type");
        click("#vidWarningBtn", "Confirm on the VID warning dialog");
    }

    // ── One-time VID ──────────────────────────────────────────────────────────

    public void generateOneTimeVid() {
        click("#OneTimeUse", "Select 'One-Time Use' VID type");
        click("#vidWarningBtn", "Confirm on the VID warning dialog");
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    public void assertVidCreatedPopup() {
        visual.assertSuccessPopup();
    }

    public void assertVidDownloadedPopup() {
        visual.assertSuccessPopup();
    }

    public void assertVidDeletedPopup() {
        visual.assertSuccessPopup();
    }

    public void dismissPopup() {
        click("#dismissBtn", "Dismiss the popup");
    }

    /** Extracts the numeric event ID from the popup header shown after VID creation. */
    public String extractEidFromPopupHeader() {
        Locator header = page.locator(".pop-up-header");
        // The header renders before the EID is bound in — poll instead of reading it immediately.
        SmartWait.untilTrue(() -> header.textContent().replaceAll("[^0-9]", "").length() >= 5,
                Duration.ofSeconds(10));
        return header.textContent().replaceAll("[^0-9]", "");
    }
}
