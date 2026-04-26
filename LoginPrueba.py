import unittest
import time
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from paginas.LoginPagina import LoginPagina


class LoginPrueba(unittest.TestCase):

    def setUp(self):
        opciones = Options()
        opciones.add_experimental_option("prefs", {
            "credentials_enable_service": False,
            "profile.password_manager_enabled": False,
            "profile.password_manager_leak_detection": False
        })
        opciones.add_argument("--disable-save-password-bubble")

        # Para usar chromedriver.exe manual descomentar y comentar la linea de abajo
        # from selenium.webdriver.chrome.service import Service
        # self.driver = webdriver.Chrome(service=Service("../drivers/chromedriver.exe"), options=opciones)
        self.driver = webdriver.Chrome(options=opciones)
        self.driver.maximize_window()
        self.driver.get("https://www.saucedemo.com/")
        self.pagina_login = LoginPagina(self.driver)
        time.sleep(1.0)

    # probar login exitoso
    def test_login_exitoso(self):
        self.pagina_login.iniciar_sesion("standard_user", "secret_sauce")
        self.pagina_login.esperar_inventario()
        time.sleep(2.0)
        self.assertIn("inventory", self.driver.current_url, "URL no contiene inventario")

    # probar login invalido
    def test_login_fallido(self):
        self.pagina_login.iniciar_sesion("user_invalid", "djksadjskla")
        error = self.pagina_login.obtener_mensaje_error()
        time.sleep(2.0)
        self.assertIn("Username and password do not match", error, "Mensaje de error incorrecto")

    def tearDown(self):
        if self.driver:
            self.driver.quit()
