package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import utilities.ExcelReader;

public class TestBase {

	public static ThreadLocal<RemoteWebDriver> dr = new ThreadLocal<RemoteWebDriver>();
	public static RemoteWebDriver driver = null;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static ExcelReader excel = new ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	
	@BeforeSuite
	public void setUp() {
		File filePath = new File(System.getProperty("user.dir"), "\\src\\test\\resources\\properties");
		File configFile = new File(filePath, "config.properties");
		File ORFile = new File(filePath, "OR.properties");
		if (driver==null) {
			try {
				fis = new FileInputStream(configFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				config.load(fis);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				fis = new FileInputStream(ORFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static WebDriver getDriver() {
		return dr.get();
	}
	
	public static void setDriver(RemoteWebDriver driver) {
		dr.set(driver);
	}
	public void startBrowser(String browser) throws MalformedURLException {
		// 浏览器选项设置
		DesiredCapabilities cap = null;
		if (browser.equals("chrome")) {
			cap = DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");
			cap.setPlatform(Platform.ANY);
		}else if (browser.equals("firefox")) {
			cap = DesiredCapabilities.firefox();
			cap.setBrowserName("firefox");
			cap.setPlatform(Platform.ANY);
		}else if (browser.equals("edge")) {
			cap = DesiredCapabilities.edge();
			cap.setBrowserName("edge");
			cap.setPlatform(Platform.WIN10);
		}
		driver = new RemoteWebDriver(new URL("http://192.168.99.101:4444/wd/hub"), cap);
		setDriver(driver);
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	public void getUrl(String url) {
		// 获取网址
		getDriver().get(config.getProperty(url));
	}
	public void clear(String locator) {
		// 执行清空操作
		if (locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).clear();
		}else if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).clear();
		}
	}
	public void click(String locator) {
		// 执行点击操作
		if (locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).click();
		}else if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).click();
		}
	}
	public void sendKeys(String locator, String value) {
		// 发送字符到目标元素
		if (locator.endsWith("_XPATH")) {
			getDriver().findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		}else if (locator.endsWith("_CSS")) {
			getDriver().findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		}
	}
	public void isTitleEqual(String expected) {
		// 断言标题与预期的完全相符
		String actual = getDriver().getTitle();
		try {
			Assert.assertEquals(actual, expected);
		} catch (AssertionError e) {
			e.printStackTrace();
		}
	}
	@AfterMethod
	public void tearDown() {
		// 方法内的步骤执行完成后，关闭浏览器
		if (driver!=null) {
			driver.quit();
		}
	}
}





















