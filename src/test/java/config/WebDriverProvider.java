package config;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;

public class WebDriverProvider {
    static WebDriverConfig config = ConfigFactory.create(WebDriverConfig.class, System.getProperties());

    public static void configure() {

        Configuration.baseUrl = WebDriverProvider.config.getBaseUrl();
        Configuration.browser = WebDriverProvider.config.getBrowser();
        Configuration.browserVersion = WebDriverProvider.config.getBrowserVersion();
        Configuration.browserSize = WebDriverProvider.config.browserSize();
        RestAssured.baseURI = WebDriverProvider.config.getBaseUrl();

        String remoteUrl = WebDriverProvider.config.getRemoteWebDriver();
        if (remoteUrl != null) {
            Configuration.remote = remoteUrl;
        }
    }
}
