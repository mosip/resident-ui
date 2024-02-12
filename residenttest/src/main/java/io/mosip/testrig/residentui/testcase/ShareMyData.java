package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

public class ShareMyData extends BaseClass {

	@Test(groups = "SMD")
	public void shareMyData() throws Exception {
		LoginTest.loginTest();

		test = extent.createTest("shareMyData Test", "verify shareMyData");
		Commons.clickWebelement(test, driver, By.id("uinservices/sharewithpartner"));

		Commons.clickWebelement(test, driver, By.id("name"));
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver, By.id("UIN"));
		Commons.dropdown(test, driver, By.id("partnerDetails"));
		test.log(Status.INFO, "Select Dropdown PartnerDetails");
		Commons.enter(test, driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		Commons.clickWebelement(test, driver, By.id("confirmShareInfo"));
		Commons.clickWebelement(test, driver, By.id("shareInfoBtn"));
		test.log(Status.INFO, "Click On Share");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

		// without partner name and purpose
		Thread.sleep(3000);
		test.log(Status.INFO, "Click On Share without partner name and purpose");
		Commons.clickWebelement(test, driver, By.id("uinservices/sharewithpartner"));
		Commons.clickWebelement(test, driver, By.id("name"));
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver, By.id("UIN"));
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		test.log(Status.INFO, "Click On Share");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

		// without partner name
		Thread.sleep(3000);
		test.log(Status.INFO, "Click On Share without partner name");
		Commons.enter(test, driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

		// without purpose
		test.log(Status.INFO, "Click On without purpose");
		driver.findElement(By.id("sharingReasonPlaceholder")).clear();
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

		// all with valid
		test.log(Status.INFO, "all with valid");
		Commons.clickWebelement(test, driver, By.id("perpetualVID"));
		Commons.clickWebelement(test, driver, By.id("phone"));
		Commons.clickWebelement(test, driver, By.id("email"));
		Commons.clickWebelement(test, driver, By.id("fullAddress"));
		Commons.clickWebelement(test, driver, By.id("gender"));
		Commons.clickWebelement(test, driver, By.id("photo"));
//		Commons.clickWebelement(test, driver, By.id("Mask It2"));
	//	Commons.clickWebelement(test, driver, By.id("Mask It3"));
		//Commons.clickWebelement(test, driver, By.id("Mask It4"));
//		Commons.clickWebelement(test, driver, By.id("Mask It5"));
//		Commons.click(test, driver, By.id("Mask It2"));
//		Commons.click(test, driver, By.id("Mask It3"));
//		Commons.click(test, driver, By.id("Mask It4"));
//		Commons.click(test, driver, By.id("Mask It5"));
		test.log(Status.INFO, "Click on Mask");
		Commons.dropdown(test, driver, By.id("partnerDetails"));
		test.log(Status.INFO, "Select Dropdown");
		Commons.enter(test, driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		Commons.clickWebelement(test, driver, By.id("confirmShareInfo"));
		Commons.clickWebelement(test, driver, By.id("shareInfoBtn"));
		test.log(Status.INFO, "Click On Share");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

		// without purpose and partner
		test.log(Status.INFO, "Share My Data without purpose and partner");
		Commons.clickWebelement(test, driver, By.id("uinservices/sharewithpartner"));
		Commons.clickWebelement(test, driver, By.id("name"));
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver, By.id("UIN"));
		Commons.clickWebelement(test, driver, By.id("perpetualVID"));
		Commons.clickWebelement(test, driver, By.id("phone"));
		Commons.clickWebelement(test, driver, By.id("email"));
		Commons.clickWebelement(test, driver, By.id("fullAddress"));
		Commons.clickWebelement(test, driver, By.id("gender"));
		Commons.clickWebelement(test, driver, By.id("photo"));
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		test.log(Status.INFO, "Click Share");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

		// without partnername
		test.log(Status.INFO, "Share My Data without partnername");
		Commons.enter(test, driver, By.id("sharingReasonPlaceholder"), data);
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		test.log(Status.INFO, "Click Share");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));
		driver.findElement(By.id("sharingReasonPlaceholder")).clear();

		// without purpose
		test.log(Status.INFO, "Share My Data without purpose");
		Commons.dropdown(test, driver, By.id("partnerDetails"));
		Commons.clickWebelement(test, driver, By.id("shareBtn"));
		test.log(Status.INFO, "Click Share");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

	}
}
