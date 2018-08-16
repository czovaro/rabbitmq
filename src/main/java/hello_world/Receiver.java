package hello_world;

import com.rabbitmq.client.*;
import utils.RabbitQueue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class Receiver {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [INFO] Waiting for messages.");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                throws UnsupportedEncodingException {
                String message = new String(body, "UTF-8");
                System.out.println(" [INFO] Received '" + message + "'");
            }
        };

        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
