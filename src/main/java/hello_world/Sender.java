package hello_world;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import utils.RabbitQueue;

import java.util.concurrent.TimeoutException;

public class Sender {

    private final static String QUEUE_NAME = "hello";

    public static void main(String argv[]) throws java.io.IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();
        Connection connection = queue.getConnection();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [INFO] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
