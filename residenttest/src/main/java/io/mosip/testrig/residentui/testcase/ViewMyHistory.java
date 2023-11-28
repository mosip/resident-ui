package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import java.util.Date;

import ch.qos.logback.core.util.Duration;
import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;

public class ViewMyHistory extends BaseClass {
	
	@Test(groups = "VMH")
	public void viewMyHistory() throws Exception {
		LoginTest.loginTest();
		String date = "12/04/2022";
		
		
		test=extent.createTest("ViewMyHistory Test ", "verify Login");
		Commons.click(test,driver, By.className("mat-card-header-text"));
		
		Commons.click(test,driver, By.id("fromPickertext"));
		Commons.click(test,driver, By.xpath("//*[@id=\"mat-datepicker-0\"]/div/mat-month-view/table/tbody/tr[5]/td[4]"));
		Commons.click(test,driver, By.id("searchbtn"));
		test.log(Status.INFO, "date");
	
		Commons.click(test,driver, By.id("serviceType"));
		Commons.click(test,driver, By.id("ALL"));
		Commons.click(test,driver, By.id("searchbtn"));
		Commons.click(test,driver, By.id("serviceType"));
		
		Commons.click(test,driver, By.id("ALL"));
		
		Commons.click(test,driver, By.id("AUTHENTICATION_REQUEST"));
		Commons.click(test, driver, By.xpath("//h3[text()='View My History']"));
		Commons.click(test,driver, By.id("searchbtn"));
	    Commons.click(test,driver, By.id("serviceType"));
	    Commons.click(test,driver, By.id("AUTHENTICATION_REQUEST"));
		Commons.click(test,driver, By.id("SERVICE_REQUEST"));
		Commons.click(test, driver, By.xpath("//h3[text()='View My History']"));
		Commons.click(test,driver, By.id("searchbtn"));
		Commons.click(test,driver, By.id("serviceType"));
		Commons.click(test,driver, By.id("SERVICE_REQUEST"));
		Commons.click(test,driver, By.id("DATA_UPDATE_REQUEST"));
		Commons.click(test,driver, By.id("searchbtn"));
		Commons.click(test,driver, By.id("DATA_UPDATE_REQUEST"));
		Commons.click(test,driver, By.id("ID_MANAGEMENT_REQUEST"));
		Commons.click(test,driver, By.id("searchbtn"));
		Commons.click(test,driver, By.id("ID_MANAGEMENT_REQUEST"));
		Commons.click(test,driver, By.id("DATA_SHARE_REQUEST"));
		Commons.click(test,driver, By.id("searchbtn"));
		Commons.click(test,driver, By.id("DATA_SHARE_REQUEST"));
		Commons.click(test,driver, By.id("ALL"));
		 test.log(Status.INFO, "Select ServiceType");
		Commons.click(test,driver, By.id("statusFilter"));
		Commons.click(test,driver, By.xpath("//span[text()=' Success']"));
		Commons.click(test,driver, By.id("searchbtn"));
		Commons.click(test,driver, By.id("Success"));
		Commons.click(test,driver, By.id("In Progress"));
		Commons.click(test,driver, By.id("searchbtn"));
		Commons.click(test,driver, By.id("In Progress"));
		Commons.click(test,driver, By.id("Failed"));
		Commons.click(test,driver, By.id("searchbtn"));
		test.log(Status.INFO, "Select Status");
		Commons.click(test,driver, By.id("downloadbtn"));
			
	}
}
