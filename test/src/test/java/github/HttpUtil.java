package github;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

    public static String sendHttpsGet(String url, Map<String, String> headers, Map<String, String> pmap) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        // 实例化HTTP方法
        HttpGet get = new HttpGet();
        for (String keyh : headers.keySet()) {
            get.setHeader(keyh, headers.get(keyh));
        }
        String params = "";
        for (String keyp : pmap.keySet()) {
            params += "&" + keyp + "=" + pmap.get(keyp);
        }
        url += params.replaceAll("^&", "?");
        String result = "";
        try {
            get.setURI(new URI(url));
            HttpResponse response = client.execute(get);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
        }
        try {
            client.close();
        } catch (IOException e) {
        }
        return result;
    }
}