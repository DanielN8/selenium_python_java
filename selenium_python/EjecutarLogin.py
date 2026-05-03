import unittest
from LoginPrueba import LoginPrueba
from ReporteGenerador import ReporteGenerador

if __name__ == "__main__":
    suite = unittest.TestLoader().loadTestsFromTestCase(LoginPrueba)

    reporte = ReporteGenerador()
    suite.run(reporte)
    reporte.generar_html()

    total    = reporte.testsRun
    fallaron = len(reporte.failures) + len(reporte.errors)
    pasaron  = total - fallaron
    print(f"Total: {total} \n Pasaron: {pasaron} \n Fallaron: {fallaron}")
