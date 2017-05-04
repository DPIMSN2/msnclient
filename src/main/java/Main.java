import jms.JMSDispatcher;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class Main {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) {
        JMSDispatcher.getInstance();
        try {
            JMSDispatcher.publishMessage(QUEUE_NAME,"consumer alles");
            JMSDispatcher.closeDispatcher();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
