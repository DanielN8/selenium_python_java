import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class EjecutarCarrito {
    public static void main(String[] args) throws Exception {
        JUnitCore core = new JUnitCore();
        core.addListener(new ReporteGenerador());
        Result result = core.run(CarritoPrueba.class);
        System.exit(result.wasSuccessful() ? 0 : 1);
    }
}
