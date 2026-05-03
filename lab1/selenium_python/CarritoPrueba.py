import unittest
import time
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from paginas.LoginPagina import LoginPagina
from paginas.InventarioPagina import InventarioPagina
from paginas.CarritoPagina import CarritoPagina


class CarritoPrueba(unittest.TestCase):

    def setUp(self):
        opciones = Options()
        opciones.add_experimental_option("prefs", {
            "credentials_enable_service": False,
            "profile.password_manager_enabled": False,
            "profile.password_manager_leak_detection": False
        })
        opciones.add_argument("--disable-save-password-bubble")

        # Para usar chromedriver.exe manual, descomentar y comentar la linea de abajo:
        # from selenium.webdriver.chrome.service import Service
        # self.driver = webdriver.Chrome(service=Service("../../drivers/chromedriver.exe"), options=opciones)
        self.driver = webdriver.Chrome(options=opciones)
        self.driver.maximize_window()
        self.driver.get("https://www.saucedemo.com/")

        self.pagina_login      = LoginPagina(self.driver)
        self.pagina_inventario = InventarioPagina(self.driver)
        self.pagina_carrito    = CarritoPagina(self.driver)

        time.sleep(1.0)
        self.pagina_login.iniciar_sesion("standard_user", "secret_sauce")
        self.pagina_login.esperar_inventario()
        time.sleep(1.2)

    def test_agregar_productos_al_carrito(self):
        cantidad = 3
        self.pagina_inventario.agregar_productos(cantidad)

        contador = self.pagina_inventario.obtener_contador_carrito()

        self.pagina_inventario.ir_al_carrito()
        productos = self.pagina_carrito.obtener_nombres_productos()

        print(f"Productos agregados ({len(productos)}):")
        for p in productos:
            print(f"  [+] {p}")
        print("===================================================\n")

        time.sleep(5)

        self.assertEqual(cantidad, contador, f"El carrito debe tener {cantidad} productos")

    def test_validar_productos_en_carrito(self):
        cantidad = 2
        self.pagina_inventario.agregar_productos(cantidad)
        self.pagina_inventario.ir_al_carrito()

        productos       = self.pagina_carrito.obtener_nombres_productos()
        cantidad_carrito = self.pagina_carrito.obtener_cantidad_productos()

        print(f"Productos en carrito ({len(productos)}):")
        for p in productos:
            print(f"  [OK] {p}")
        print("===================================================\n")

        time.sleep(5)

        self.assertEqual(cantidad, cantidad_carrito, f"El carrito debe contener {cantidad} items")

    def tearDown(self):
        if self.driver:
            self.driver.quit()
