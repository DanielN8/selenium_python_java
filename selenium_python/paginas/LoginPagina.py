# POM - página de login de SauceDemo
import time
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


class LoginPagina:
    def __init__(self, driver):
        self.driver = driver
        self.espera = WebDriverWait(driver, 5)

    def iniciar_sesion(self, usuario, clave):
        campo_usuario = self.espera.until(EC.visibility_of_element_located((By.ID, "user-name")))
        self.driver.execute_script("arguments[0].style.outline='3px solid red';", campo_usuario)
        time.sleep(0.4)
        self._escribir(campo_usuario, usuario)

        time.sleep(0.4)
        campo_clave = self.driver.find_element(By.ID, "password")
        self.driver.execute_script("arguments[0].style.outline='3px solid red';", campo_clave)
        time.sleep(0.4)
        self._escribir(campo_clave, clave)

        time.sleep(0.5)
        boton = self.driver.find_element(By.ID, "login-button")
        self.driver.execute_script("arguments[0].style.outline='3px solid blue';", boton)
        time.sleep(0.4)
        boton.click()

    def esperar_inventario(self):
        self.espera.until(EC.url_contains("inventory"))

    def obtener_mensaje_error(self):
        return self.espera.until(EC.visibility_of_element_located(
            (By.CSS_SELECTOR, "[data-test='error']"))).text

    # funcion para que escriba en tiempo real
    def _escribir(self, elemento, texto):
        for char in texto:
            elemento.send_keys(char)
            time.sleep(0.05)
