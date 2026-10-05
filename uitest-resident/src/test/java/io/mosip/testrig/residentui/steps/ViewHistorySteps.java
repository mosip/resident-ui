package io.mosip.testrig.residentui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.mosip.testrig.residentui.pages.ViewHistoryPage;
import io.mosip.testrig.uitestrig.browser.PlaywrightFactory;

public class ViewHistorySteps {

    private ViewHistoryPage page() {
        return new ViewHistoryPage(PlaywrightFactory.getPage());
    }

    @When("the user navigates to View My History")
    public void navigateToViewHistory() {
        page().navigateTo();
    }

    @When("the user sets a from date and searches")
    public void setFromDateAndSearch() {
        page().openDatePicker();
        page().selectDateInCalendar(4, 3);
        page().search();
    }

    @When("the user filters by history type {string}")
    public void filterByHistoryType(String type) {
        page().openHistoryTypeFilter();
        page().selectHistoryType(type);
        // Checkbox clicks stop propagation so the mat-menu stays open for multi-select — close it
        // before searching, else its backdrop blocks the search click.
        page().closeFilterPanel();
        page().search();
    }

    @When("the user filters by status {string}")
    public void filterByStatus(String status) {
        page().openStatusFilter();
        page().selectStatus(status);
        // Same mat-menu-stays-open pattern as the history type filter — close before searching.
        page().closeFilterPanel();
        page().search();
    }

    @When("the user downloads the history results")
    public void downloadResults() {
        page().downloadResults();
    }

    @Then("the download button should be visible")
    public void assertDownloadVisible() {
        page().assertDownloadButtonVisible();
    }
}
