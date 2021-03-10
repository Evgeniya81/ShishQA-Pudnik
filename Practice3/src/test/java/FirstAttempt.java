import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class FirstAttempt {
    private WebDriver driver;
    private String baseUrl;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "src\\test\\chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "https://sandbox.cardpay.com/MI/cardpayment2.html?orderXml=PE9SREVSIFdBTExFVF9JRD0nODI5OScgT1JERVJfTlVNQkVSPSc0NTgyMTEnIEFNT1VOVD0nMjkxLjg2JyBDVVJSRU5DWT0nRVVSJyAgRU1BSUw9J2N1c3RvbWVyQGV4YW1wbGUuY29tJz4KPEFERFJFU1MgQ09VTlRSWT0nVVNBJyBTVEFURT0nTlknIFpJUD0nMTAwMDEnIENJVFk9J05ZJyBTVFJFRVQ9JzY3NyBTVFJFRVQnIFBIT05FPSc4NzY5OTA5MCcgVFlQRT0nQklMTElORycvPgo8L09SREVSPg==&sha512=998150a2b27484b776a1628bfe7505a9cb430f276dfa35b14315c1c8f03381a90490f6608f0dcff789273e05926cd782e1bb941418a9673f43c47595aa7b8b0d\n";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testFirstTestCase() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("4000000000000036");
        driver.findElement(By.id("input-card-holder")).sendKeys("PUDNIK EVGENIYA");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("07");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2037");
        driver.findElement(By.id("input-card-cvc")).sendKeys("007");
        driver.findElement(By.id("action-submit")).click();


        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "CONFIRMED");
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-cardholder']/div[2]")).getText(), "PUDNIK EVGENIYA");
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-total']/div[2]")).getText(), "EUR   291.86");
    }


    @Test
    public void testSecondTestCase() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("5555555555554444");
        driver.findElement(By.id("input-card-holder")).clear();
        driver.findElement(By.id("input-card-holder")).sendKeys("PUDNIK EVGENIYA");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("03");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2024");
        driver.findElement(By.id("input-card-cvc")).clear();
        driver.findElement(By.id("input-card-cvc")).sendKeys("001");
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("failure")).submit();
        AssertJUnit.assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-cardholder']/div[2]")).getText(), "PUDNIK EVGENIYA");
        driver.findElement(By.xpath("//div[@id='payment-item-total']/div[2]")).click();
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-total']/div[2]")).getText(), "EUR   291.86");
        AssertJUnit.assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "Declined by issuing bank");

    }

    @Test
    public void thirdTestCase() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("4000000000000044");
        driver.findElement(By.id("input-card-holder")).clear();
        driver.findElement(By.id("input-card-holder")).sendKeys("PUDNIK EVGENIYA");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("03");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2024");
        driver.findElement(By.id("input-card-cvc")).clear();
        driver.findElement(By.id("input-card-cvc")).sendKeys("001");
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("success")).submit();
        AssertJUnit.assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-cardholder']/div[2]")).getText(), "PUDNIK EVGENIYA");
        driver.findElement(By.xpath("//div[@id='payment-item-total']/div[2]")).click();
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-total']/div[2]")).getText(), "EUR   291.86");
        AssertJUnit.assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "CONFIRMED");
    }

    @Test
    public void testScreenTestCase() throws Exception {
        Actions action = new Actions(driver);
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("4000000000000002");
        driver.findElement(By.id("input-card-holder")).sendKeys("PUDNIK EVGENIYA");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("07");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2025");
        driver.findElement(By.id("input-card-cvc")).sendKeys("777");
        WebElement cvcHint = driver.findElement(By.xpath("//*[@id=\"cvc-hint-toggle\"]"));
        action.moveToElement(cvcHint).build().perform();
        Screenshot screenshot = new AShot().takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "png", new File("src\\test\\Screenshot.png"));
        driver.findElement(By.id("action-submit")).click();
        driver.findElement(By.id("success")).submit();
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-status']/div[2]")).getText(), "CONFIRMED");
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-cardholder']/div[2]")).getText(), "PUDNIK EVGENIYA");
        assertEquals(driver.findElement(By.xpath("//div[@id='payment-item-total']/div[2]")).getText(), "EUR   291.86");


    }

    @Test
    public void InvalidTestCase() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("input-card-number")).sendKeys("0000000000000044");
        driver.findElement(By.id("input-card-holder")).clear();
        driver.findElement(By.id("input-card-holder")).sendKeys("ПУДНИК");
        new Select(driver.findElement(By.id("card-expires-month"))).selectByVisibleText("01");
        new Select(driver.findElement(By.id("card-expires-year"))).selectByVisibleText("2021");
        driver.findElement(By.id("input-card-cvc")).clear();
        driver.findElement(By.id("input-card-cvc")).sendKeys("00");
        driver.findElement(By.id("action-submit")).click();
        assertEquals(driver.findElement(By.xpath("//div[@id='card-holder-field']/div/label")).getText(), "Cardholder name is not valid");
        assertEquals(driver.findElement(By.xpath("//div[@id='card-number-field']/div/label")).getText(), "Card number is not valid");
        assertEquals(driver.findElement(By.xpath("//div[@id='card-cvc-field']/div/label")).getText(), "CVV2/CVC2/CAV2 is not valid");
        assertEquals(driver.findElement(By.xpath("//div[@id='card-expires-field']/div/label")).getText(), "Invalid date");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }

}
