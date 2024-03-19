package io.mosip.testrig.residentui.testcase;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.JsonUtil;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.ResidentBaseClass;
import io.mosip.testrig.residentui.utility.TestRunner;

@Test(groups = "GMU",dependsOnGroups="UMD")
public class GetMyUIN extends ResidentBaseClass{
	
	String vid =TestRunner.perpetualVid;
	@Test(priority=0)
	public void getMyUIN() throws Exception {
		Thread.sleep(20000);
		String tempemail = ConfigManager.gettempemail();
		test = extent.createTest("getMyUIN", "verify Login");
		Commons.click(test, driver, By.id("getMyUIN"));
		Commons.enter(test, driver, By.id("aidValue"), vid);//
		test.log(Status.INFO, "AID Entered");
		Commons.click(test, driver, By.id("getUinSendOtpBtn"));
		String otp = MockSMTPListener.getOtp(tempemail);
		Thread.sleep(5000);
		Commons.enter(test, driver, By.id("otp-input"), otp);
		Commons.click(test, driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		test.log(Status.INFO, "Click on submit");
		Commons.click(test, driver, By.id("dismissBtn"));
		
		
		
	}
	@Test(priority=1)
	public void getMyUINWithoutAID() throws InterruptedException, IOException {
		test=extent.createTest("get MyUIN Without AID", "verify Login");
		Thread.sleep(20000);
		Commons.click(test, driver, By.id("getMyUIN"));
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
	}
	
	@Test(priority=2)
	public void getMyUINWithInvalidOtp() throws Exception {
		String tempemail = ConfigManager.gettempemail();
		Thread.sleep(20000);
		test=extent.createTest("get My UIN With Invalid Otp", "verify Login");
		Commons.click(test, driver, By.id("getMyUIN"));
		Commons.enter(test,driver, By.id("aidValue"), vid);
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
		String otp = MockSMTPListener.getOtp(tempemail);
		Thread.sleep(5000);
		Commons.enter(test,driver, By.id("otp-input"), otp+"56");
		   Commons.click(test,driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		   Commons.click(test,driver, By.id("dismissBtn"));
		
	}
	@Test(priority=3)
	public void getMyUINWithExpiredOtp() throws Exception {
	//	String tempemail = ConfigManager.gettempemail();
		Thread.sleep(20000);
		test=extent.createTest("get My UIN With Expired Otp", "verify Login");
		Commons.click(test, driver, By.id("getMyUIN"));//id
		Commons.enter(test,driver, By.id("aidValue"), vid);//
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
	//	String otp = MockSMTPListener.getOtp(tempemail);
		Thread.sleep(5000);
		Commons.enter(test,driver, By.id("otp-input"),"12345");
		   Commons.click(test,driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		   Commons.click(test,driver, By.id("dismissBtn"));
		   
	}
	
	@Test(priority=4)
	public void getMyUINWithoutOtp() throws Exception {
		Thread.sleep(20000);
		test=extent.createTest("get My UIN Without Otp", "verify Login");
		Commons.click(test, driver, By.id("getMyUIN"));//id
		Commons.enter(test,driver, By.id("aidValue"), vid);
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
		Thread.sleep(5000);
		   Commons.click(test,driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		   
	}
}
