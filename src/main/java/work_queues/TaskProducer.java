package work_queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import utils.MessageUtils;
import utils.RabbitQueue;

import java.lang.reflect.Array;
import java.util.concurrent.TimeoutException;

public class TaskProducer {

    private final static String QUEUE_NAME = "task_queue";
    private final static int MESSAGES_COUNT = 20;

    public static void main(String argv[]) throws java.io.IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();
        Connection connection = queue.getConnection();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String[] messages = new String[MESSAGES_COUNT];
        String dots;
        for (int i = 0; i < MESSAGES_COUNT; i++){
            dots = (i%2 == 0) ? "...." : "..";
            messages[i] = "Task number " + i + dots;
        }
        for (String msg: messages){
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
            System.out.println(" [INFO] Sent '" + msg + "'");

        }

        channel.close();
        connection.close();
    }

}
