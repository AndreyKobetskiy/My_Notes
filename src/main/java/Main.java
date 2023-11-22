import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Main {
    private static final Logger logger = LogManager.getLogger("Note log");
    public static void main(String[] args) {

        Data data = new Data(logger);
        logger.info("Data created");

        @SuppressWarnings("unused")
        GUI gui = new  GUI(data, logger);
        logger.info("App load ended");
    }
}
