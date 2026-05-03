// POM - página de login
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPagina {
    private WebDriver driver;
    private WebDriverWait espera;
    private JavascriptExecutor js;

    public LoginPagina(WebDriver driver) {
        this.driver = driver;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.js = (JavascriptExecutor) driver;
    }

    public void iniciarSesion(String usuario, String clave) {
        WebElement campoUsuario = espera.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        js.executeScript("arguments[0].style.outline='3px solid red';", campoUsuario);
        pausa(400);
        escribir(campoUsuario, usuario);

        pausa(400);
        WebElement campoClave = driver.findElement(By.id("password"));
        js.executeScript("arguments[0].style.outline='3px solid red';", campoClave);
        pausa(400);
        escribir(campoClave, clave);

        pausa(500);
        WebElement boton = driver.findElement(By.id("login-button"));
        js.executeScript("arguments[0].style.outline='3px solid blue';", boton);
        pausa(400);
        boton.click();
    }

    public void esperarInventario() {
        espera.until(ExpectedConditions.urlContains("inventory"));
    }

    public String obtenerMensajeError() {
        return espera.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test='error']"))).getText();
    }
    // funcion para que escriba en tiempo real
    private void escribir(WebElement elemento, String texto) {
        for (char c : texto.toCharArray()) {
            elemento.sendKeys(String.valueOf(c));
            pausa(60);
        }
    }

    private void pausa(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
