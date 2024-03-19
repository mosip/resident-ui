package io.mosip.testrig.residentui.testcase;

import java.io.IOException;


import org.openqa.selenium.By;
import org.testng.annotations.Test;


import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.utility.Commons;

import io.mosip.testrig.residentui.utility.ResidentBaseClass;

@Test(groups = "GI")
public class GetInformation extends ResidentBaseClass {
	String postalcode =BaseTestCase.GethierarchyName(5);
	@Test(groups = "GI")
	public void GetInformation() throws Exception {
		String hierarchyLevel0 = BaseTestCase.GethierarchyName(0); //Country
		String hierarchyLevel1 = BaseTestCase.GethierarchyName(1); //Region
		String hierarchyLevel2 = BaseTestCase.GethierarchyName(2);//Province
		String hierarchyLevel3 = BaseTestCase.GethierarchyName(3);//City
		String hierarchyLevel4 = BaseTestCase.GethierarchyName(4);//Zone
		

		test = extent.createTest("GetInformation test", "verify Login");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("getInformation"));
		test = extent.createTest("Get Information of country", "verify country");
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel0")); //get it fix 
		Commons.enter(test, driver, By.id("searchLocationBox"), hierarchyLevel0);// take from testdata
		Thread.sleep(1000);
		Commons.click(test, driver, By.id("regCenterSearchBtn"));//get it fix 
		Commons.click(test, driver, By.id("regCentersDownloadBtn"));//get it fix 
		// region
		test = extent.createTest("Get Information of region", "verify region");
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel1"));
		Commons.enter(test, driver, By.id("searchLocationBox"), hierarchyLevel1);
		Thread.sleep(1000);
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.id("regCentersDownloadBtn"));
		// province
		test = extent.createTest("Get Information of province", "verify province");
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel2"));
		Commons.enter(test, driver, By.id("searchLocationBox"), hierarchyLevel2);
		Thread.sleep(1000);
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.id("regCentersDownloadBtn"));
		// City
		test = extent.createTest("Get Information of city", "verify city");
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel3"));
		Commons.enter(test, driver, By.id("searchLocationBox"), hierarchyLevel3);
		Thread.sleep(1000);
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
		//Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.id("regCentersDownloadBtn"));
		// Zone
		test = extent.createTest("Get Information of zone", "verify zone");
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel4"));
		Commons.enter(test, driver, By.id("searchLocationBox"), hierarchyLevel4);
		Thread.sleep(1000);
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.id("regCentersDownloadBtn"));
		// Postal Code
		test = extent.createTest("Get Information of postalcode", "verify postalcode");
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel5"));
		Commons.enter(test, driver, By.id("searchLocationBox"), postalcode);
		Thread.sleep(1000);
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[8]"));
		Commons.click(test, driver, By.id("regCentersDownloadBtn"));
		// Commons.enter(driver, By.className("search-input ng-pristine ng-valid
		// ng-touched"), "CST");
		// driver.findElement(By.xpath("//input[@type='search']")).sendKeys("CST");

	}

	public void GetInformationWithInvalidcountry() throws Exception {
		
		test = extent.createTest("Get Information With Invalid country", "verify country");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("getInformation"));
//		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel0"));
		Commons.enter(test, driver, By.id("searchLocationBox"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	}

	public void GetInformationWithInvalidProvince() throws Exception {
	
		test = extent.createTest("Get Information With Invalid Province", "verify Province");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("getInformation"));
	//	Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel1"));
		Commons.enter(test, driver, By.id("searchLocationBox"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	}

	public void GetInformationWithInvalidRegion() throws Exception {
	
		test = extent.createTest("Get Information With Invalid Region", "verify Region");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("getInformation"));
	//	Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel2"));
		Commons.enter(test, driver, By.id("searchLocationBox"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	}

	public void GetInformationWithInvalidCity() throws Exception {
		
		test = extent.createTest("GetI nformation With InvalidCity", "verify City");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("getInformation"));
	//	Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel3"));
		Commons.enter(test, driver, By.id("searchLocationBox"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	}

	public void GetInformationWithInvalidZone() throws Exception {
		
		test = extent.createTest("Get Information With Invalid Zone", "verify Zone");
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("getInformation"));
//		Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel4"));
		Commons.enter(test, driver, By.id("searchLocationBox"), postalcode);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	}

	public void GetInformationWithInvalidPostalCode() throws IOException, InterruptedException {
		Thread.sleep(2000);
		Commons.click(test, driver, By.id("getInformation"));
		test = extent.createTest("Get Information With Invalid PostalCode", "verify PostalCode");
	//	Commons.click(test, driver, By.id("mat-tab-label-0-0"));
		Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel5"));
		Commons.enter(test, driver, By.id("searchLocationBox"), data);
	//	Commons.click(test, driver, By.xpath("(//span[@class='mat-button-wrapper'])[7]"));
		Commons.click(test, driver, By.id("regCenterSearchBtn"));
	}
}
