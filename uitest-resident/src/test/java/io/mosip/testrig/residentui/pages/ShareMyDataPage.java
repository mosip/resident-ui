package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

public class ShareMyDataPage extends BasePage {

    private final VisualValidator visual;

    public ShareMyDataPage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        dismissPopupIfPresent("#dismissBtn"); // guard against a leftover popup blocking the click
        click("#uinservices\\/sharewithpartner", "Open the Share My Data service");
        waitForNetworkIdle();
    }

    // Field id follows the identity schema's attributeName, which varies per environment.
    public void selectFullName() {
        page.locator("#fullName, #name").first().click();
    }

    public void selectDateOfBirth()  { click("#dateOfBirth", "Select the 'Date of Birth' attribute"); }
    public void selectUin()          { click("#UIN", "Select the 'UIN' attribute"); }
    public void selectPerpetualVid() { click("#perpetualVID", "Select the 'Perpetual VID' attribute"); }
    public void selectPhone()        { click("#phone", "Select the 'Phone' attribute"); }
    public void selectEmail()        { click("#email", "Select the 'Email' attribute"); }
    public void selectFullAddress()  { click("#fullAddress", "Select the 'Full Address' attribute"); }
    public void selectGender()       { click("#gender", "Select the 'Gender' attribute"); }
    public void selectPhoto()        { click("#photo", "Select the 'Photo' attribute"); }

    public void selectPartner() {
        page.locator("#partnerDetails").click();
        page.locator("mat-option").first().click();
    }

    public void fillSharingReason(String reason) {
        fill("#sharingReasonPlaceholder", reason, "Enter the reason for sharing data");
    }

    public void clearSharingReason() {
        page.locator("#sharingReasonPlaceholder").clear();
    }

    public void clickShare() {
        click("#shareBtn", "Click the 'Share' button");
    }

    public void confirmShare() {
        click("#confirmShareInfo", "Tick the share-information consent checkbox");
        click("#shareInfoBtn", "Confirm sharing the information");
        waitForNetworkIdle();
    }

    public void assertShareSuccessPopup() {
        visual.assertSuccessPopup();
    }

    public void dismissPopup() {
        click("#dismissBtn", "Dismiss the popup");
    }
}
