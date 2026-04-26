import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReporteGenerador extends RunListener {

    private static final String VERDE  = "[32m";
    private static final String ROJO   = "[31m";
    private static final String NEGRITA = "[1m";
    private static final String RESET  = "[0m";

    private final Set<String> started  = new LinkedHashSet<>();
    private final Map<String, String> failures = new LinkedHashMap<>();
    private long startTime;

    @Override
    public void testRunStarted(Description d) {
        startTime = System.currentTimeMillis();
        System.out.println();
    }

    @Override
    public void testStarted(Description d) {
        started.add(d.getMethodName());
        System.out.printf("  Ejecutando: %s%n", d.getMethodName());
    }

    @Override
    public void testFailure(Failure f) {
        failures.put(f.getDescription().getMethodName(), f.getMessage());
    }

    @Override
    public void testFinished(Description d) {
        String nombre = d.getMethodName();
        if (failures.containsKey(nombre)) {
            System.out.printf("  %s%sFAIL%s  %s%n", NEGRITA, ROJO, RESET, nombre);
        } else {
            System.out.printf("  %s%sPASS%s  %s%n", NEGRITA, VERDE, RESET, nombre);
        }
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        long ms = System.currentTimeMillis() - startTime;
        int total     = result.getRunCount();
        int failCount = result.getFailureCount();
        int passCount = total - failCount;

        System.out.println();
        System.out.printf("  Total: %d%s  |  ", total, RESET);
        System.out.printf("%sPasaron: %d%s  |  ", VERDE, passCount, RESET);
        System.out.printf("%sFallaron: %d%s%n", failCount > 0 ? ROJO : VERDE, failCount, RESET);
        System.out.println();

        StringBuilder rows = new StringBuilder();
        int i = 1;
        for (String name : started) {
            String status = failures.containsKey(name) ? "FAIL" : "PASS";
            String detail = failures.getOrDefault(name, "").replace("<", "&lt;").replace(">", "&gt;");
            rows.append("<tr>")
                .append("<td>").append(i++).append("</td>")
                .append("<td>").append(name).append("</td>")
                .append("<td class='").append(status).append("'>").append(status).append("</td>")
                .append("<td class='error'>").append(detail).append("</td>")
                .append("</tr>");
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String html = "<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Reporte</title>"
        + "<style>"
        + "*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}"
        + "body{font-family:-apple-system,BlinkMacSystemFont,'SF Pro Text','Helvetica Neue',sans-serif;background:#f5f5f7;color:#1d1d1f;padding:32px 20px;display:flex;justify-content:center;min-height:100vh}"
        + ".report{max-width:880px;width:100%;background:#fff;border-radius:24px;padding:56px 52px;box-shadow:0 2px 24px rgba(0,0,0,0.04)}"
        + "h1{font-size:32px;font-weight:700;letter-spacing:-0.015em;margin-bottom:4px;color:#1d1d1f}"
        + ".meta{font-size:15px;color:#86868b;margin-bottom:44px}"
        + ".summary{display:flex;gap:20px;margin-bottom:44px}"
        + ".stat{flex:1;background:#f9f9f9;border-radius:18px;padding:24px 20px;text-align:center}"
        + ".stat .num{font-size:44px;font-weight:700;line-height:1;color:#1d1d1f}"
        + ".stat.s-pass .num{color:#34c759}"
        + ".stat.s-fail .num{color:#ff3b30}"
        + ".stat .lbl{font-size:13px;text-transform:uppercase;letter-spacing:0.06em;color:#86868b;margin-top:8px}"
        + "table{width:100%;border-collapse:collapse}"
        + "thead th{text-align:left;padding:8px 16px 10px 16px;font-size:12px;font-weight:600;text-transform:uppercase;letter-spacing:0.05em;color:#86868b;border-bottom:1px solid #d2d2d7}"
        + "td{padding:14px 16px;border-bottom:1px solid #f0f0f5;font-size:15px;color:#1d1d1f;vertical-align:top}"
        + "tbody tr:last-child td{border-bottom:none}"
        + "tbody tr:hover{background:#f5f5f7}"
        + ".PASS{display:inline-block;padding:2px 12px;border-radius:20px;font-size:12px;font-weight:600;text-transform:uppercase;color:#34c759;background:#e9f7ef}"
        + ".FAIL{display:inline-block;padding:2px 12px;border-radius:20px;font-size:12px;font-weight:600;text-transform:uppercase;color:#ff3b30;background:#feeae6}"
        + ".error{display:block;font-size:13px;color:#86868b;margin-top:4px;word-break:break-word}"
        + "@media (max-width:600px){"
        + ".report{padding:28px 20px;border-radius:16px}"
        + ".summary{flex-direction:column;gap:12px}"
        + "h1{font-size:26px}"
        + "td,thead th{padding-left:10px;padding-right:10px}"
        + "}"
        + "</style></head><body>"
        + "<div class='report'>"
        + "<h1>Reporte de Pruebas</h1>"
        + "<p class='meta'>Ejecutado: " + date + " &nbsp;&middot;&nbsp; Duración: " + ms + " ms</p>"
        + "<div class='summary'>"
        + "<div class='stat'><div class='num'>" + total + "</div><div class='lbl'>Total</div></div>"
        + "<div class='stat s-pass'><div class='num'>" + passCount + "</div><div class='lbl'>Pasaron</div></div>"
        + "<div class='stat s-fail'><div class='num'>" + failCount + "</div><div class='lbl'>Fallaron</div></div>"
        + "</div>"
        + "<table><thead><tr><th>#</th><th>Test</th><th>Resultado</th><th>Detalle</th></tr></thead>"
        + "<tbody>" + rows + "</tbody></table>"
        + "</div>"
        + "</body></html>";

        try (PrintWriter pw = new PrintWriter(new FileWriter("reporte.html"))) {
            pw.write(html);
        }
        System.out.println("  Reporte generado");
    }
}
