package io.github.amzexin.commons.test.all.http;

import io.github.amzexin.commons.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Test;

import java.util.TreeSet;

/**
 * Description: CommonTest
 *
 * @author Lizexin
 * @date 2022-05-24 14:39
 */
@Slf4j
public class HttpUtilsTest {

    @Test
    public void test20220524_1439() {
        /**
         * 1. 创建HttpClient对象。
         *
         * 2. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
         *
         * 3. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HttpParams params)方法来添加请求参数；对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
         *
         * 4. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
         *
         * 5. 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。程序可通过该对象获取服务器的响应内容。
         *
         * 6. 释放连接。无论执行方法是否成功，都必须释放连接
         */
    }

    @Test
    public void httpsTest() {
        String url = "https://www.baidu.com";
        HttpUtils.get(url);
    }

    @Test
    public void cookieTest() {
        BasicClientCookie basicCookieStore1 = new BasicClientCookie("name", "value");
        basicCookieStore1.setDomain("amzexin.com");
        basicCookieStore1.setPath("/");
        BasicClientCookie basicCookieStore2 = new BasicClientCookie("name", "value");
        basicCookieStore2.setDomain("amzexin.com");
        basicCookieStore2.setPath("/x");
        log.info("basicCookieStore1 == basicCookieStore2 {}", basicCookieStore1.equals(basicCookieStore2));

        TreeSet<Cookie> cookieStores = new TreeSet<>(new CookieIdentityComparator());
        cookieStores.add(basicCookieStore1);
        cookieStores.add(basicCookieStore2);
        log.info("cookieStores.size = {}", cookieStores.size());
    }
}
