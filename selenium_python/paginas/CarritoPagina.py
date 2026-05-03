# POM - página del carrito de SauceDemo
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


class CarritoPagina:
    def __init__(self, driver):
        self.driver = driver
        self.espera = WebDriverWait(driver, 5)

    def obtener_cantidad_productos(self):
        self.espera.until(EC.presence_of_element_located((By.CSS_SELECTOR, ".cart_item")))
        return len(self.driver.find_elements(By.CSS_SELECTOR, ".cart_item"))

    def obtener_nombres_productos(self):
        self.espera.until(EC.presence_of_element_located((By.CSS_SELECTOR, ".cart_item")))
        items = self.driver.find_elements(By.CSS_SELECTOR, ".cart_item .inventory_item_name")
        return [item.text for item in items]

    def obtener_nombre_primer_producto(self):
        return self.driver.find_element(By.CSS_SELECTOR, ".inventory_item_name").text
