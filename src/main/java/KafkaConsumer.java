import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by enudler on 15/03/2016.
 */
public class KafkaConsumer {

    private final ConsumerConfig consumerConfig;

    public KafkaConsumer(String kafkaUrl) {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", kafkaUrl);
        properties.put("group.id", "defaultGroup");

        this.consumerConfig = new ConsumerConfig(properties);
    }

    private ConsumerIterator<byte[], byte[]> getConsumerIterator(String topicName) {

        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topicName, 1);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        KafkaStream<byte[], byte[]> stream = consumerConnector.createMessageStreams(topicCountMap).get(topicName).get(0);
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        return iterator;
    }

    public void consumeMessages(String topicName, java.util.function.Consumer<String> consumer) {
        ConsumerIterator<byte[], byte[]> iterator = getConsumerIterator(topicName);
        while (iterator.hasNext()) {
            MessageAndMetadata<byte[], byte[]> next = iterator.next();
            String message = new String(next.message());
            consumer.accept(message);
        }
    }
}
