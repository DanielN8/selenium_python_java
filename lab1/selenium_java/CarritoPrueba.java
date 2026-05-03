import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.CarritoPagina;
import pages.InventarioPagina;
import pages.LoginPagina;

public class CarritoPrueba {

    private WebDriver driver;
    private WebDriverWait espera;
    private LoginPagina paginaLogin;
    private InventarioPagina paginaInventario;
    private CarritoPagina paginaCarrito;

    @Before
    public void configurar() {
        ChromeOptions opciones = new ChromeOptions();
        opciones.setExperimentalOption("prefs", Map.of(
            "credentials_enable_service", false,
            "profile.password_manager_enabled", false,
            "profile.password_manager_leak_detection", false
        ));
        opciones.addArguments("--disable-save-password-bubble");
        // si se quiere usar el chromedriver.exe descomentar y comentar lo de abajo
        // System.setProperty("webdriver.chrome.driver", "../drivers/chromedriver.exe");
        // WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(opciones);
        driver.manage().window().maximize();
        espera = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("https://www.saucedemo.com/");

        paginaLogin      = new LoginPagina(driver);
        paginaInventario = new InventarioPagina(driver);
        paginaCarrito    = new CarritoPagina(driver);

        pausa(1000);
        paginaLogin.iniciarSesion("standard_user", "secret_sauce");
        paginaLogin.esperarInventario();
        pausa(1200);
    }

    @Test
    public void agregarProductosAlCarrito() {
        int cantidad = 3;
        paginaInventario.agregarProductos(cantidad);

        int contador = paginaInventario.obtenerContadorCarrito();

        paginaInventario.irAlCarrito();
        List<String> productos = paginaCarrito.obtenerNombresProductos();

        System.out.println("Productos agregados (" + productos.size() + "):");
        for (String p : productos) System.out.println("  [+] " + p);
        System.out.println("===========================================\n");

        pausa(5000);

        Assert.assertEquals("El carrito debe tener " + cantidad + " productos", cantidad, contador);
    }

    @Test
    public void validarProductosEnCarrito() {
        int cantidad = 2;
        paginaInventario.agregarProductos(cantidad);
        paginaInventario.irAlCarrito();

        List<String> productos = paginaCarrito.obtenerNombresProductos();
        int cantidadEnCarrito  = paginaCarrito.obtenerCantidadProductos();

        System.out.println("Productos en carrito (" + productos.size() + "):");
        for (String p : productos) System.out.println("  [OK] " + p);
        System.out.println("===========================================\n");

        pausa(5000);

        Assert.assertEquals("El carrito debe contener " + cantidad + " items", cantidad, cantidadEnCarrito);
    }

    @After
    public void cerrar() {
        if (driver != null) driver.quit();
    }

    private void pausa(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
