// POM - página de inventario
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class InventarioPagina {
    private WebDriver driver;
    private WebDriverWait espera;
    private JavascriptExecutor js;

    public InventarioPagina(WebDriver driver) {
        this.driver = driver;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.js = (JavascriptExecutor) driver;
    }

    public void agregarProductos(int cantidad) {
        espera.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector("[data-test^='add-to-cart']")));

        for (int i = 0; i < cantidad; i++) {
            List<WebElement> botones = driver.findElements(
                By.cssSelector("[data-test^='add-to-cart']"));

            if (botones.isEmpty()) break;

            WebElement boton = botones.get(0);

            js.executeScript(
                "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", boton);
            pausa(700);

            js.executeScript("arguments[0].style.outline='3px solid red';", boton);
            pausa(500);

            boton.click();
            if (i == 0) {
                espera.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".shopping_cart_badge")));
            } else {
                espera.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.cssSelector(".shopping_cart_badge"), String.valueOf(i + 1)));
            }
            pausa(800);
        }
    }

    public void agregarPrimerProductoAlCarrito() {
        agregarProductos(1);
    }

    public int obtenerContadorCarrito() {
        try {
            espera.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".shopping_cart_badge")));
            return Integer.parseInt(
                driver.findElement(By.cssSelector(".shopping_cart_badge")).getText());
        } catch (Exception e) {
            return 0;
        }
    }

    public void irAlCarrito() {
        WebElement icono = espera.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".shopping_cart_link")));

        js.executeScript("arguments[0].style.outline='3px solid blue';", icono);
        pausa(600);

        icono.click();
        espera.until(ExpectedConditions.urlContains("cart"));
        pausa(1000);
    }

    private void pausa(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
