import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class WebDriverTestBase
{
	Logger log;
	Properties properties;
	WebDriver driver;
	String browser = "CHROME"; // default

	static final Set<String> AvailableBrowsers = new HashSet<>(
			Arrays.asList("CHROME", "FIREFOX", "EDGE", "SAFARI"));

	@Before
	public void setup() throws Exception
	{
		log = Logger.getLogger(this.getClass().getName());
		properties = loadProperties("test.properties");
		browser = getBrowser();
		driver = createWebDriver(browser);
	}

	@After
	public void cleanup()
	{
		if (driver != null)
		{
			driver.quit();
		}
	}

	public Properties loadProperties(String s) throws IOException
	{
		Properties properties = new Properties();
		InputStream propsFile = this.getClass().getResourceAsStream("test.properties");
		try {
			properties.load(propsFile);
		}
		catch (IOException e)
		{
			System.out.println("Warning: could not load properties from file");
		}

		return properties;
	}

	public String getBrowser()
	{
		String DEFAULT_BROWSER = browser;

		String BROWSER_FROM_SYSTEM_PROPERTIES = System.getProperty("BROWSER");
		log.info("BROWSER_FROM_SYSTEM_PROPERTIES: " + BROWSER_FROM_SYSTEM_PROPERTIES);

		String BROWSER_FROM_ENVIRONMENT_VARIABLE = System.getenv("BROWSER");
		log.info("BROWSER_FROM_ENVIRONMENT_VARIABLE: " + BROWSER_FROM_ENVIRONMENT_VARIABLE);

		String BROWSER_FROM_PROPERTIES_FILE = properties.getProperty("BROWSER");
		log.info("BROWSER_FROM_PROPERTIES_FILE: " + BROWSER_FROM_PROPERTIES_FILE);

		// order of preference for choosing browser
		List<String> POTENTIAL_BROWSERS = Arrays.asList(
				BROWSER_FROM_SYSTEM_PROPERTIES,
				BROWSER_FROM_ENVIRONMENT_VARIABLE,
				BROWSER_FROM_PROPERTIES_FILE
		);

		browser = POTENTIAL_BROWSERS.stream()
				.filter(BROWSER ->
						BROWSER != null && AvailableBrowsers.contains(BROWSER.toUpperCase())
				)
				.findFirst()
				.orElse(DEFAULT_BROWSER);

		log.info("Browser: " + browser);
		return browser;
	}

	public  WebDriver createWebDriver(String browser) throws Exception
	{
		switch(browser.toUpperCase())
		{
			case "CHROME":
				return new ChromeDriver();
			case "FIREFOX":
				return new FirefoxDriver();
			case "EDGE":
				return new EdgeDriver();
			case "SAFARI":
				return new SafariDriver();
			default:
				throw new Exception("unknown browser: " + browser);
		}
	}
}
