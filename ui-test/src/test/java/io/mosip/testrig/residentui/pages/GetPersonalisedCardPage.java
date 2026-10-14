package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

public class GetPersonalisedCardPage extends BasePage {

    private final VisualValidator visual;

    public GetPersonalisedCardPage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        dismissPopupIfPresent("#dismissBtn"); // guard against a leftover popup blocking the click
        click("#uinservices\\/personalisedcard", "Open the Personalised Card service");
        waitForNetworkIdle();
    }

    public void selectFullName()     { click("#fullName", "Select the 'Full Name' attribute"); }
    public void selectDateOfBirth()  { click("#dateOfBirth", "Select the 'Date of Birth' attribute"); }
    public void selectUin()          { click("#UIN", "Select the 'UIN' attribute"); }
    public void selectPerpetualVid() { click("#perpetualVID", "Select the 'Perpetual VID' attribute"); }
    public void selectPhone()        { click("#phone", "Select the 'Phone' attribute"); }
    public void selectEmail()        { click("#email", "Select the 'Email' attribute"); }
    public void selectFullAddress()  { click("#fullAddress", "Select the 'Full Address' attribute"); }
    public void selectGender()       { click("#gender", "Select the 'Gender' attribute"); }
    public void selectPhoto()        { click("#photo", "Select the 'Photo' attribute"); }

    public void downloadCard() {
        click("#downloadFileBtn", "Click the 'Download' button");
        waitForNetworkIdle();
    }

    public void assertDownloadSuccessPopup() {
        visual.assertSuccessPopup();
    }

    public void dismissPopup() {
        click("#dismissBtn", "Dismiss the popup");
    }
}
