package io.mosip.testrig.residentui.utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
	public static ExtentSparkReporter html;
	public static	ExtentReports extent;
	//public static WebDriver driver;
	public static String Filepath;
	public static	ExtentTest test;
	public static ExtentReports getReports() {
		if(extent==null) {
			extent=new ExtentReports();
			extent=new ExtentReports();
			 Filepath=System.getProperty("user.dir")+"/Reports/"+"resident"+Commons.getDateTime()+".html";
			html=new ExtentSparkReporter(Filepath);
			  extent.attachReporter(html);
		}
		
		return extent;
		
	}
}
