package routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitQueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static utils.MessageUtils.getMessage;
import static utils.MessageUtils.getSeverity;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws IOException, TimeoutException {
        RabbitQueue queue = new RabbitQueue("localhost");
        Channel channel = queue.getChannel();
        Connection connection = queue.getConnection();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String severity = getSeverity(argv);
        String message = getMessage(argv);

        int count = 0;
        while (count < 10){
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println("Sent '" + severity + "':'" + message +"'");
            count++;
        }

        channel.close();
        connection.close();
    }
}
