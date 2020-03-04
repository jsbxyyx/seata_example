package org.xxz.test;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

/**
 * @author jimin.jm@alibaba-inc.com
 * @date 2019/12/13
 */
public class HttpUtil {

    public static String sendHttpsGet(String url, Map<String, String> headers, Map<String, String> pmap) {
        HttpClient client = new DefaultHttpClient();
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
            // TODO: handle exception
        }
        try {
            result = new String(result.getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;

    }
}