package io.mosip.testrig.residentui.utility;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.Assert;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.mosip.testrig.residentui.testcase.LoginTest;

public class Commons extends BaseClass{
	private static final Logger logger = Logger.getLogger(Commons.class);

	public static String appendDate="0"+getDateTime();
	
	public static String getDateTime()
	  {
		
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	   LocalDateTime now = LocalDateTime.now();
	   return dtf.format(now);
	  }
	
	public  static void filter(ExtentTest test,WebDriver driver, By by,String data) {
		logger.info("Inside Filter " + by + data);
		try {
			Commons.click(test,driver, By.id("Filter"));
 
	
		Commons.enter(test,driver, by, data); 
		Commons.click(test,driver, By.id("applyTxt")); 
		}
		catch (Exception e) {
			try {
				test.fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", driver.findElement(by));

		}
	}
	
	public  static void filterCenter(ExtentTest test,WebDriver driver, By by,String data) {
		logger.info("Inside filterCenter " + by + data);
		try {
		click(test,driver, By.id("Filter")); 
	
		Commons.dropdowncenter(test,driver, by, data); 
		
		Commons.click(test,driver, By.id("applyTxt")); 
		}
		catch (Exception e) {
			try {
				test.fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", driver.findElement(by));

		}
	}
	
  
	public  static void click(ExtentTest test,WebDriver driver, By by) throws IOException, InterruptedException {
		logger.info("Clicking " + by );
		
		try {
			(new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(by));
			Thread.sleep(1000);
			driver.findElement(by).click();
			Thread.sleep(500);
		}catch (StaleElementReferenceException sere) {
			Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
			// simply retry finding the element in the refreshed DOM
			driver.findElement(by).click();
		}
		 catch (Exception e) {
			 Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
				try {
					test.fail(e.getMessage());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));

			}
		}
	public static ExtentTest enter(ExtentTest test,WebDriver driver, By by,int i,String value) throws TimeoutException, IOException {
		//logger.info("Entering " + by +value);
		try {
			(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated(by));
			Thread.sleep(500);
			driver.findElements(by).get(i).sendKeys(value);
			Thread.sleep(500);
			try {
				Thread.sleep(8);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block


				driver.findElements(by).get(i).sendKeys(value);
			}
		}catch (Exception sere) {
			Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");

			// simply retry finding the element in the refreshed DOM

			return test.fail(sere.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
		}
		return test.log(Status.INFO, "Enter  "+value); 
	}
	public  static void clickWebelement(ExtentTest test,WebDriver driver, By by) throws IOException, InterruptedException {
		logger.info("Clicking " + by );
		
		try {
			(new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(by));
			Thread.sleep(1000);
			WebElement checkbox= driver.findElement(by);
		    js.executeScript("arguments[0].click();", checkbox);
			Thread.sleep(500);
		}catch (StaleElementReferenceException sere) {
			// simply retry finding the element in the refreshed DOM
			Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
			driver.findElement(by).click();
		}
		catch (Exception e) {
			try {
				test.fail(e.getMessage());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", driver.findElement(by));

		}

		}
	public static void enter(ExtentTest test,WebDriver driver, By by,String value) throws IOException {
		logger.info("Entering " + by +value);
			try {
				(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated(by));
				driver.findElement(by).clear();
				driver.findElement(by).sendKeys(value);
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch (StaleElementReferenceException sere) {
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");

				// simply retry finding the element in the refreshed DOM
				driver.findElement(by).sendKeys(value);
			}
			catch (TimeoutException toe) {
				test.pass( MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='500'/></p>");
				driver.findElement(by).sendKeys(value);
				System.out.println( "Element identified by " + by.toString() + " was not clickable after 20 seconds");
			}
			catch (Exception e) {
				try {
					test.fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));

			}
			
			
	}
	public static void enterdate(ExtentTest test,WebDriver driver, By by,String value) throws IOException {
		logger.info("Entering " + by +value);
			try {
				(new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated(by));
				driver.findElement(by).clear();
				driver.findElement(by).sendKeys(value);
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch (StaleElementReferenceException sere) {
				Reporter.log("<p><img src='data:image/png;base64," + Screenshot.ClickScreenshot(driver) + "' width='900' height='450'/></p>");

				// simply retry finding the element in the refreshed DOM
				driver.findElement(by).sendKeys(value);
			}
			catch (TimeoutException toe) {
				driver.findElement(by).sendKeys(value);
				System.out.println( "Element identified by " + by.toString() + " was not clickable after 20 seconds");
			}
			
			catch (Exception e) {
				try {
					test.fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));

			}
	}
	
	public static void dropdown(ExtentTest test,WebDriver driver, By by)
	  {
		logger.info("Selecting DropDown Index Zero Value " + by );
		  
		 try {
			 Thread.sleep(500);
			 click(test,driver,by);//REGION
				Thread.sleep(500);
			
		   String att= driver.findElement(by).getAttribute("aria-owns");
		   String[] list=att.split(" ");
		    click(test,driver,By.id(list[0]));
		    try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }catch(Exception e)
		 
		 {
			 test.fail(e.getMessage());
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));

		 }
		 
		 
		 
	  }
	public static void dropdown(WebDriver driver, By by,By value)
	  {
		logger.info("Selecting DropDown By Value " + by +value );
		 try {  
			 Thread.sleep(500);
			 click(driver,by);
			 Thread.sleep(500);
		    click(driver,value);
				Thread.sleep(500);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 }
	public  static void click(WebDriver driver, By by) throws IOException, InterruptedException {
		logger.info("Clicking " + by );
		try {
			(new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(by));
			Thread.sleep(500);
			driver.findElement(by).click();
			Thread.sleep(500);
		} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
	public static void dropdown(ExtentTest test,WebDriver driver, By by,String value)
	  {
		logger.info("Selecting DropDown By Value " + by +value );
		  
		 try {
			 Thread.sleep(500);
			 click(test,driver,by);
				Thread.sleep(500);
			   String val="'"+value +"'";
		   
		    click(test,driver,By.xpath("//span[contains(text(),"+val+")]"));
		    try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }catch(Exception e)
		 
		 {
			 try {
					test.fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));

		 }
	  }
	
	public static void dropdowncenter(ExtentTest test,WebDriver driver, By by,String value)
	  {
		logger.info("Selecting DropDown By Value " + by +value );
		  
		 try {
			 Thread.sleep(500);
			 click(test,driver,by);
				Thread.sleep(500);
			   String val="'"+value +"'";
		   
		    click(test,driver,By.id(value));
		    try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }catch(Exception e)
		 
		 {
			 e.getMessage();
		 }
	  }
	
	public static void dropdown(ExtentTest test,WebDriver driver, By by,By value)
	  {
		logger.info("Selecting DropDown By Value " + by +value );
		 try {  
			 Thread.sleep(500);
			 click(test,driver,by);
			 Thread.sleep(500);
		    click(test,driver,value);
		  
				Thread.sleep(500);
			
		 }catch(Exception e)
		 
		 {
			 try {
					test.fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromBase64String(Screenshot.ClickScreenshot(driver)).build());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", driver.findElement(by));

		 }
	  }
	public static String getTestData()
	{
		return JsonUtil.readJsonFileText("TestData.json");
	}
	public static String getFieldData(String idfield) throws Exception
	{
		return	JsonUtil.JsonObjSimpleParsing(getTestData(), idfield);
	
	}

	public static void clickSpan(WebDriver driver,String key) throws Exception {
		
		String val=Commons.getFieldData(key);
		String var="//span[contains(text(),'"+ val+ "')]";
		  Commons.click(test,driver,By.xpath(var)); 
		  logger.info("clickSpan" + var );
	}






	
	public static boolean switchToFrameByIndex(ExtentTest test,WebDriver driver,int index) {
		boolean flag = false;
		try {
		new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe")));
		driver.switchTo().frame(index);
		flag = true;
		return true;
		} catch (Exception e) {

		return false;
		} finally {
		if (flag) {
		System.out.println("Frame with index \"" + index + "\" is selected");
		} else {
		System.out.println("Frame with index \"" + index + "\" is not selected");
		}
		}
		}
	
	public static boolean switchToFrameByName(WebDriver driver,String nameValue) {

		boolean flag = false;
		try {
		driver.switchTo().frame(nameValue);

		flag = true;
		return true;
		} catch (Exception e) {

		return false;
		} finally {
		if (flag) {
		System.out.println("Frame with Name \"" + nameValue + "\" is selected");
		} else if (!flag) {
		System.out.println("Frame with Name \"" + nameValue + "\" is not selected");
		}
		}
		}
	
	public static void implicitWait(WebDriver driver, int timeOut) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);}
	
	public static void explicitWait(WebDriver driver, WebElement element, int timeOut ) {
		WebDriverWait wait = new WebDriverWait(driver,timeOut);
		wait.until(ExpectedConditions.visibilityOf(element));}
	
	public static boolean switchToNewWindow(WebDriver driver) {
		boolean flag = false;
		try {

			Set<String> s=driver.getWindowHandles();
			Object popup[]=s.toArray();
			driver.switchTo().window(popup[1].toString());
			flag = true;
			return flag;
		} catch (Exception e) {
			flag = false;
			return flag;
		} finally {
			if (flag) {
				System.out.println("Window is Navigated with title");				
			} else {
				System.out.println("The Window with title: is not Selected");
			}
		}
	}
	
	public static boolean switchToOldWindow(WebDriver driver) {
		boolean flag = false;
		try {

			Set<String> s=driver.getWindowHandles();
			Object popup[]=s.toArray();
			driver.switchTo().window(popup[0].toString());
			flag = true;
			return flag;
		} catch (Exception e) {
			flag = false;
			return flag;
		} finally {
			if (flag) {
				System.out.println("Focus navigated to the window with title");			
			} else {
				System.out.println("The Window with title: is not Selected");
			}
		}
	}
	public static boolean isDisplayed(WebDriver driver ,By by) {
		try {
			(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(by));
			return driver.findElement(by).isDisplayed();
		}catch(Exception e) {
			return false;
		}
	}

	
	public static boolean switchWindowByTitle(WebDriver driver,String windowTitle, int count) {
		boolean flag = false;
		try {
			Set<String> windowList = driver.getWindowHandles();

			String[] array = windowList.toArray(new String[0]);

			driver.switchTo().window(array[count-1]);

			if (driver.getTitle().contains(windowTitle)){
				flag = true;
			}else{
				flag = false;
			}
			return flag;
		} catch (Exception e) {
			//flag = true;
			return false;
		} finally {
			if (flag) {
				System.out.println("Navigated to the window with title");
			} else {
				System.out.println("The Window with title is not Selected");
			}
		}
	}
	
	
	
}
