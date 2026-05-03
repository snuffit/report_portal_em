package browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class BrowserSessionFactory {

    private static final String BROWSER_PROPERTY = "browser";

    private BrowserSessionFactory() {
    }

    public static WebDriver createSession() {
        String browserName = System.getProperty(BROWSER_PROPERTY, "chrome").toLowerCase();
        return switch (browserName) {
            case "firefox" -> createFirefoxSession();
            case "chrome" -> createChromeSession();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };
    }

    private static WebDriver createChromeSession() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxSession() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        options.addArguments("-private");
        return new FirefoxDriver(options);
    }
}
