import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class Login {

    private WebDriver driver;

    @Before
    public void setUp() {

//        // Для того, чтобы запустить под капотом
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
//        driver = new ChromeDriver(options);


        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    }

    @Test
    public void testInvalidInput() {

        String[][] testData = {

                {"", ""}, // Пустые данные
                {"invalidEmail@example.com", ""},  // Нет пароля
                {"", "invalidPassword123"},  // Нет email-а
                {"invalidEmail@example.com", "test"},  // Невалидный email
                {"test", "invalidPassword123"}  // Невалидный пароль
        };

        for (String[] data : testData) {
            String email = data[0];
            String password = data[1];

            driver.get("https://synaps-dev.k3s.dex-it.ru/");

            WebElement inputEmail = driver.findElement(By.id("basic_email"));
            inputEmail.click();
            inputEmail.clear();
            inputEmail.sendKeys(email);

            WebElement inputPassword = driver.findElement(By.id("basic_password"));
            inputPassword.click();
            inputPassword.clear();
            inputPassword.sendKeys(password);

            WebElement buttonEnter = driver.findElement(By.xpath(".//span[text()='Войти']"));
            buttonEnter.click();

            boolean haveAlert = false;

            if (!email.isEmpty() && !password.isEmpty()) {
                WebElement errorAlert = driver.findElement(By.xpath(".//div[@class='ant-alert-message']"));
                String errorTextAlert = errorAlert.getText();
            }

            if (!password.isEmpty() && email.isEmpty()) {
                WebElement errorAlertEmail = driver.findElement(By.xpath(".//div[contains(text(), 'поч')]"));
                String errorTextMail = errorAlertEmail.getText();
            }

            if (!email.isEmpty()) {
                WebElement errorAlertPass = driver.findElement(By.xpath(".//div[contains(text(), 'пар')]"));
                String errorTextPass = errorAlertPass.getText();
                //haveAlert = errorTextPass.isEmpty();
            }

            if (email.isEmpty() && password.isEmpty()) {
                Assert.assertFalse("Ошибка валидации для пустых данных", haveAlert);
            } else if (email.isEmpty()) {
                Assert.assertFalse("Email пустой", haveAlert);
            } else if (password.isEmpty()) {
                Assert.assertFalse("Пароль пустой", haveAlert);
            } else {
                Assert.assertFalse("Валидация не сработала", haveAlert);

            }
        }
    }

    @Test
    public void testValidInput (){
        driver.get("https://synaps-dev.k3s.dex-it.ru/");

        WebElement inputEmail = driver.findElement(By.id("basic_email"));
        inputEmail.click();
        inputEmail.clear();
        inputEmail.sendKeys("test");

        WebElement inputPassword = driver.findElement(By.id("basic_password"));
        inputPassword.click();
        inputPassword.clear();
        inputPassword.sendKeys("test");

        WebElement buttonEnter = driver.findElement(By.xpath(".//span[text()='Войти']"));
        buttonEnter.click();


        int logoOnLogin = driver.findElements(By.xpath(".//img[@class='ant-image-img css-dev-only-do-not-override-ccdg5a']")).size();
        boolean logoIsPresent = logoOnLogin > 0;
        Assert.assertTrue("Пользователь не авторизован", logoIsPresent);

    }

    @After
    public void close() {
        driver.quit();
    }

}


