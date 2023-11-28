package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

public class ManageMyVid extends BaseClass {

	@Test(groups = "MMV", priority = 2)
	public void manageMyVid() throws Exception {
		LoginTest.loginTest();

		test = extent.createTest(" Test ManageMyVid", "verify Login");
		Commons.click(test, driver, By.id("uinservices/managemyvid"));
		Commons.click(test, driver, By.id("Perpetual"));
		test.log(Status.INFO, "Click on Perpetual");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.xpath("(//img[@id='download0'])[1]"));
		test.log(Status.INFO, "Click on download");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));

		// Commons.click(test,driver, By.id("uinservices/managemyvid"));
		Commons.click(test, driver, By.id("Temporary"));
		test.log(Status.INFO, "Click on Temporary");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.xpath("(//img[@id='download0'])[2]"));
		test.log(Status.INFO, "Click on download");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.xpath("(//mat-icon[@id='delete0'])[2]"));
		test.log(Status.INFO, "Click on delete");
		Commons.click(test, driver, By.xpath("//span[text()=' Delete ']"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Thread.sleep(3000);

		// Commons.click(test,driver, By.id("uinservices/managemyvid"));
		Commons.click(test, driver, By.id("OneTimeUse"));
		test.log(Status.INFO, "Click on OneTimeUse");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.xpath("(//img[@id='download0'])[2]"));
		test.log(Status.INFO, "Click on download");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.xpath("(//mat-icon[@id='delete0'])[2]"));
		test.log(Status.INFO, "Click on delete");
		Commons.click(test, driver, By.id("confirmmessagepopup"));
		Commons.click(test, driver, By.id("confirmmessagepopup"));

//	    
	}

}
