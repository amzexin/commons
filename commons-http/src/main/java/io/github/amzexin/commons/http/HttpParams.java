package io.github.amzexin.commons.http;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class HttpParams {

    private static final String CHARACTER_SET = "UTF-8";

    /**
     * 请求头
     */
    private final Map<String, String> headers = new HashMap<>(16);

    /**
     * cookie
     */
    private final List<BasicClientCookie> cookies = new ArrayList<>(16);

    /**
     * x-www-form-urlencode 编码格式的表单参数 POST 请求
     */
    private final Map<String, String> forms = new HashMap<>(16);

    /**
     * url 拼接参数 Get|Delete 请求
     */
    private final Map<String, String> params = new HashMap<>(16);

    /**
     * application/json 编码格式的参数 POST 请求
     */
    private String body;

    /**
     * 指从连接池获取连接的timeout, 默认1秒
     */
    private Integer connectionRequestTimeout = 1000;

    /**
     * 客户端和服务器建立连接的timeout, 默认3秒
     */
    private Integer connectionTimeout = 3000;

    /**
     * 客户端和服务器建立连接后， 客户端从服务器读取数据的timeout, 默认5秒
     */
    private Integer socketTimeout = 5000;

    /**
     * 请求重试次数, 默认1次
     */
    private Integer retryCount = 1;

    /**
     * 字符编码
     */
    private String charSet = CHARACTER_SET;

    public HttpParams setCharSet(String charSet) {
        this.charSet = charSet;
        return this;
    }

    /**
     * 设置请求头参数信息
     *
     * @param key   key
     * @param value value
     * @return HttpParams
     */
    public HttpParams setHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }


    /**
     * set headers
     *
     * @param headers headers
     * @return HttpParams
     */
    public HttpParams setHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * 设置cookie
     *
     * @param name   name
     * @param cookie cookie
     * @return HttpParams
     */
    public HttpParams setCookie(String name, String cookie) {
        return setCookie(name, cookie, null, null, null, null);
    }

    /**
     * 设置cookie
     *
     * @param name    name
     * @param cookie  cookie
     * @param domain  domain
     * @param path    path
     * @param version version
     * @param date    date
     * @return HttpParams
     */
    public HttpParams setCookie(String name, String cookie, String domain, String path, Integer version, Date date) {
        BasicClientCookie basicClientCookie = new BasicClientCookie(name, cookie);
        basicClientCookie.setDomain(domain);
        basicClientCookie.setPath(path == null || "".equals(path) ? "/" : path);
        basicClientCookie.setVersion(version == null ? 0 : version);
        basicClientCookie.setExpiryDate(date == null ? new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)) : date);
        cookies.add(basicClientCookie);
        return this;
    }

    /**
     * 填充cookie的domain（cookie必须设置domain才会生效）
     *
     * @param url
     */
    public void fillCookieDomain(String url) {
        if (cookies.isEmpty()) {
            return;
        }

        String domain = url.split("/")[2];
        for (BasicClientCookie cookie : cookies) {
            String cookieDomain = cookie.getDomain();
            if (cookieDomain == null || "".equals(cookieDomain)) {
                cookie.setDomain(domain);
            }
        }
    }

    /**
     * 设置表单参数信息
     *
     * @param key   key
     * @param value value
     * @return HttpParams
     */
    public HttpParams setForm(String key, String value) {
        forms.put(key, value);
        return this;
    }

    /**
     * 设置url拼接参数
     *
     * @param key   key
     * @param value value
     * @return HttpParams
     */
    public HttpParams setParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    /**
     * 设置从连接池获取连接超时时间, 单位:毫秒
     *
     * @param connectionRequestTimeout connectionRequestTimeout
     * @return HttpParams
     */
    public HttpParams setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    /**
     * 设置连接建立超时时间, 单位:毫秒
     *
     * @param connectionTimeout connectionTimeout
     * @return HttpParams
     */
    public HttpParams setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    /**
     * 设置客户端socket请求超时时间, 单位:毫秒
     *
     * @param socketTimeout socketTimeout
     * @return HttpParams
     */
    public HttpParams setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * 设置数据
     * form值为json，需要进行URLEncode编码
     *
     * @param key  key
     * @param json json
     * @return HttpParams
     */
    public HttpParams setFormJson(String key, String json) {
        // TODO: 2022/2/16  setFormJson
        try {
            forms.put(key, URLEncoder.encode(json, CHARACTER_SET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 设置Map POST请求
     *
     * @param map map
     * @return HttpParams
     */
    public HttpParams setFormByMap(Map<String, String> map) {
        this.forms.putAll(map);
        return this;
    }

    public HttpParams setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    /**
     * 设置对象
     * 反射，通过对象getter方法，获取对象属性，为form赋值
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("all")
    public HttpParams setFormByObj(Object obj) {
        Map<String, Object> map = getFieldValueMapByGetter(obj);
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            Object entryValue = entry.getValue();
            if (entryValue instanceof List) {
                List<String> collect = ((List<Byte>) entryValue).stream().map(Object::toString).collect(Collectors.toList());
                forms.put(entry.getKey(), String.join(",", collect));
            } else {
                forms.put(entry.getKey(), entry.getValue().toString());
            }
        }

        return this;
    }

    /**
     * 设置body参数
     *
     * @param body body
     * @return HttpParams
     */
    public HttpParams setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * 设置body
     *
     * @param obj obj
     * @return HttpParams
     */
    public HttpParams setBodyByObj(Object obj) {
        if (obj != null) {
            body = JSON.toJSONString(obj);
        }
        return this;
    }

    //region 私有方法
    private static Map<String, Object> getFieldValueMapByGetter(Object obj) {
        Map<String, Object> fieldValueMap = new HashMap<>(16);

        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String field = getFieldName(method);

            // Object 基类，存在 getter 方法 getClass，所以需要排除 class
            if (isExclude(field, "class")) {
                try {
                    Object value = method.invoke(obj);
                    if (value != null) {
                        fieldValueMap.put(field, value);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
        return fieldValueMap;
    }

    private static boolean isExclude(String field, String... excludes) {
        if (field != null && !"".equals(field)) {
            List<String> excludeList = new ArrayList<>();

            if (excludes != null && excludes.length > 0) {
                excludeList.addAll(Arrays.asList(excludes));
            }

            return !excludeList.contains(field);
        }
        return false;
    }

    private static String getFieldName(Method method) {
        String getStr = "get";
        String isStr = "is";
        String methodName = method.getName();

        // getter
        String get = methodName.substring(0, 3);
        String field = null;

        // is
        String is = methodName.substring(0, 2);
        if (getStr.equals(get)) {
            field = firstCharacterLower(methodName.substring(3));
        } else if (isStr.equals(is)) {
            field = firstCharacterLower(methodName.substring(2));
        }
        return field;
    }

    private static String firstCharacterLower(String str) {
        if (str != null && !"".equals(str)) {
            char c = str.charAt(0);
            return Character.toLowerCase(c) + str.substring(1);
        } else {
            return null;
        }
    }
    //endregion
}
