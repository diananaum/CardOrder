import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        System.setProperty("webdriver.chrome.driver", "/Users/Dianaum/IdeaProjects/Zayavka/driver/mac/chromedriver");
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void validTest() {
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Иванов Иван-Иван");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+79999999999");

        driver.findElement(By.cssSelector("label[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id=order-success]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void invalidName() {
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Ivan");
        driver.findElement(By.className("button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.className("input__sub")).getText().trim();

        assertEquals(expected, actual);
    }

    @Test
    public void invalidPhone() {
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+799999999999999");
        driver.findElement(By.className("button")).click();

        List<WebElement> exceptions = driver.findElements(By.className("input__sub"));

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = exceptions.get(1).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void invalidCheckbox() {
        driver.findElement(By.cssSelector("span[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("span[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.className("button")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.className("input_invalid")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void fieldBlank() {
        driver.findElement(By.className("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.className("input__sub")).getText().trim();
        assertEquals(expected, actual);
    }
}