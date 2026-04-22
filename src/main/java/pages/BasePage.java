package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BasePage {

    private static final By BODY = By.tagName("body");
    private static final String CLICKABLE_BY_TEXT_XPATH =
            "//*[self::button or @role='button' or self::span or self::a or self::div]"
                    + "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'%s')]";

    protected By byClickableText(String text) {
        return By.xpath(String.format(CLICKABLE_BY_TEXT_XPATH, text.toLowerCase()));
    }

    protected SelenideElement visibleElementByClickableText(String text) {
        return $$(byClickableText(text)).findBy(Condition.visible);
    }

    protected void clickElementByText(String text) {
        visibleElementByClickableText(text).click();
    }

    protected void clickElementIfVisible(String text) {
        ElementsCollection visibleCandidates = $$(byClickableText(text)).filter(Condition.visible);
        if (!visibleCandidates.isEmpty()) {
            visibleCandidates.first().click();
        }
    }

    protected boolean isBodyContainsText(String text) {
        return $(BODY).has(Condition.text(text));
    }

    protected boolean isBodyMatchesPattern(String regex) {
        return $(BODY).has(Condition.matchText(regex));
    }

    protected void shouldSeeTextInBody(String text) {
        $(BODY).shouldHave(Condition.text(text));
    }

    protected void shouldSeeBodyByPattern(String regex) {
        $(BODY).shouldHave(Condition.matchText(regex));
    }
}