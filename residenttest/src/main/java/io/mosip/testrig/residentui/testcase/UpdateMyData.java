package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.JsonUtil;
import io.mosip.testrig.residentui.utility.MockSMTPListener;

public class UpdateMyData extends BaseClass {
	@Test(groups = "UMD")
	public void updateMyData() throws Exception {
		String tempemail1 = JsonUtil.JsonObjParsing(Commons.getTestData(), "tempemail1");
		LoginTest.loginTest();
		test = extent.createTest("updateMyData Test ", "verify Login");
		Commons.clickWebelement(test, driver, By.id("uinservices/updatedemographic"));
		Commons.enter(test, driver, By.id("fullName"), data);
		Commons.dropdown(test, driver, By.id("gender"));
		test.log(Status.INFO, "Click on gender");
		Commons.enter(test, driver, By.id("proofOfIdentityvalue"), data);
		Commons.dropdown(test, driver, By.id("proofOfIdentity"));
		Commons.enter(test, driver, By.id("fileDropRef"),
				System.getProperty("user.dir") + "\\BulkUploadFiles\\tonyId.png");
		Commons.clickWebelement(test, driver, By.id("submit"));
		test.log(Status.INFO, "Click on submit");
		Commons.clickWebelement(test, driver, By.xpath("//div[@class='preview']"));
		Commons.clickWebelement(test, driver, By.xpath("//span[text()='Update']"));
		Commons.clickWebelement(test, driver, By.xpath("//input[@type='checkbox']"));
		Commons.clickWebelement(test, driver, By.xpath("//span[text()='Submit']"));
		test.log(Status.INFO, "Click on Submit");
		Commons.clickWebelement(test, driver, By.id("confirmmessagepopup"));
		Thread.sleep(3000);

		test.log(Status.INFO, "Update Address");
		Commons.clickWebelement(test, driver, By.id("uinservices/updatedemographic"));
		// Commons.click(test,driver, By.xpath("//span[text()='Demographic Data']"));
		Commons.clickWebelement(test, driver, By.xpath("//div[text()='Address']"));
		Commons.enter(test, driver, By.id("addressLine1"), data);
		Commons.dropdown(test, driver, By.id("Region"));
		Commons.dropdown(test, driver, By.id("Province"));
		Commons.dropdown(test, driver, By.id("City"));
		Commons.dropdown(test, driver, By.id("Zone"));
		Commons.dropdown(test, driver, By.id("Postal Code"));
		Commons.dropdown(test, driver, By.id("proofOfAddress"));
		Commons.enter(test, driver, By.xpath("//input[@id='proofOfAddress']"), data);
		Commons.enter(test, driver, By.id("fileAddRef"),
				System.getProperty("user.dir") + "\\BulkUploadFiles\\tonyId.png");
		test.log(Status.INFO, "file Uploaded");
		Commons.clickWebelement(test, driver, By.xpath("//div[@class='preview']"));
		Commons.clickWebelement(test, driver, By.id("submit"));
		Commons.clickWebelement(test, driver, By.xpath("//span[text()='Update']"));
		Commons.clickWebelement(test, driver, By.xpath("//input[@type='checkbox']"));
		Commons.clickWebelement(test, driver, By.xpath("//span[text()='Submit']"));
		test.log(Status.INFO, "Click on Submit");
		Commons.clickWebelement(test, driver, By.id("confirmmessagepopup"));

		test.log(Status.INFO, "Update Email");
		Commons.click(test, driver, By.id("uinservices/updatedemographic"));
		// Commons.click(test,driver, By.xpath("//span[text()='Demographic Data']"));
		Commons.clickWebelement(test, driver, By.xpath("//div[text()='Contact']"));

		Commons.enter(test, driver, By.id("email"), tempemail1);
		Commons.enter(test, driver, By.id("confirmemail"), tempemail1);
		Commons.clickWebelement(test, driver, By.id("sendOTPemail"));
		String otp = MockSMTPListener.getOtp(tempemail1);
		System.out.println(otp);
//		    for(int i=0;i<=otp.length()-1;i++) {
//		    Commons.enter(driver, By.xpath("//*[@class=\"pincode-input-text\"]["+(i+1)+"]"), Character.toString(otp.charAt(i)));}

		Commons.enter(test, driver, By.id("otp-input"), otp);
		Commons.clickWebelement(test, driver, By.xpath("(//button[@class='button mat-button'])[2]"));
		Commons.clickWebelement(test, driver, By.id("confirmmessagepopup"));

		// Lang prefrence
		test.log(Status.INFO, "Select Language Preference");
		Commons.click(test, driver, By.id("uinservices/updatedemographic"));
		// Commons.click(test,driver, By.xpath("//span[text()='Demographic Data']"));
		Commons.clickWebelement(test, driver, By.xpath("//div[text()='Language Preference']"));
		Commons.dropdown(test, driver, By.id("preferredLang"));
		Commons.clickWebelement(test, driver, By.id("submit"));

//		without selecting doc type
//		Commons.click(test,driver, By.xpath("(//mat-card[@class='mini-card mat-card'])[7]"));
//		Commons.click(test,driver, By.xpath("//span[text()='Demographic Data']"));
//	    Commons.enter(driver, By.id("fullName"), data);
//		Commons.dropdown(driver, By.id("gender"));
//		Commons.enter(driver, By.id("proofOfIdentityvalue"), "photoId");
//		Commons.enter(driver, By.id("fileDropRef"), "D:\\GItAuto\\resident\\resident-ui\\admintest\\BulkUploadFiles\\tonyId.png");
//		Commons.click(test,driver, By.id("submit"));
//		Commons.click(test,driver, By.xpath("//div[@class='preview']"));
//		Commons.click(test,driver, By.xpath("//span[text()='Update']"));
//		Commons.click(test,driver, By.xpath("//input[@type='checkbox']"));
//		Commons.click(test,driver, By.xpath("//span[text()='Submit']"));
//		Commons.click(test,driver, By.id("confirmmessagepopup"));
//		
//		Thread.sleep(3000);
//		Commons.click(test,driver, By.xpath("//span[text()='Demographic Data']"));
		// Female

//		
	}

