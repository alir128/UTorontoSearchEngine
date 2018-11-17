package com.example.tests;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class ProfileChangeInfoTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.chrome.driver", "src/test/java/com/example/tests/chromedriver");
	driver = new ChromeDriver();
    baseUrl = "https://www.katalon.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testProfileChangeInfo() throws Exception {
	driver.get("http://localhost:8080/login");
	driver.findElement(By.id("emaillogin")).click();
	driver.findElement(By.id("emaillogin")).clear();
	driver.findElement(By.id("emaillogin")).sendKeys("sidi.he@mail.utoronto.ca");
	driver.findElement(By.id("passwordlogin")).click();
	driver.findElement(By.id("passwordlogin")).clear();
	driver.findElement(By.id("passwordlogin")).sendKeys("1234");
	driver.findElement(By.id("login-submit")).click();
	driver.findElement(By.linkText("SIDI HE")).click();
    driver.get("http://localhost:8080/profile");
    driver.findElement(By.name("FirstName")).click();
    driver.findElement(By.name("FirstName")).clear();
    driver.findElement(By.name("FirstName")).sendKeys("SIDI");
    driver.findElement(By.name("LastName")).click();
    driver.findElement(By.name("LastName")).clear();
    driver.findElement(By.name("LastName")).sendKeys("HE");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Groups:'])[1]/following::span[1]")).click();
    driver.findElement(By.name("password")).click();
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("1234");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Password:'])[1]/following::input[2]")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
