package io.mosip.testrig.residentui.testcase;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.JsonUtil;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.ResidentBaseClass;

@Test(groups = "GMU")
public class GetMyUIN extends ResidentBaseClass{

	// @Test(groups = "GMU")
	public void getMyUIN() throws Exception {
		String tempemail = JsonUtil.JsonObjParsing(Commons.getTestData(), "tempemail");
		String aid = JsonUtil.JsonObjParsing(Commons.getTestData(), "aid");

		test = extent.createTest("getMyUIN", "verify Login");
		Commons.click(test, driver, By.xpath("(//*[@id='dashboardCard4'])[1]"));
		Commons.enter(test, driver, By.id("aidValue"), aid);//
		test.log(Status.INFO, "AID Entered");
		Commons.switchToFrameByIndex(test, driver, 0);
		Commons.click(test, driver, By.xpath("//div[@id='rc-anchor-container']"));
		Thread.sleep(3000);
		driver.switchTo().parentFrame();
		Commons.click(test, driver, By.id("getUinSendOtpBtn"));
		String otp = MockSMTPListener.getOtp(tempemail);
		Commons.enter(test, driver, By.id("otp-input"), otp);
		Commons.click(test, driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		test.log(Status.INFO, "Click on submit");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		
		
		
	}
	
	public void getMyUINWithoutAID() throws InterruptedException, IOException {
		test=extent.createTest("get MyUIN Without AID", "verify Login");
		Commons.click(test, driver, By.xpath("(//*[@id='dashboardCard4'])[1]"));
		Commons.switchToFrameByIndex(test,driver, 0);
		Commons.click(test,driver, By.xpath("//div[@id='rc-anchor-container']"));
		Thread.sleep(3000);
		driver.switchTo().parentFrame();
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
	}
	
	
	public void getMyUINWithInvalidOtp() throws Exception {
		String tempemail=JsonUtil.JsonObjParsing(Commons.getTestData(),"tempemail");
		String aid=JsonUtil.JsonObjParsing(Commons.getTestData(),"aid");
		test=extent.createTest("get My UIN With Invalid Otp", "verify Login");
		Commons.click(test, driver, By.xpath("(//*[@id='dashboardCard4'])[1]"));
		Commons.enter(test,driver, By.id("aidValue"), aid);//
		Commons.switchToFrameByIndex(test,driver, 0);
		Commons.click(test,driver, By.xpath("//div[@id='rc-anchor-container']"));
		Thread.sleep(3000);
		driver.switchTo().parentFrame();
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
		String otp = MockSMTPListener.getOtp(tempemail);
		
		Commons.enter(test,driver, By.id("otp-input"), otp+"56");
		   Commons.click(test,driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		   Commons.click(test,driver, By.id("confirmmessagepopup"));
		
	}
	
	public void getMyUINWithExpiredOtp() throws Exception {
		String tempemail=JsonUtil.JsonObjParsing(Commons.getTestData(),"tempemail");
		String aid=JsonUtil.JsonObjParsing(Commons.getTestData(),"aid");
		test=extent.createTest("get My UIN With Expired Otp", "verify Login");
		Commons.click(test, driver, By.xpath("(//*[@id='dashboardCard4'])[1]"));//id
		Commons.enter(test,driver, By.id("aidValue"), aid);//
		Commons.switchToFrameByIndex(test,driver, 0);
		Commons.click(test,driver, By.xpath("//div[@id='rc-anchor-container']"));
		Thread.sleep(3000);
		driver.switchTo().parentFrame();
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
		String otp = MockSMTPListener.getOtp(tempemail);
		Thread.sleep(180020);
		Commons.enter(test,driver, By.id("otp-input"), otp);
		   Commons.click(test,driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		   Commons.click(test,driver, By.id("confirmmessagepopup"));
		   
	}
	
	
	public void getMyUINWithoutOtp() throws Exception {
		test=extent.createTest("get My UIN Without Otp", "verify Login");
		String tempemail=JsonUtil.JsonObjParsing(Commons.getTestData(),"tempemail");
		String aid=JsonUtil.JsonObjParsing(Commons.getTestData(),"aid");
		Commons.click(test, driver, By.xpath("(//*[@id='dashboardCard4'])[1]"));//id
		Commons.enter(test,driver, By.id("aidValue"), aid);//
		Commons.switchToFrameByIndex(test,driver, 0);
		Commons.click(test,driver, By.xpath("//div[@id='rc-anchor-container']"));
		Thread.sleep(3000);
		driver.switchTo().parentFrame();
		Commons.click(test,driver, By.id("getUinSendOtpBtn"));
		   Commons.click(test,driver, By.xpath("//button[@id='getUinsubmitBtn']"));
		   
	}
}
