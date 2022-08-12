package io.github.amzexin.commons.pathplan.common;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 如何通过HttpURLConnection得到http 302的跳转地址
 *
 * @author javaniu
 */
public class Http302 {


    public static String realUrl(String url) throws IOException {
        URL serverUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
        String realUrl = conn.getHeaderField("Location");
        conn.disconnect();
        return realUrl;
    }

}
