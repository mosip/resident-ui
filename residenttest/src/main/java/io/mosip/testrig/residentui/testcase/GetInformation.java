package io.mosip.testrig.residentui.testcase;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.utility.Commons;

import io.mosip.testrig.residentui.utility.ResidentBaseClass;

@Test(groups = "GI")
public class GetInformation extends ResidentBaseClass {

	@Test(groups = "GI")

	public void getInformation() throws Exception {
		int totalHierarchyLevels=BaseTestCase.getHierarchyNumbers();
		Commons.click(test, driver, By.id("getInformation"));
		for(int i=0;i<totalHierarchyLevels;i++) {
			Commons.dropdown(test, driver, By.id("locationType"), By.id("hierarchyLevel"+i));
			Commons.enter(test, driver, By.id("searchLocationBox"),  BaseTestCase.GethierarchyName(i));
			Thread.sleep(1000);
			Commons.click(test, driver, By.id("regCenterSearchBtn"));
			Commons.click(test, driver, By.id("regCentersDownloadBtn"));
		}
	}

}
