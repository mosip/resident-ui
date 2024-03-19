package io.mosip.testrig.residentui.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.mosip.testrig.residentui.kernel.util.ConfigManager;
import io.mosip.testrig.residentui.kernel.util.S3Adapter;
import io.mosip.testrig.residentui.service.BaseTestCase;
import io.mosip.testrig.residentui.testcase.LoginTest;

public class ResidentBaseClass {

	private static final Logger logger = Logger.getLogger(BaseClass.class);
	protected static WebDriver driver;
	protected Map<String, Object> vars;
	protected JavascriptExecutor js;
	protected String langcode;
	protected String env = ConfigManager.getiam_apienvuser();
	protected String envPath = ConfigManager.getiam_residentportal_path();
	protected String vid = System.getProperty("vid");
	protected String externalurl = System.getProperty("externalurl");
	protected String password = System.getProperty("password");
	protected String data = Commons.appendDate;
	public static ExtentSparkReporter html;
	public static ExtentReports extent;
	public static ExtentTest test;

	public void setLangcode(String langcode) throws Exception {
		this.langcode = Commons.getFieldData("langcode");

	}

	@BeforeMethod

	public void set() {
		extent = ExtentReportManager.getReports();

	}

	@BeforeMethod
	public void setUp() throws Exception {
		test = extent.createTest(env, getCommitId());
		if(System.getProperty("os.name").equalsIgnoreCase("Linux") && ConfigManager.getDocker().equals("yes") ) {
			
			
			logger.info("Docker start");
			String configFilePath ="/usr/bin/chromedriver";
			System.setProperty("webdriver.chrome.driver", configFilePath);
		
	}else {
		WebDriverManager.chromedriver().setup();
		logger.info("window chrome driver start");
	}
		ChromeOptions options = new ChromeOptions();
		String headless=ConfigManager.getHeadless();
		if(headless.equalsIgnoreCase("yes")) {
			logger.info("Running is headless mode");
			options.addArguments("--headless", "--disable-gpu","--no-sandbox", "--window-size=1920x1080","--disable-dev-shm-usage");
			

		}
		
		driver = new ChromeDriver(options);
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
		driver.get(envPath);
		logger.info("launch url --"+envPath);
		Thread.sleep(500);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		String language=ConfigManager.getloginlang();
		try {
			if(!language.equals("sin")) {
			Commons.dropdown( driver, By.id("languages"), By.id("lang"+language));
			}
		}
		catch (Exception e) {
		e.printStackTrace();
		}

	}

	@AfterMethod
	public void tearDown() {
		MockSMTPListener mockSMTPListener = new MockSMTPListener();
		mockSMTPListener.bTerminate = true;
		driver.quit();
		extent.flush();
	}

	@AfterSuite
	public void pushFileToS3() {

		if (ConfigManager.getPushReportsToS3().equalsIgnoreCase("yes")) {
			// EXTENT REPORT

			File repotFile = new File(ExtentReportManager.Filepath);
			System.out.println("reportFile is::" + repotFile);
			String reportname = repotFile.getName();

			S3Adapter s3Adapter = new S3Adapter();
			boolean isStoreSuccess = false;
			try {
				isStoreSuccess = s3Adapter.putObject(ConfigManager.getS3Account(), BaseTestCase.testLevel, null,
						"ResidentUi", env + BaseTestCase.currentModule + data + ".html", repotFile);

				System.out.println("isStoreSuccess:: " + isStoreSuccess);
			} catch (Exception e) {
				System.out.println("error occured while pushing the object" + e.getLocalizedMessage());
				e.printStackTrace();
			}
			if (isStoreSuccess) {
				System.out.println("Pushed file to S3");
			} else {
				System.out.println("Failed while pushing file to S3");
			}
		}

	}

	@DataProvider(name = "data-provider")
	public Object[] dpMethod() {
		String listFilename[] = readFolderJsonList();
		String s[][] = null;
		String temp[] = null;
		for (int count = 0; count < listFilename.length; count++) {
			listFilename[count] = listFilename[count].replace(".csv", "");

		}

		return listFilename;
	}

	public static String[] readFolderJsonList() {
		String contents[] = null;
		try {
			String langcode = JsonUtil.JsonObjParsing(Commons.getTestData(), "loginlang");

			File directoryPath = new File(System.getProperty("user.dir") + "\\BulkUploadFiles\\" + langcode + "\\");

			if (directoryPath.exists()) {

				contents = directoryPath.list();
				System.out.println("List of files and directories in the specified directory:");
				for (int i = 0; i < contents.length; i++) {
					System.out.println(contents[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contents;
	}

	private String getCommitId() {
		Properties properties = new Properties();
		try (InputStream is = ExtentReportManager.class.getClassLoader().getResourceAsStream("git.properties")) {
			properties.load(is);

			return "Commit Id is: " + properties.getProperty("git.commit.id.abbrev") + " & Branch Name is:"
					+ properties.getProperty("git.branch");

		} catch (IOException e) {
			logger.error(e.getStackTrace());
			return "";
		}

	}
}
