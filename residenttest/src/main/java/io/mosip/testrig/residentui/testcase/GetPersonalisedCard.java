package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

public class GetPersonalisedCard extends BaseClass {

	@Test(groups = "GPC")
	public void GetPersonalisedCard() throws Exception {
		LoginTest.loginTest();
		test = extent.createTest("Get Personalised Card", "verify Login");
		Commons.click(test, driver, By.id("uinservices/personalisedcard"));

		Commons.click(test, driver, By.id("fullName"));
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver, By.id("UIN"));

		Commons.click(test, driver, By.id("downloadFileBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));
		test.log(Status.INFO, "Click on download");
		Commons.click(test, driver, By.id("uinservices/personalisedcard"));

		Commons.click(test, driver, By.id("fullName"));
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver, By.id("UIN"));
		Commons.clickWebelement(test, driver, By.id("perpetualVID"));
		Commons.clickWebelement(test, driver, By.id("phone"));

//		Commons.click(test, driver, By.id("Mask It2"));
//		Commons.click(test, driver, By.id("Mask It3"));
//		Commons.click(test, driver, By.id("Mask It4"));
		//Commons.click(test, driver, By.id("Mask It3"));
		//Commons.click(test, driver, By.id("Mask It4"));
		Commons.click(test, driver, By.id("downloadFileBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));

		test.log(Status.INFO, "Submit with any three checkbox with masked  ");
//		Commons.click(test,driver, By.id("uinservices/personalisedcard"));
//		Commons.click(test,driver, By.xpath("//span[text()='Name']"));
//		Commons.click(test,driver, By.xpath("//span[text()='DOB']"));
//		
//		Commons.click(test,driver, By.id("downloadFileBtn"));
//		Commons.click(test,driver, By.id("confirmmessagepopup"));
//		Thread.sleep(3000);
//
//		Commons.click(test,driver, By.id("Mask It2"));
//		Commons.click(test,driver, By.id("Mask It3"));
//		Commons.click(test,driver, By.id("Mask It4"));
//		Commons.click(test,driver, By.id("downloadFileBtn"));
//		Commons.click(test,driver, By.id("confirmmessagepopup"));

		Commons.click(test, driver, By.id("uinservices/personalisedcard"));
		Commons.clickWebelement(test, driver, By.id("fullName"));
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver, By.id("UIN"));
		Commons.clickWebelement(test, driver, By.id("perpetualVID"));
		Commons.clickWebelement(test, driver, By.id("phone"));
		Commons.clickWebelement(test, driver, By.id("email"));
		Commons.clickWebelement(test, driver, By.id("fullAddress"));
		Commons.clickWebelement(test, driver, By.id("gender"));
		Commons.clickWebelement(test, driver, By.id("photo"));
		Commons.click(test, driver, By.id("downloadFileBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));
		test.log(Status.INFO, "Select all check box");

		Commons.click(test, driver, By.id("uinservices/personalisedcard"));
		Commons.clickWebelement(test, driver, By.id("fullName"));
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver, By.id("UIN"));
		Commons.clickWebelement(test, driver, By.id("perpetualVID"));
		Commons.clickWebelement(test, driver, By.id("phone"));
		Commons.clickWebelement(test, driver, By.id("email"));
		Commons.clickWebelement(test, driver, By.id("fullAddress"));
		Commons.clickWebelement(test, driver, By.id("gender"));
		Commons.clickWebelement(test, driver, By.id("photo"));
//		Commons.click(test, driver, By.id("Mask It2"));
//		Commons.click(test, driver, By.id("Mask It3"));
//		Commons.click(test, driver, By.id("Mask It4"));
//		Commons.click(test, driver, By.id("Mask It5"));
	//	Commons.click(test, driver, By.xpath("//input[@id='Mask it2-input']"));
	//	Commons.click(test, driver, By.xpath("//input[@id='Mask it3-input']"));
	//	Commons.click(test, driver, By.xpath("//input[@id='Mask it4-input']"));
	//	Commons.click(test, driver, By.xpath("//input[@id='Mask it5-input']"));
		test.log(Status.INFO, "Select all checbox masked");
		Commons.click(test, driver, By.id("downloadFileBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));
		test.log(Status.INFO, "Click on Submit");
	}

}
