package testcases;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

import base.TestBase;
import utilities.TestUtil;

public class TestBaiduSearch extends TestBase{

	@Test(dataProviderClass=TestUtil.class, dataProvider="dp")
	public void testBaiduSearch(String browser, String keyword, String title) throws MalformedURLException, InterruptedException {
		startBrowser(browser);
		getUrl("testUrl");
		clear("searchBox_XPATH");
		sendKeys("searchBox_XPATH", keyword);
		click("submitBtn_CSS");
		
		TimeUnit.SECONDS.sleep(1);
		isTitleEqual(title);
		
		System.out.println("≤‚ ‘»´≤øΩ· ¯°£");
		
	}
}
