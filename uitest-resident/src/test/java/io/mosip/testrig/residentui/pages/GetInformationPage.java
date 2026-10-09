package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.reporting.UiExtentReportManager;
import io.mosip.testrig.residentui.config.ResidentUtil;
import org.junit.jupiter.api.Assertions;

public class GetInformationPage extends BasePage {

    private int succeededDownloads;

    public GetInformationPage(Page page) {
        super(page);
    }

    public void navigateTo() {
        click("#getInformation", "Open the Get Information service");
        waitForNetworkIdle();
    }

    public void searchAndDownloadAllHierarchyLevels() {
        int totalLevels = ResidentUtil.getHierarchyNumbers();
        succeededDownloads = 0;
        for (int i = 0; i < totalLevels; i++) {
            selectLocationType(i);
            fill("#searchLocationBox", ResidentUtil.getHierarchyName(i), "Enter the location name in the search box");
            click("#regCenterSearchBtn", "Click the registration center 'Search' button");
            waitForNetworkIdle();
            Download download = assertions().awaitDownload(
                    () -> click("#regCentersDownloadBtn", "Click the registration centers 'Download' button"));
            Assertions.assertNull(download.failure(),
                    "Download failed for hierarchy level " + i + ": " + download.failure());
            succeededDownloads++;
        }
    }

    private void selectLocationType(int levelIndex) {
        click("#locationType", "Open the location type dropdown");
        click("#hierarchyLevel" + levelIndex, "Select location hierarchy level " + levelIndex);
    }

    public void assertAllDownloadsSucceeded() {
        int expectedTotal = ResidentUtil.getHierarchyNumbers();
        Assertions.assertEquals(expectedTotal, succeededDownloads,
                "Expected " + expectedTotal + " successful downloads but got " + succeededDownloads);
        UiExtentReportManager.logPass("Verified all " + expectedTotal
                + " registration center hierarchy levels downloaded successfully");
    }
}
