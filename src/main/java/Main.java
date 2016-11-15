import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by enudler on 15/03/2016.
 */
public class Main {

    private static String influxUrl;
    private static String kafkaUrl;
    private static List<String> topicNames = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Kafka to Influx");
        if (args.length < 3) {
            throw new Exception("Expecting at least 3 args, kafkaUrl, influxUrl and a topic");
        }

        kafkaUrl = args[0];
        System.out.println("kafkaUrl: " + kafkaUrl);
        influxUrl = args[1];
        System.out.println("influxUrl: " + influxUrl);


        for (int i = 2; i < args.length; i++) {
            topicNames.add(args[i]);
            System.out.println("topicName: " + args[i]);

        }

        handleMetrics();
    }

    private static void handleMetrics() {
        InfluxClient influxClient = new InfluxClient(influxUrl);
        KafkaConsumer kafkaConsumer = new KafkaConsumer(kafkaUrl);
        ExecutorService pool = Executors.newFixedThreadPool(topicNames.size());
        ExecutorService influxPool = Executors.newFixedThreadPool(25);

        final int[] messageId = {0};

        for (int i = 0; i < topicNames.size(); i++) {
            final int finalI = i;
            pool.submit(() -> kafkaConsumer.consumeMessages(topicNames.get(finalI), o ->
            {
                if (messageId[0] % 1000 == 0) {
                    System.out.println(System.currentTimeMillis() + " current index: " + messageId[0]);
                    System.out.println(o);
                }
                messageId[0]++;
                influxPool.submit(() -> influxClient.publish(o));
            }));
        }
    }
}
