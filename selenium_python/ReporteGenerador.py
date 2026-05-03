# Generar reporte de pruebas
import unittest
import datetime
import os


class ReporteGenerador(unittest.TestResult):

    def __init__(self):
        super().__init__()
        self.pruebas = []
        self.inicio = datetime.datetime.now()

    def addSuccess(self, test):
        super().addSuccess(test)
        self.pruebas.append((test._testMethodName, "PASS", ""))

    def addFailure(self, test, err):
        super().addFailure(test, err)
        self.pruebas.append((test._testMethodName, "FAIL", str(err[1])))

    def addError(self, test, err):
        super().addError(test, err)
        self.pruebas.append((test._testMethodName, "FAIL", str(err[1])))

    def generar_html(self):
        fin = datetime.datetime.now()
        duracion = int((fin - self.inicio).total_seconds() * 1000)
        fecha = self.inicio.strftime("%Y-%m-%d %H:%M:%S")

        total = len(self.pruebas)
        pasaron = sum(1 for _, estado, _ in self.pruebas if estado == "PASS")
        fallaron = total - pasaron

        filas = ""
        for i, (nombre, estado, detalle) in enumerate(self.pruebas, 1):
            detalle_escaped = detalle.replace("<", "&lt;").replace(">", "&gt;")
            filas += (
                f"<tr>"
                f"<td>{i}</td>"
                f"<td>{nombre}</td>"
                f"<td class='{estado}'>{estado}</td>"
                f"<td class='error'>{detalle_escaped}</td>"
                f"</tr>"
            )

        html = (
            "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Reporte</title>"
            "<style>"
            "*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}"
            "body{font-family:-apple-system,BlinkMacSystemFont,'SF Pro Text','Helvetica Neue',sans-serif;background:#f5f5f7;color:#1d1d1f;padding:32px 20px;display:flex;justify-content:center;min-height:100vh}"
            ".report{max-width:880px;width:100%;background:#fff;border-radius:24px;padding:56px 52px;box-shadow:0 2px 24px rgba(0,0,0,0.04)}"
            "h1{font-size:32px;font-weight:700;letter-spacing:-0.015em;margin-bottom:4px;color:#1d1d1f}"
            ".meta{font-size:15px;color:#86868b;margin-bottom:44px}"
            ".summary{display:flex;gap:20px;margin-bottom:44px}"
            ".stat{flex:1;background:#f9f9f9;border-radius:18px;padding:24px 20px;text-align:center}"
            ".stat .num{font-size:44px;font-weight:700;line-height:1;color:#1d1d1f}"
            ".stat.s-pass .num{color:#34c759}"
            ".stat.s-fail .num{color:#ff3b30}"
            ".stat .lbl{font-size:13px;text-transform:uppercase;letter-spacing:0.06em;color:#86868b;margin-top:8px}"
            "table{width:100%;border-collapse:collapse}"
            "thead th{text-align:left;padding:8px 16px 10px 16px;font-size:12px;font-weight:600;text-transform:uppercase;letter-spacing:0.05em;color:#86868b;border-bottom:1px solid #d2d2d7}"
            "td{padding:14px 16px;border-bottom:1px solid #f0f0f5;font-size:15px;color:#1d1d1f;vertical-align:top}"
            "tbody tr:last-child td{border-bottom:none}"
            "tbody tr:hover{background:#f5f5f7}"
            ".PASS{display:inline-block;padding:2px 12px;border-radius:20px;font-size:12px;font-weight:600;text-transform:uppercase;color:#34c759;background:#e9f7ef}"
            ".FAIL{display:inline-block;padding:2px 12px;border-radius:20px;font-size:12px;font-weight:600;text-transform:uppercase;color:#ff3b30;background:#feeae6}"
            ".error{display:block;font-size:13px;color:#86868b;margin-top:4px;word-break:break-word}"
            "@media (max-width:600px){"
            ".report{padding:28px 20px;border-radius:16px}"
            ".summary{flex-direction:column;gap:12px}"
            "h1{font-size:26px}"
            "td,thead th{padding-left:10px;padding-right:10px}"
            "}"
            "</style></head><body>"
            "<div class='report'>"
            "<h1>Reporte de Pruebas</h1>"
            f"<p class='meta'>Ejecutado: {fecha} &nbsp;&middot;&nbsp; Duración: {duracion} ms</p>"
            "<div class='summary'>"
            f"<div class='stat'><div class='num'>{total}</div><div class='lbl'>Total</div></div>"
            f"<div class='stat s-pass'><div class='num'>{pasaron}</div><div class='lbl'>Pasaron</div></div>"
            f"<div class='stat s-fail'><div class='num'>{fallaron}</div><div class='lbl'>Fallaron</div></div>"
            "</div>"
            "<table><thead><tr><th>#</th><th>Test</th><th>Resultado</th><th>Detalle</th></tr></thead>"
            f"<tbody>{filas}</tbody></table>"
            "</div>"
            "</body></html>"
        )

        ruta = os.path.join(os.path.dirname(os.path.abspath(__file__)), "reporte.html")
        with open(ruta, "w", encoding="utf-8") as f:
            f.write(html)
        print("\nReporte generado")
