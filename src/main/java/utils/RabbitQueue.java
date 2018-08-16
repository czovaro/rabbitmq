package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitQueue {

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public RabbitQueue(String host) throws IOException, TimeoutException {
        this.factory = new ConnectionFactory();
        factory.setHost(host);
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
    }

    public Connection getConnection() {
        return connection;
    }
    public Channel getChannel() {
        return channel;
    }
}
