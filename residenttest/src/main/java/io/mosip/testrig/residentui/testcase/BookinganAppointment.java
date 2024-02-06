package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.JsonUtil;
import io.mosip.testrig.residentui.utility.ResidentBaseClass;

public class BookinganAppointment extends ResidentBaseClass {

	@Test
	public void bookinganAppointment () throws Exception {
		 String externalemail="";
		
		
		
		Commons.click(test,driver, By.id("dashboardCard3"));
		Commons.click(test,driver, By.xpath("(//div[text()=' LOGIN'])[2]"));
		Commons.click(test,driver, By.xpath("//mat-select[@role='listbox']"));
		//Commons.dropdown(driver, By.id("mat-select-0"));
		//Commons.dropdown(driver, By.id("mat-select-0"), "français");
		//Commons.enter(driver, By.id("inputFieldContact"), "Resident_AddIdentity_ValidParam_smoke_Pos@mosip.net");
		
		
		
		
		
	}
}
