import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import java.io.IOException;

/**
 * Created by enudler on 15/03/2016.
 */
public class InfluxClient {

    private final InfluxDB influxDB;
    private final String url;

    public InfluxClient(String url) {
        this.url = url;
        this.influxDB = InfluxDBFactory.connect(url, "root", "root");
        influxDB.createDatabase("metrics");
    }

    public void publish(String lineProtocol) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url+"/write?db=metrics");
        try {
            httppost.setEntity(new StringEntity(lineProtocol));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        HttpEntity entity = response.getEntity();
    }
}
