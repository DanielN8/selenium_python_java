// POM - página del carrito
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CarritoPagina {
    private WebDriver driver;
    private WebDriverWait espera;

    public CarritoPagina(WebDriver driver) {
        this.driver = driver;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public int obtenerCantidadProductos() {
        espera.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart_item")));
        return driver.findElements(By.cssSelector(".cart_item")).size();
    }

    public List<String> obtenerNombresProductos() {
        espera.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart_item")));
        List<WebElement> items = driver.findElements(By.cssSelector(".cart_item .inventory_item_name"));
        return items.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String obtenerNombrePrimerProducto() {
        return driver.findElement(By.cssSelector(".inventory_item_name")).getText();
    }
}
