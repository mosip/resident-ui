package io.mosip.testrig.residentui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.mosip.testrig.uitestrig.pages.BasePage;
import io.mosip.testrig.uitestrig.assertions.VisualValidator;

import java.util.List;
import java.util.stream.Collectors;

public class ViewHistoryPage extends BasePage {

    private final VisualValidator visual;

    public ViewHistoryPage(Page page) {
        super(page);
        this.visual = new VisualValidator(page);
    }

    public void navigateTo() {
        click("#uinservices\\/viewhistory", "Open the View History service");
        waitForNetworkIdle();
    }

    public void openDatePicker() {
        click("#fromPickertext", "Open the 'From' date picker");
    }

    public void selectDateInCalendar(int weekRow, int dayCol) {
        page.locator("#mat-datepicker-0 div mat-month-view table tbody")
                .locator("tr").nth(weekRow)
                .locator("td").nth(dayCol)
                .click();
    }

    public void search() {
        click("#searchbtn", "Click the 'Search' button");
        waitForNetworkIdle();
    }

    public void openHistoryTypeFilter() {
        click("#historyTypeBtn", "Open the history type filter");
    }

    public void selectHistoryType(String type) {
        selectOnly(type);
    }

    public void openStatusFilter() {
        click("#statusTypeBtn", "Open the status filter");
    }

    public void selectStatus(String status) {
        selectOnly(status);
    }

    /**
     * Checks exactly {@code targetId} in a filter panel, clearing any other leftover checked
     * boxes first. Individual checkboxes only toggle themselves and clear "ALL" — but "ALL" is
     * special: clicking it while checked resets EVERY item to unchecked (not just itself). So
     * "ALL" must be cleared first, on its own, and the target's state re-checked only after that
     * reset has happened — not from a value read before any of this ran.
     */
    private void selectOnly(String targetId) {
        // The filter's option list (and whether it even has an "ALL" entry) is config-driven per
        // environment — don't assume #ALL exists.
        Locator allBox = page.locator("#ALL");
        if (!"ALL".equals(targetId) && allBox.count() > 0 && allBox.isChecked()) {
            allBox.click(); // resets every item, including the target, to unchecked
        }
        // Capture ids as plain strings first — Locator.all() re-resolves by position, and each
        // click here shrinks the live ":checked" set, which would misalign later iterations.
        List<String> checkedIds = page.locator("input.check-box:checked").all().stream()
                .map(box -> box.getAttribute("id"))
                .collect(Collectors.toList());
        for (String id : checkedIds) {
            if (!targetId.equals(id) && !"ALL".equals(id)) {
                page.locator("#" + id).click();
            }
        }
        if (!page.locator("#" + targetId).isChecked()) {
            click("#" + targetId, "Select '" + targetId + "'");
        }
    }

    /**
     * Closes the currently-open mat-menu filter panel. Re-clicking its trigger button doesn't
     * work — the panel's full-viewport backdrop covers the trigger too, so that click just hits
     * the same interception the backdrop causes everywhere else. Escape closes any CDK overlay
     * without needing a clickable target.
     */
    public void closeFilterPanel() {
        page.keyboard().press("Escape");
    }

    public void downloadResults() {
        click("#downloadbtn", "Click the 'Download' button");
        waitForNetworkIdle();
    }

    public void assertDownloadButtonVisible() {
        visual.assertVisible("#downloadbtn");
    }

    public void goBack() {
        page.goBack();
        waitForNetworkIdle();
    }
}
