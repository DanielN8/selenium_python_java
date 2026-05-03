import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import pages.LoginPagina;

public class LoginPrueba {

    private WebDriver driver;
    private LoginPagina paginaLogin;

    @Before
    public void configurar() {
        ChromeOptions opciones = new ChromeOptions();
        opciones.setExperimentalOption("prefs", Map.of(
            "credentials_enable_service", false,
            "profile.password_manager_enabled", false,
            "profile.password_manager_leak_detection", false
        ));
        opciones.addArguments("--disable-save-password-bubble");

        driver = new ChromeDriver(opciones);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        paginaLogin = new LoginPagina(driver);
        pausa(1000);
    }

    // probar login exitoso
    @Test
    public void loginExitoso() {
        paginaLogin.iniciarSesion("standard_user", "secret_sauce");
        paginaLogin.esperarInventario();
        pausa(2000);
        Assert.assertTrue("URL no contiene inventario", driver.getCurrentUrl().contains("inventory"));
    }

    // probar login invalido
    @Test
    public void loginFallido() {
        paginaLogin.iniciarSesion("user_invalid", "djksadjskla");
        String error = paginaLogin.obtenerMensajeError();
        pausa(2000);
        Assert.assertTrue("Mensaje de error incorrecto",
            error.contains("Username and password do not match"));
    }

    @After
    public void cerrar() {
        if (driver != null) driver.quit();
    }

    private void pausa(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
