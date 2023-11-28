package io.mosip.testrig.residentui.testcase;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.mosip.testrig.residentui.utility.BaseClass;
import io.mosip.testrig.residentui.utility.Commons;
import io.mosip.testrig.residentui.utility.JsonUtil;
import io.mosip.testrig.residentui.utility.ResidentBaseClass;

@Test(groups = "GI")
public class GetInformation extends ResidentBaseClass {

	@Test(groups = "GI")
	public void getInformation() throws Exception {
		String country = JsonUtil.JsonObjParsing(Commons.getTestData(), "country");
		String region = JsonUtil.JsonObjParsing(Commons.getTestData(), "region");
		String province = JsonUtil.JsonObjParsing(Commons.getTestData(), "province");
		String city = JsonUtil.JsonObjParsing(Commons.getTestData(), "city");
		String zone = JsonUtil.JsonObjParsing(Commons.getTestData(), "zone");
		String postalcode = JsonUtil.JsonObjParsing(Commons.getTestData(), "postalcode");

		test = extent.createTest("GetInformation test", "verify Login");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("dashboardCard2"));
//		Commons.click(test,driver, By.id("downloadAcknowledgementbtn"));

		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		// Commons.dropdown(driver, By.id("mat-select-0"));

	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[6]"));// connect with arvind

//		MyCountry

		test = extent.createTest("Get Information of country", "verify country");
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-0"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), country);// take from testdata
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));// connect with arvind
		//Commons.click(test, driver, By.xpath("//img[@class='save-icon']"));
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
		Commons.click(test, driver, By.xpath("//img[@class='save-icon']"));
		// region
		test = extent.createTest("Get Information of region", "verify region");
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-1"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), region);
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.xpath("//img[@class='save-icon']"));
		// province
		test = extent.createTest("Get Information of province", "verify province");
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-2"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), province);
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.xpath("//img[@class='save-icon']"));
		// City
		test = extent.createTest("Get Information of city", "verify city");
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-3"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), city);
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
		//Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.xpath("//img[@class='save-icon']"));
		// Zone
		test = extent.createTest("Get Information of zone", "verify zone");
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-4"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), zone);
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.xpath("//img[@class='save-icon']"));
		// Postal Code
		test = extent.createTest("Get Information of postalcode", "verify postalcode");
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-5"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), postalcode);
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.xpath("//img[@class='save-icon']"));
		// Commons.enter(driver, By.className("search-input ng-pristine ng-valid
		// ng-touched"), "CST");
		// driver.findElement(By.xpath("//input[@type='search']")).sendKeys("CST");

	}

	public void GetInformationWithInvalidcountry() throws Exception {
		String postalcode = JsonUtil.JsonObjParsing(Commons.getTestData(), "postalcode");
		test = extent.createTest("Get Information With Invalid country", "verify country");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("dashboardCard2"));
		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-0"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	}

	public void GetInformationWithInvalidProvince() throws Exception {
		String postalcode = JsonUtil.JsonObjParsing(Commons.getTestData(), "postalcode");
		test = extent.createTest("Get Information With Invalid Province", "verify Province");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("dashboardCard2"));
		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-1"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	}

	public void GetInformationWithInvalidRegion() throws Exception {
		String postalcode = JsonUtil.JsonObjParsing(Commons.getTestData(), "postalcode");
		test = extent.createTest("Get Information With Invalid Region", "verify Region");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("dashboardCard2"));
		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-2"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	}

	public void GetInformationWithInvalidCity() throws Exception {
		String postalcode = JsonUtil.JsonObjParsing(Commons.getTestData(), "postalcode");
		test = extent.createTest("GetI nformation With InvalidCity", "verify City");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("dashboardCard2"));
		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-3"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	}

	public void GetInformationWithInvalidZone() throws Exception {
		String postalcode = JsonUtil.JsonObjParsing(Commons.getTestData(), "postalcode");
		test = extent.createTest("Get Information With Invalid Zone", "verify Zone");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("dashboardCard2"));
		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-4"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	}

	public void GetInformationWithInvalidPostalCode() throws IOException, InterruptedException {
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("dashboardCard2"));
		test = extent.createTest("Get Information With Invalid PostalCode", "verify PostalCode");
		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("mat-select-0"), By.id("mat-option-5"));
		Commons.enter(test, driver, By.xpath("//input[@id='search']"), data);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.xpath("//button[@class='search-btn mat-raised-button']"));
	}
}
