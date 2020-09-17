package config;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;
import static script.driver.*;

public class ActionKeyword {

    public static WebDriver driver;

    public static void openBrowser(String object, String data) {
        try {


            String exePath = Constant.ChromeDriver_Path;
            System.setProperty("webdriver.chrome.driver", exePath);
            //Log.info("Opening Browser");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }catch (Exception e){
//            Log.info("Not able to open Browser --- " + e.getMessage());
//            Driver.bResult=false;
        }
    }

    public static void navigate(String object, String data) {
        try {


            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
           // Log.info("Navigate to URL");
            driver.get(data);
        }catch (Exception e){
//            Log.info("Not able to Navigate --- " + e.getMessage());
//            Driver.bResult=false;
        }
    }

    public static void click(String object, String data) {
        try {


           // Log.info("Clickin on webelement" + object);
            driver.findElement(By.xpath(OR.getProperty(object))).click();
        }catch (Exception e){
//            Log.info("Not able to Click --- " + e.getMessage());
//            Driver.bResult=false;
        }
    }

    public static void input(String object, String data) {
        try {


            //Log.info("Entering data in field" + object);
            driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
        }catch (Exception e){
//            Log.info("Not able to input --- " + e.getMessage());
//            Driver.bResult=false;
        }
    }





    public static void wait(String object, String data) throws Exception {
        try {


           // Log.info("Waiting "+ object);
            Thread.sleep(Constant.wait);
        }catch (Exception e){
//            Log.info("wait not work --- " + e.getMessage());
//            Driver.bResult=false;
        }
    }

    public static void closeBrowser(String object, String data) {
        try {

            //Log.info("Closing browser" + object);
            driver.quit();
        }catch (Exception e){
//            Log.info("Not able to close Browser --- " + e.getMessage());
//            Driver.bResult=false;
        }


    }

    public static void validateTitle(String object, String data) {
        try {


            //Log.info("Entering data in field" + object);
            String ActualTitle= driver.getTitle();
            String ExpectedTitle =data;
            //Assert.assertEquals(ExpectedTitle,ActualTitle);


        }catch (Exception e){
//            Log.info("Not able to input --- " + e.getMessage());
//            Driver.bResult=false;
        }
    }




}
