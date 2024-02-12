package io.mosip.testrig.residentui.testcase;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.JsonUtil;
import io.mosip.testrig.residentui.utility.MockSMTPListener;
import io.mosip.testrig.residentui.utility.ResidentBaseClass;
import io.mosip.testrig.residentui.utility.TestRunner;

@Test(groups = "VPNEEI")
public class VerifyPhoneNumberEmailID extends ResidentBaseClass {



	@Test(priority=0)
	public void VerifyEmailIDWIthInvalidVid() throws IOException, InterruptedException {
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click(test, driver, By.id("emailChannelBtn-button"));
		Commons.enter(test, driver, By.id("uin"), "6534906793542111");
		//		Commons.switchToFrameByIndex(test, driver, 0);
		//		Commons.click(test, driver, By.id("recaptcha-anchor"));
		//		driver.switchTo().parentFrame();
		Commons.click(test, driver, By.id("sendOtpBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));
	}
	@Test(priority=1)
	public void VerifyPhoneWIthInvalidVid() throws IOException, InterruptedException {
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click(test, driver, By.id("phoneChannelBtn"));
		Commons.enter(test, driver, By.id("uin"), "6534906793542111");
		//		Commons.switchToFrameByIndex(test, driver, 0);
		//		Commons.click(test, driver, By.id("recaptcha-anchor"));
		//		driver.switchTo().parentFrame();
		Commons.click(test, driver, By.id("sendOtpBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));

	}
	@Test(priority=2)
	public void VerifyEmailIDWIthInvalidOtp() throws Exception {
		String externalemail = ConfigManager.getexternalemail();
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click(test, driver, By.id("emailChannelBtn-button"));
		Commons.enter(test, driver, By.id("uin"), TestRunner.perpetualVid);
		//		Commons.switchToFrameByIndex(test, driver, 0);
		//		Commons.click(test, driver, By.id("recaptcha-anchor"));
		//		driver.switchTo().parentFrame();
		Thread.sleep(3000);
		Commons.click(test, driver, By.id("sendOtpBtn"));
		// Commons.click(test,driver, By.xpath("//button[text()='Get OTP']"));
		String otp = MockSMTPListener.getOtp(externalemail);
		Thread.sleep(3000);
		Commons.enter(test, driver, By.id("otp-input"), otp + "23");
		Commons.click(test, driver, By.id("submitOtpBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));
	}
	@Test(priority=3)
	public void VerifyEmailIDWIthoutOtp() throws Exception {
		String externalemail = ConfigManager.getexternalemail();
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click(test, driver, By.id("emailChannelBtn-button"));
		Commons.enter(test, driver, By.id("uin"), TestRunner.perpetualVid);
		//		Commons.switchToFrameByIndex(test, driver, 0);
		//		Commons.click(test, driver, By.id("recaptcha-anchor"));
		//		driver.switchTo().parentFrame();
		Thread.sleep(3000);
		Commons.click(test, driver, By.id("sendOtpBtn"));


	}
	@Test(priority=4)
	public void VerifyEmailID() throws Exception {
		String externalemail = ConfigManager.getexternalemail();
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("verifyPhoneNumber/EmailID"));
		Commons.click(test, driver, By.id("emailChannelBtn-button"));
		Commons.enter(test, driver, By.id("uin"), TestRunner.perpetualVid);

		Thread.sleep(3000);
		Commons.click(test, driver, By.id("sendOtpBtn"));
		// Commons.click(test,driver, By.xpath("//button[text()='Get OTP']"));
		String otp = MockSMTPListener.getOtp(externalemail);
		Thread.sleep(3000);
		Commons.enter(test, driver, By.id("otp-input"), otp);
		Commons.click(test, driver, By.id("submitOtpBtn"));
		Commons.click(test, driver, By.id("dismissBtn"));
	}
}
