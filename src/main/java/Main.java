import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by enudler on 15/03/2016.
 */
public class Main {


    private static final String influxUrl = "http://192.168.99.100:8086";
    private static final String kafkaUrl = "192.168.99.100:2181";
    private static List<String> topicNames = Arrays.asList("Production_SmartRouting_ThisIsMyPrefix_Metrics_JVM", "Production_SmartRouting_ThisIsMyPrefix_Metrics_Counters", "Production_SmartRouting_ThisIsMyPrefix_Metrics_Gauges");

    public static void main(String[] args) {
        handleMetrics();
    }

    private static void handleMetrics() {
        InfluxClient influxClient = new InfluxClient(influxUrl);
        KafkaConsumer kafkaConsumer = new KafkaConsumer(kafkaUrl);
        ExecutorService pool = Executors.newFixedThreadPool(topicNames.size());

        for (int i = 0; i < topicNames.size(); i++) {
            final int finalI = i;
            pool.submit(() -> kafkaConsumer.consumeMessages(topicNames.get(finalI), o ->
            {
                System.out.println(o);
                influxClient.publish(o);
            }));
        }
    }
}
