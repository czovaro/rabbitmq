package publish_subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static utils.MessageUtils.getMessage;

public class EmitLogs {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();
        Connection connection = queue.getConnection();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = getMessage(argv);

        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println("Sent '" + message +"'");

        channel.close();
        connection.close();
    }
}
