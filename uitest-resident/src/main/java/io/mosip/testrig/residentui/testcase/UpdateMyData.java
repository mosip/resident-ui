package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.TestRunner;
@Test(groups = "UMD")
public class UpdateMyData extends BaseClass {
	
	public void updateMyDataName() throws Exception {
		LoginTest.loginTest();
		test = extent.createTest("updateMyData Test ", "verify Login");
		Commons.clickWebelement(test, driver, By.id("uinservices/updatedemographic"));
		Commons.enter(test, driver, By.id("fullName"+BaseClass.envsupportlang()), Commons.generateRandomAlphabetString());
		Commons.dropdown(test, driver, By.id("gender"));
		test.log(Status.INFO, "Click on gender");
		Commons.enter(test, driver, By.id("proofOfIdentityvalue"), data);
		Commons.dropdown(test, driver, By.id("proofOfIdentity"));
		Commons.enter(test, driver, By.id("fileDropRef"),
				TestRunner.getResourcePath() + "//BulkUploadFiles//tonyId.png");
		Thread.sleep(2000);
		Commons.clickWebelement(test, driver, By.id("previewBtn"));
		test.log(Status.INFO, "Click on submit");	
		Commons.clickWebelement(test, driver, By.id("submitBtn"));
		Commons.clickWebelement(test, driver, By.id("confirmUpdateData"));
    	Commons.clickWebelement(test, driver, By.id("updateMyDataBtn"));	
		test.log(Status.INFO, "Click on Submit");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));
	}
	
	
	public void updateMyDataAddress() throws Exception {
	
		test.log(Status.INFO, "Update Address");
		LoginTest.loginTest();
		Commons.clickWebelement(test, driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement(test, driver, By.xpath("//div[@id='mat-tab-label-0-1']"));
		
		
		Commons.enter(test, driver, By.id("addressLine1"+BaseClass.envsupportlang()),  Commons.generateRandomAlphabetString());
		Commons.dropdown(test, driver, By.id("Region"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("Province"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("City"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("Zone"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("Postal Code"));
		Commons.dropdown(test, driver, By.id("proofOfAddress"));
		Commons.enter(test, driver, By.id("proofOfAddressvalue"), data);
		Commons.enter(test, driver, By.id("fileAddRef"),
				TestRunner.getResourcePath() + "//BulkUploadFiles//tonyId.png");
		test.log(Status.INFO, "file Uploaded");
		Thread.sleep(2000);
		Commons.clickWebelement(test, driver, By.id("previewBtn"));
		
		
		Commons.clickWebelement(test, driver, By.id("submitBtn"));
		Commons.clickWebelement(test, driver, By.id("confirmUpdateData"));
    	Commons.clickWebelement(test, driver, By.id("updateMyDataBtn"));
		test.log(Status.INFO, "Click on Submit");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

	}
	public void UpdateDataWithEmail() throws Exception {
		String tempemail1 = ConfigManager.gettempemail1();
		LoginTest.loginTest();
		test.log(Status.INFO, "Update Email");
		Commons.click(test, driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement(test, driver, By.xpath("//div[@id='mat-tab-label-0-2']"));
		Commons.enter(test, driver, By.id("email"), tempemail1);	
		Commons.clickWebelement(test, driver, By.id("sendOTPemail"));
		String otp = MockSMTPListener.getOtp(tempemail1);
		System.out.println(otp);
    	Commons.enter(test, driver, By.id("otp-input"), otp);
		Commons.clickWebelement(test, driver, By.id("submitOtpBtn"));
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));

	}
	public void UpdateDataWithLanguage() throws Exception {
		LoginTest.loginTest();
		test.log(Status.INFO, "Select Language Preference");
		Commons.click(test, driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement(test, driver, By.xpath("//div[@id='mat-tab-label-0-3']"));
		Commons.dropdown(test, driver, By.id("preferredLang"));
		Commons.clickWebelement(test, driver, By.id("submit"));

		
	}
	
	public void UpdateDataWithoutAddressLine() throws Exception {
		LoginTest.loginTest();
		test = extent.createTest("UpdateDataWithoutAddressLine ", "verify Login");
		Commons.clickWebelement(test, driver, By.id("uinservices/updatedemographic"));
		Commons.clickWebelement(test, driver, By.xpath("//div[@id='mat-tab-label-0-1']"));
		Commons.dropdown(test, driver, By.id("Region"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("Province"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("City"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("Zone"+BaseClass.envsupportlang()));
		Commons.dropdown(test, driver, By.id("Postal Code"));
		Commons.dropdown(test, driver, By.id("proofOfAddress"));
		Commons.enter(test, driver, By.id("proofOfAddressvalue"), data);
		Commons.enter(test, driver, By.id("fileAddRef"),
				TestRunner.getResourcePath() + "//BulkUploadFiles//tonyId.png");
		test.log(Status.INFO, "file Uploaded");
		Thread.sleep(2000);
		Commons.clickWebelement(test, driver, By.id("previewBtn"));
		Commons.clickWebelement(test, driver, By.id("submitBtn"));
		Commons.clickWebelement(test, driver, By.id("confirmUpdateData"));
    	Commons.clickWebelement(test, driver, By.id("updateMyDataBtn"));
		test.log(Status.INFO, "Click on Submit");
		Commons.clickWebelement(test, driver, By.id("dismissBtn"));
		
	}

}
