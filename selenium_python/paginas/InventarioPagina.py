# POM - página de inventario de SauceDemo
import time
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


class InventarioPagina:
    def __init__(self, driver):
        self.driver = driver
        self.espera = WebDriverWait(driver, 5)

    def agregar_productos(self, cantidad):
        self.espera.until(EC.presence_of_all_elements_located(
            (By.CSS_SELECTOR, "[data-test^='add-to-cart']")))

        for i in range(cantidad):
            botones = self.driver.find_elements(By.CSS_SELECTOR, "[data-test^='add-to-cart']")
            if not botones:
                break

            boton = botones[0]

            self.driver.execute_script(
                "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", boton)
            time.sleep(0.7)

            self.driver.execute_script("arguments[0].style.outline='3px solid red';", boton)
            time.sleep(0.5)

            boton.click()

            if i == 0:
                self.espera.until(EC.visibility_of_element_located(
                    (By.CSS_SELECTOR, ".shopping_cart_badge")))
            else:
                self.espera.until(EC.text_to_be_present_in_element(
                    (By.CSS_SELECTOR, ".shopping_cart_badge"), str(i + 1)))
            time.sleep(0.8)

    def agregar_primer_producto_al_carrito(self):
        self.agregar_productos(1)

    def obtener_contador_carrito(self):
        try:
            self.espera.until(EC.visibility_of_element_located(
                (By.CSS_SELECTOR, ".shopping_cart_badge")))
            return int(self.driver.find_element(By.CSS_SELECTOR, ".shopping_cart_badge").text)
        except Exception:
            return 0

    def ir_al_carrito(self):
        icono = self.espera.until(EC.element_to_be_clickable(
            (By.CSS_SELECTOR, ".shopping_cart_link")))
        self.driver.execute_script("arguments[0].style.outline='3px solid blue';", icono)
        time.sleep(0.6)
        icono.click()
        self.espera.until(EC.url_contains("cart"))
        time.sleep(1.0)