	public void UpdateDataWithNameAndDOB() throws Exception {
		LoginTest.loginTest();
		test = extent.createTest("UpdateDataWithNameAndDOB ", "verify Login");
		Commons.clickWebelement(test, driver, By.xpath("(//mat-card[@class='mini-card mat-card'])[7]"));
		Commons.enter(test, driver, By.id("fullName"), data);
		Commons.clickWebelement(test, driver, By.id("dateOfBirth"));
		Commons.clickWebelement(test, driver,
				By.xpath("//*[@id=\"mat-datepicker-0\"]/mat-calendar-header/div/div/button[2]"));
		Commons.clickWebelement(test, driver,
				By.xpath("//*[@id=\"mat-datepicker-0\"]/div/mat-month-view/table/tbody/tr[4]/td[6]/div"));
		Commons.dropdown(test, driver, By.id("proofOfIdentity"));
		test.log(Status.INFO, "Select ProofOfIdentity");
		Commons.enter(test, driver, By.id("fileDropRef"),
				System.getProperty("user.dir") + "\\BulkUploadFiles\\tonyId.png");
		Commons.clickWebelement(test, driver, By.xpath("//div[@class='preview']"));
		Commons.clickWebelement(test, driver, By.id("submit"));
		test.log(Status.INFO, "Click on Submit");

	}

	public void UpdateDataWithNameAndGender() throws Exception {
		LoginTest.loginTest();
		test = extent.createTest("UpdateDataWithNameAndGender ", "verify Login");
		Commons.clickWebelement(test, driver, By.xpath("(//mat-card[@class='mini-card mat-card'])[7]"));
		Commons.enter(test, driver, By.id("fullName"), data);
		Commons.dropdown(test, driver, By.id("gender"), "Female");
		Commons.dropdown(test, driver, By.id("proofOfIdentity"));
		test.log(Status.INFO, "Select ProofOfIdentity");
		Commons.enter(test, driver, By.id("fileDropRef"),
				System.getProperty("user.dir") + "\\BulkUploadFiles\\tonyId.png");
		Commons.clickWebelement(test, driver, By.xpath("//div[@class='preview']"));
		Commons.clickWebelement(test, driver, By.id("submit"));
	}

	public void UpdateDataWithoutAddressLine() throws Exception {
		LoginTest.loginTest();
		test = extent.createTest("UpdateDataWithoutAddressLine ", "verify Login");
		Commons.clickWebelement(test, driver, By.xpath("(//mat-card[@class='mini-card mat-card'])[7]"));
		Commons.clickWebelement(test, driver, By.xpath("//div[text()='Address']"));
		Commons.dropdown(test, driver, By.id("Region"));
		// Commons.dropdown(driver, By.id("Province"));
		Commons.dropdown(test, driver, By.id("Province"), " Rabat ");
		Commons.dropdown(test, driver, By.id("City"), " Rabat ");
		// Commons.dropdown(driver, By.id("City"));
		// Agdal
		Commons.dropdown(test, driver, By.id("Zone"), " Agdal ");
		// Commons.dropdown(driver, By.id("Zone"));
		// Commons.dropdown(driver, By.id("Postal Code"));
		Commons.dropdown(test, driver, By.id("Postal Code"), " 10106 ");
		Commons.dropdown(test, driver, By.id("proofOfAddress"));
		Commons.enter(test, driver, By.xpath("//input[@id='proofOfAddress']"), data);
		Commons.enter(test, driver, By.id("fileAddRef"),
				System.getProperty("user.dir") + "\\BulkUploadFiles\\tonyId.png");
		test.log(Status.INFO, "File Uploaded");
		Commons.clickWebelement(test, driver, By.xpath("//div[@class='preview']"));
		Commons.clickWebelement(test, driver, By.id("submit"));
		test.log(Status.INFO, "Click on Submit");
	}

}
