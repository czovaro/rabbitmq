package routing;

import com.rabbitmq.client.*;
import utils.RabbitQueue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        if (argv.length < 1){
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        for(String severity : argv){
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }

        System.out.println("Connected and waiting for messages");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws UnsupportedEncodingException {
                String message = new String(body, "UTF-8");
                System.out.println("Received " + envelope.getRoutingKey() + ":" + message);

            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}
