package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.JsonUtil;
   @Test(groups = "TMR")
   public class TrackMyRequests extends BaseClass{
	@Test(groups = "TMR",priority = 1)
	public void trackMyRequest() throws Exception {
		 String Eid="";
		 
		//(//span[text()='Event ID : '])[1]
		 LoginTest.loginTest();
		
//		Commons.click(test,driver, By.xpath("(//mat-card[@class='mini-card mat-card'])[4]"));
//		Commons.enter(driver, By.id("appIdValue"),Eid );
//		Commons.click(test,driver, By.id("getEIDStatusbtn"));
//		Commons.click(test,driver, By.id("downloadAcknowledgementbtn"));
		//String eid=driver.findElement(By.xpath("(//span[@class='mat-button-wrapper'])")).getText();
//		Commons.click(test,driver, By.className("mat-card-header-text"));
//		String eid=driver.findElement(By.xpath("//span[text()='Event ID : ']")).getText();
//		System.out.println(eid);
		 test=extent.createTest("TrackMyRequests", "verify Login");
		Commons.click(test,driver, By.id("uinservices/managemyvid"));
		 Commons.click(test,driver, By.id("Temporary"));
		 test.log(Status.INFO, "Click on Temporary VID");
		 Commons.click(test,driver, By.id("vidWarningBtn"));
		 Thread.sleep(2000);
	     String eid	= driver.findElement(By.className("pop-up-header")).getText();
		 System.out.println(eid);
		  Eid = eid.replaceAll("[^0-9]", "");
		 System.out.println(Eid);
		 
		 
		 Commons.click(test,driver, By.id("dismissBtn"));
	driver.navigate().back();
		 Commons.click(test,driver, By.id("uinservices/trackservicerequest"));
		 Commons.enter(test,driver, By.id("appIdValue"), Eid);
		 Thread.sleep(2000);
		 test.log(Status.INFO, "EID Extracted");
		 Commons.clickWebelement(test,driver, By.id("getEIDStatusbtn"));
		 test.log(Status.INFO, "Click on Track");
		 Thread.sleep(2000);
		 Commons.click(test,driver, By.id("downloadAcknowledgementbtn"));
	}

	
	public void TrackMyRequestsInvalidEId() throws Exception {
		LoginTest.loginTest();
		test=extent.createTest("TrackMyRequestsInvalidEId", "verify Login");
		 Commons.click(test,driver, By.id("uinservices/trackservicerequest"));
		 Commons.enter(test,driver, By.id("appIdValue"), data+"345");
		 Commons.click(test,driver, By.id("getEIDStatusbtn"));
		 test.log(Status.INFO, "Click on Track");
		 Commons.click(test,driver, By.id("dismissBtn"));
	}
	
	public void TrackMyRequestsWithDiffEId() throws Exception {
		String tempEID=ConfigManager.gettempEID();
		LoginTest.loginTest();
		test=extent.createTest("TrackMyRequestsInvalidEId", "verify Login");
		 Commons.click(test,driver, By.id("uinservices/trackservicerequest"));
		 Commons.enter(test,driver, By.id("appIdValue"), tempEID);
		 Commons.click(test,driver, By.id("getEIDStatusbtn"));
		 test.log(Status.INFO, "Click on Track");
		 Commons.click(test,driver, By.id("dismissBtn"));
	}
}
