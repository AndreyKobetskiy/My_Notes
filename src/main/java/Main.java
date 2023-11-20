import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Main {
    private static final Logger logger = LogManager.getLogger("Main ");
    public static void main(String[] args) {

        Data data = new Data();
        logger.info("Data created");
        new GUI(data);
        logger.info("gui created");
    }
}
