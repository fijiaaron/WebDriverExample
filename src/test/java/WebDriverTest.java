import org.junit.Test;

public class WebDriverTest extends WebDriverTestBase
{
	@Test
	public void openBrowser()
	{
		driver.get("https://quora.com");
	}
}
