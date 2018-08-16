package work_queues;

import com.rabbitmq.client.*;
import utils.RabbitQueue;
import utils.WorkUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class Worker {

    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [INFO] Waiting for messages.");
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [INFO] Received '" + message + "'");
                try {
                    System.out.println(" [INFO] Working on task " + message);
                    WorkUtils.doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [INFO] " + message + " is Done!");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
