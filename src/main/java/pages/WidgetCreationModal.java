package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;

public class WidgetCreationModal extends BasePage {

    // Locators
    private static final By WIDGET_TYPE_ITEMS = By.xpath(
            "//*[contains(@class,'widget-type-item') or contains(@class,'widgetTypeItem') or contains(@class,'widgetItem')]"
    );

    // Texts
    private static final String TEXT_NEXT = "next";
    private static final String TEXT_SAVE = "save";
    private static final String TEXT_ADD = "add";

    public WidgetCreationModal selectFirstWidgetType() {
        return Allure.step("Widget wizard: select first widget type", () -> {
            ElementsCollection widgetTypeItems = $$(WIDGET_TYPE_ITEMS).filter(Condition.visible);
            if (!widgetTypeItems.isEmpty()) {
                widgetTypeItems.first().click();
            }
            return this;
        });
    }

    public WidgetCreationModal clickNext() {
        return Allure.step("Widget wizard: Next", () -> {
            clickElementIfVisible(TEXT_NEXT);
            return this;
        });
    }

    public WidgetCreationModal clickSave() {
        return Allure.step("Widget wizard: Save", () -> {
            clickElementIfVisible(TEXT_SAVE);
            return this;
        });
    }

    public WidgetCreationModal clickAdd() {
        return Allure.step("Widget wizard: Add", () -> {
            clickElementIfVisible(TEXT_ADD);
            return this;
        });
    }

    public DashboardPage createDefaultWidget() {
        return Allure.step("Widget wizard: complete default flow", () -> {
            selectFirstWidgetType()
                    .clickNext()
                    .clickNext()
                    .clickSave()
                    .clickAdd();
            return new DashboardPage();
        });
    }
}