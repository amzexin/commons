package com.amzexin.util.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public final class HttpUtil {

    private static boolean openLogger = true;

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * CONTENT_TYPE
     */
    public static final String CONTENT_TYPE = "Content-type";
    /**
     * 失败重发间隔，毫秒
     */
    private static final int FREQUENCY_MS = 1000;
    /**
     * 指从连接池获取连接的timeout
     */
    private static int CONNECTION_REQUEST_TIMEOUT = 1000;
    /**
     * 指客户端和服务器建立连接的timeout
     */
    private static int CONNECT_TIMEOUT = 3000;
    /**
     * 指数据传输过程中数据包之间间隔的最大时间
     */
    private static int SOCKET_TIMEOUT = 5000;
    /**
     * HttpClient
     */
    private static CloseableHttpClient httpClient;

    static {
        // 初始化 HttpClient
        httpClient = createHttpClient();
    }

    // region 创建HttpClient

    private static CloseableHttpClient createHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(128);
        connectionManager.setDefaultMaxPerRoute(128);
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    /**
     * 获取 HttpClient
     * TODO 待优化成连接池
     *
     * @param url     url
     * @param cookies cookies
     * @return CloseableHttpClient
     */
    private static CloseableHttpClient getHttpClient(String url, List<BasicClientCookie> cookies) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        // TODO cookie不生效，待解决；参考博客：https://blog.csdn.net/zhongzh86/article/details/84070561
        if (cookies != null && !cookies.isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            httpClientBuilder.setDefaultCookieStore(cookieStore);
            for (BasicClientCookie basicClientCookie : cookies) {
                cookieStore.addCookie(basicClientCookie);
            }
        }

        if (url.startsWith("https")) {
            SSLContext sslContext = SSLContexts.createSystemDefault();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
        }

        return httpClientBuilder.build();
    }

    /**
     * 创建SSL Client：方式二
     *
     * @return CloseableHttpClient
     */
    private static CloseableHttpClient createSSLInsecureClient2() {
        // 采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        // 创建自定义的 httpsClient 对象
        return HttpClients.custom().setConnectionManager(connManager).build();
    }

    // endregion

    /**
     * ContentType 静态内部类
     */
    public static class ContentType {
        public static final String APPLICATION_JSON_VALUE = "application/json";
        public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
        public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
        public static final String APPLICATION_FORM_URLENCODED_UTF8_VALUE = "application/x-www-form-urlencoded;charset=UTF-8";
    }

    /**
     * 设置HttpUtil工具栏是否打开日志打印功能
     *
     * @param openLogger openLogger
     */
    public static void setOpenLogger(boolean openLogger) {
        HttpUtil.openLogger = openLogger;
    }

    //region GET 请求

    /**
     * GET 请求
     *
     * @param url url
     * @return HttpResult
     */
    public static HttpResult<String> get(String url) {
        HttpParams httpParams = new HttpParams();
        return get(url, httpParams);
    }

    /**
     * GET 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @return HttpResult<String>
     */
    public static HttpResult<String> get(String url, HttpParams httpParams) {
        return http(HttpMethod.GET, url, httpParams);
    }

    /**
     * GET 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @param clazz      clazz
     * @param <E>        <E>
     * @return HttpResult<E>
     */
    public static <E> HttpResult<E> get(String url, HttpParams httpParams, Class<E> clazz) {
        HttpResult<String> result = http(HttpMethod.GET, url, httpParams);
        return convertHttpResult(result, clazz);
    }

    //endregion

    //region POST 请求

    /**
     * POST 请求
     *
     * @param url url
     * @return HttpResult<String>
     */
    public static HttpResult<String> post(String url) {
        HttpParams httpParams = new HttpParams();
        return post(url, httpParams);
    }

    /**
     * POST 请求
     *
     * @param url  url
     * @param body body
     * @return HttpResult<String>
     */
    public static HttpResult<String> post(String url, String body) {
        HttpParams httpParams = new HttpParams();
        httpParams.setBody(body);
        return post(url, httpParams);
    }

    /**
     * POST 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @return HttpResult<String>
     */
    public static HttpResult<String> post(String url, HttpParams httpParams) {
        return http(HttpMethod.POST, url, httpParams);
    }

    /**
     * POST 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @param clazz      clazz
     * @param <E>        <E>
     * @return HttpResult<E>
     */
    public static <E> HttpResult<E> post(String url, HttpParams httpParams, Class<E> clazz) {
        HttpResult<String> result = http(HttpMethod.POST, url, httpParams);
        return convertHttpResult(result, clazz);
    }
    //endregion

    //region PUT 请求

    /**
     * PUT 请求
     *
     * @param url url
     * @return HttpResult<String>
     */
    public static HttpResult<String> put(String url) {
        HttpParams httpParams = new HttpParams();
        return put(url, httpParams);
    }

    /**
     * PUT 请求
     *
     * @param url  url
     * @param body body
     * @return HttpResult<String>
     */
    public static HttpResult<String> put(String url, String body) {
        HttpParams httpParams = new HttpParams();
        httpParams.setBody(body);
        return put(url, httpParams);
    }

    /**
     * PUT 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @return HttpResult<String>
     */
    public static HttpResult<String> put(String url, HttpParams httpParams) {
        return http(HttpMethod.PUT, url, httpParams);
    }


    /**
     * PUT 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @param clazz      clazz
     * @param <E>        <E>
     * @return HttpResult<E>
     */
    public static <E> HttpResult<E> put(String url, HttpParams httpParams, Class<E> clazz) {
        HttpResult<String> result = http(HttpMethod.PUT, url, httpParams);
        return convertHttpResult(result, clazz);
    }
    //endregion

    //region DELETE 请求


    /**
     * DELETE 请求
     *
     * @param url url
     * @return HttpResult<String>
     */
    public static HttpResult<String> delete(String url) {
        HttpParams httpParams = new HttpParams();
        return delete(url, httpParams);
    }

    /**
     * DELETE 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @return HttpResult<String>
     */
    public static HttpResult<String> delete(String url, HttpParams httpParams) {
        return http(HttpMethod.DELETE, url, httpParams);
    }

    /**
     * DELETE 请求
     *
     * @param url        url
     * @param httpParams httpParams
     * @param clazz      clazz
     * @param <E>        <E>
     * @return HttpResult<E>
     */
    public static <E> HttpResult<E> delete(String url, HttpParams httpParams, Class<E> clazz) {
        HttpResult<String> result = http(HttpMethod.DELETE, url, httpParams);
        return convertHttpResult(result, clazz);
    }

    //endregion

    //region HTTP 请求
    private static HttpResult<String> http(HttpMethod httpMethod, String url, HttpParams httpParams) {
        HttpResult<String> result = null;

        Map<String, String> headers = httpParams.getHeaders();
        List<BasicClientCookie> cookies = httpParams.getCookies();
        Map<String, String> formMap = httpParams.getForms();
        Map<String, String> paramMap = httpParams.getParams();
        String body = httpParams.getBody();
        Integer connectionRequestTimeout = httpParams.getConnectionRequestTimeout();
        Integer connectionTimeout = httpParams.getConnectionTimeout();
        Integer socketTimeout = httpParams.getSocketTimeout();
        Integer retryCount = httpParams.getRetryCount();
        retryCount = retryCount <= 0 ? 1 : retryCount;
        String charSet = httpParams.getCharSet();


        //设置HTTP Header TraceID
        String traceId = MDC.get(HttpConstants.LOG_TRACE_ID);
        if (traceId != null && traceId.trim().length() > 0) {
            headers.put(HttpConstants.HTTP_HEADER_TRACE_ID, traceId);
        }

        // 设置通用请求头
        headers = headers == null ? new HashMap<>(2) : headers;

        // 设置通用cookie
        cookies = cookies == null ? new ArrayList<>() : cookies;

        if (formMap != null && formMap.size() > 0 && Objects.equals(httpMethod, HttpMethod.POST)) {
            if (!headers.containsKey(CONTENT_TYPE)) {
                headers.put(CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE);
            }
        }

        if (body != null && body.trim().length() > 0) {
            if (!headers.containsKey(CONTENT_TYPE)) {
                headers.put(CONTENT_TYPE, ContentType.APPLICATION_JSON_UTF8_VALUE);
            }
        }

        //获取 RequestConfig

        RequestConfig requestConfig = getRequestConfig(connectionRequestTimeout, connectionTimeout, socketTimeout);


        for (int i = 1; i <= retryCount; i++) {
            if (HttpMethod.GET.equals(httpMethod)) {
                result = httpGet(url, charSet, headers, cookies, paramMap, requestConfig, i);
            } else if (HttpMethod.POST.equals(httpMethod)) {
                result = httpPost(url, charSet, headers, cookies, formMap, body, requestConfig, i);
            } else if (HttpMethod.PUT.equals(httpMethod)) {
                result = httpPut(url, charSet, headers, cookies, body, requestConfig, i);
            } else if (HttpMethod.DELETE.equals(httpMethod)) {
                result = httpDelete(url, charSet, headers, cookies, paramMap, requestConfig, i);
            } else {
                printErrorLogger("http method not support now {}", httpMethod);
                throw new RuntimeException("http method not support now:" + httpMethod);
            }

            if (success(result.getCode())) {
                break;
            } else {
                try {
                    Thread.sleep(FREQUENCY_MS);
                } catch (InterruptedException e) {
                    printErrorLogger(e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * 获取 RequestConfig
     *
     * @param connectionRequestTimeout connectionRequestTimeout
     * @param connectTimeout           connectTimeout
     * @param socketTimeout            socketTimeout
     * @return RequestConfig
     */
    private static RequestConfig getRequestConfig(Integer connectionRequestTimeout, Integer connectTimeout, Integer socketTimeout) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
    }

    /**
     * GET 请求
     *
     * @param url           url
     * @param charset       charset
     * @param headers       headers
     * @param cookies       cookies
     * @param paramMap      paramMap
     * @param requestConfig requestConfig
     * @param times         times
     * @return HttpResult
     */
    private static HttpResult<String> httpGet(String url, String charset, Map<String, String> headers, List<BasicClientCookie> cookies,
                                              Map<String, String> paramMap, RequestConfig requestConfig, int times) {
        // realUrl
        String realUrl = realUrl(HttpMethod.GET, url, paramMap);

        // HttpGet
        HttpGet httpget = new HttpGet(realUrl);
        httpget.setConfig(requestConfig);
        setHeaders(httpget, headers);

        //httpClient
        CloseableHttpClient httpClient = getHttpClient(url, cookies);

        HttpResult<String> result = response(httpClient, httpget, charset);

        printInfoLogger("==>> CONNECT {} times, method:{}, url:{}, headers:{}, formMap:{}, result:{}",
                times, HttpMethod.GET, url, headers, paramMap, result);

        return result;
    }

    /**
     * POST 请求
     *
     * @param url           url
     * @param charset       charset
     * @param headers       headers
     * @param cookies       cookies
     * @param formMap       formMap
     * @param body          body
     * @param requestConfig requestConfig
     * @param times         times
     * @return HttpResult
     */
    private static HttpResult<String> httpPost(String url, String charset, Map<String, String> headers, List<BasicClientCookie> cookies,
                                               Map<String, String> formMap, String body, RequestConfig requestConfig, int times) {
        // HttpPost
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        setHeaders(httpPost, headers);

        // 表单参数
        if (formMap != null && formMap.size() > 0) {
            List<NameValuePair> list = postPutFinalParams(HttpMethod.POST.getValue(), formMap);
            if (list != null && list.size() > 0) {
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(list, charset));
                } catch (UnsupportedEncodingException e) {
                    if (openLogger) {
                        log.error(e.getMessage());
                    }
                }
            }
        }

        // body参数
        if (body != null && body.trim().length() > 0) {
            httpPost.setEntity(new StringEntity(body, charset));
        }

        CloseableHttpClient httpClient = getHttpClient(url, cookies);
        HttpResult<String> result = response(httpClient, httpPost, charset);

        printInfoLogger("==>> CONNECT {} times, method:{}, url:{}, headers:{}, formMap:{}, body:{}, result:{}",
                times, HttpMethod.POST, url, headers, formMap, body, result);

        return result;
    }

    /**
     * put 请求
     *
     * @param url           url
     * @param charset       charset
     * @param headers       headers
     * @param cookies       cookies
     * @param body          body
     * @param requestConfig requestConfig
     * @param times         times
     * @return HttpResult
     */
    private static HttpResult<String> httpPut(String url, String charset, Map<String, String> headers, List<BasicClientCookie> cookies,
                                              String body, RequestConfig requestConfig, int times) {
        // HttpPut
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(requestConfig);
        setHeaders(httpPut, headers);

        // json参数
        if (body != null && body.trim().length() > 0) {
            httpPut.setEntity(new StringEntity(body, charset));
        }

        CloseableHttpClient httpClient = getHttpClient(url, cookies);
        HttpResult<String> result = response(httpClient, httpPut, charset);

        printInfoLogger("==>> CONNECT {} times, method:{}, url:{}, headers:{}, body:{}, result:{}",
                times, HttpMethod.PUT, url, headers, body, result);


        return result;
    }

    /**
     * delete 请求
     *
     * @param url           url
     * @param charset       charset
     * @param headers       headers
     * @param cookies       cookies
     * @param paramMap      paramMap
     * @param requestConfig requestConfig
     * @param times         times
     * @return HttpResult
     */
    private static HttpResult<String> httpDelete(String url, String charset, Map<String, String> headers, List<BasicClientCookie> cookies,
                                                 Map<String, String> paramMap, RequestConfig requestConfig, int times) {

        // realUrl
        String realUrl = realUrl(HttpMethod.DELETE, url, paramMap);

        // HttpDelete
        HttpDelete httpDelete = new HttpDelete(realUrl);
        httpDelete.setConfig(requestConfig);
        setHeaders(httpDelete, headers);

        //httpClient
        CloseableHttpClient httpClient = getHttpClient(url, cookies);

        HttpResult<String> result = response(httpClient, httpDelete, charset);

        printInfoLogger("==>> CONNECT {} times, method:{}, url:{}, headers:{}, paramMap:{}, result:{}",
                times, HttpMethod.DELETE, realUrl, headers, paramMap, result);

        return result;
    }

    /**
     * 设置 headers
     *
     * @param request request
     * @param headers headers
     */
    private static void setHeaders(final HttpUriRequest request, final Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 实际请求路径
     * GET 或 DELETE 方法，将参数拼接至URL
     * POST 或 PUT 方法 ，URL无需修改
     *
     * @param httpMethod httpMethod
     * @param url        url
     * @param forms      forms
     * @return String
     */
    @SuppressWarnings("all")
    private static String realUrl(HttpMethod httpMethod, final String url, final Map<String, String> paramMap) {
        StringBuilder realUrl = new StringBuilder(url);

        // HttpUtil.GET 或 HttpUtil.DELETE 方法
        if (HttpMethod.GET.equals(httpMethod) || HttpMethod.DELETE.equals(httpMethod)) {
            String strWenHao = "?";
            String strYu = "&";
            if (!url.contains(strWenHao)) {
                realUrl.append(strWenHao);
            } else {
                // 最后一个字母不为?
                if (url.lastIndexOf(strWenHao) < url.length() - 1) {
                    realUrl.append(strYu);
                }
            }
            if (paramMap != null && paramMap.size() > 0) {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    realUrl.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue()))
                            .append(strYu);
                }
            }
            realUrl.deleteCharAt(realUrl.length() - 1);
        }
        return realUrl.toString();
    }

    /**
     * 参数转化
     * POST 或 PUT 方法 将表单参数转化为 List<NameValuePair>
     *
     * @param method method
     * @param forms  forms
     * @return List
     */
    private static List<NameValuePair> postPutFinalParams(final String method, final Map<String, String> forms) {
        if (HttpMethod.POST.getValue().equalsIgnoreCase(method) || HttpMethod.PUT.getValue().equalsIgnoreCase(method)) {
            if (forms != null && forms.size() > 0) {
                Iterator<Map.Entry<String, String>> iterator = forms.entrySet().iterator();
                List<NameValuePair> list = new ArrayList<>();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                return list;
            }
        }
        return null;
    }


    private static SSLContext createIgnoreVerifySSL() {
        // 创建套接字对象
        SSLContext sslContext;
        try {
            //指定TLS版本
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("[创建套接字失败:] " + e.getMessage());
        }

        // 实现X509TrustManager接口，用于绕过验证
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            //初始化sslContext对象
            sslContext.init(null, new TrustManager[]{trustManager}, null);
        } catch (KeyManagementException e) {
            throw new RuntimeException("[初始化套接字失败:] " + e.getMessage());
        }
        return sslContext;
    }


    /**
     * 执行请求，获取响应
     *
     * @param httpClient CloseableHttpClient
     * @param request    HttpUriRequest
     * @param charset    charset
     * @return HttpResult
     */
    private static HttpResult<String> response(CloseableHttpClient httpClient, HttpUriRequest request, String charset) {
        CloseableHttpResponse response = null;
        HttpResult<String> result = new HttpResult<>();
        try {
            // 执行请求
            response = httpClient.execute(request);

            // 获取响应状态
            StatusLine statusLine = response.getStatusLine();
            result.setCode(statusLine.getStatusCode());
            result.setMessage(statusLine.getReasonPhrase());

            // 获取返回实体
            HttpEntity entity = response.getEntity();
            // 获取响应数据，指定编码格式
            result.setData(EntityUtils.toString(entity, charset));
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            printErrorLogger("HTTP 请求异常", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                printErrorLogger(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 转换HttpResult
     *
     * @param result result
     * @param clazz  clazz
     * @param <E>    E
     * @return HttpResult
     */
    private static <E> HttpResult<E> convertHttpResult(HttpResult<String> result, Class<E> clazz) {
        if (clazz == null || clazz == String.class) {
            return (HttpResult<E>) result;
        } else {
            HttpResult<E> httpResult = new HttpResult<>();
            httpResult.setCode(result.getCode());
            httpResult.setMessage(result.getMessage());
            httpResult.setData(JSONObject.parseObject(result.getData(), clazz));
            return httpResult;
        }
    }

    private static void printInfoLogger(String message, Object... objects) {
        if (openLogger) {
            log.info(message, objects);
        }
    }

    private static void printErrorLogger(String message, Object... objects) {
        if (openLogger) {
            log.error(message, objects);
        }
    }

    private static void printErrorLogger(String message, Throwable throwable) {
        if (openLogger) {
            log.error(message, throwable);
        }
    }


    /**
     * 根据 http 状态码，确定是否成功执行
     * 100~200 继续发送请求
     * 200~300 成功
     * 300~400 重定向
     * 400~500 未发现资源
     * 500~ 服务器内部错误
     *
     * @param code code
     * @return boolean
     */
    private static boolean success(int code) {
        int int200 = 200;
        int int300 = 300;
        return code >= int200 && code < int300;
    }

    //endregion

    private HttpUtil() {
    }
}


























