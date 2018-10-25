package topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static utils.MessageUtils.getMessage;
import static utils.MessageUtils.getRouting;

public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();
        Connection connection = queue.getConnection();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String routingKey = getRouting(argv);
        String message = getMessage(argv);


        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        System.out.println("Sent '" + routingKey + "':'" + message + "'");

        channel.close();
        connection.close();
    }
}
