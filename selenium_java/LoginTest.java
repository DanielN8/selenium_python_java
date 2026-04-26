import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginTest {

    public static void main(String[] args) {
        // si se quiere usar el chromedriver.exe descomentar y comentar lo de abajo
        // System.setProperty("webdriver.chrome.driver", "../drivers/chromedriver.exe");
        // WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", "../drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://www.saucedemo.com/");
            driver.manage().window().maximize();

            probarLoginExitoso(driver, wait);
            probarLoginFallido(driver, wait);

        } finally {
            driver.quit();
        }
    }

    static void probarLoginExitoso(WebDriver driver, WebDriverWait wait) {
        driver.get("https://www.saucedemo.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("inventory"));

        if (driver.getCurrentUrl().contains("inventory")) {
            System.out.println("Login exitoso, URL contiene 'inventory'");
        } else {
            System.out.println("Login exitoso, URL inesperada: " + driver.getCurrentUrl());
        }
    }

    static void probarLoginFallido(WebDriver driver, WebDriverWait wait) {
        driver.get("https://www.saucedemo.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys("usuario_invalido");
        driver.findElement(By.id("password")).sendKeys("contrasena_incorrecta");
        driver.findElement(By.id("login-button")).click();

        WebElement error = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
        );

        String mensajeError = error.getText();
        if (mensajeError.contains("Username and password do not match")) {
            System.out.println("Login fallido: mensaje de error correcto");
            System.out.println("       Mensaje: " + mensajeError);
        } else {
            System.out.println("Login fallido, mensaje inesperado:" + mensajeError);
        }
    }
}
