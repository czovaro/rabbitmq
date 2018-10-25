package rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import utils.RabbitQueue;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient {

    private RabbitQueue rabbitQueue;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RPCClient() throws IOException, TimeoutException {
        this.rabbitQueue = new RabbitQueue("localhost");
        this.channel = this.rabbitQueue.getChannel();
    }

    public String call(String message) throws IOException, InterruptedException {
        final String correlationId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties properties = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, properties, message.getBytes());

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String consumerTag = channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(correlationId)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }
        });

        String result = response.take();
        channel.basicCancel(consumerTag);
        return result;
    }

    public void close() throws IOException {
        rabbitQueue.getConnection().close();
    }

    public static void main(String[] argv) {
        RPCClient fibonacciRpc = null;
        String response;

        try {
            fibonacciRpc = new RPCClient();

            for (int i = 0; i < 32; i++) {
                System.out.println("Requesting fib(" + Integer.toString(i) + ")");
                response = fibonacciRpc.call(Integer.toString(i));
                System.out.println("Got " + response);
            }
        } catch (TimeoutException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (fibonacciRpc != null) {
                try {
                    fibonacciRpc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
