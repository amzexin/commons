package io.github.amzexin.springcloudstudy.passportcaller.web.controller;

import io.github.amzexin.springcloudstudy.passport.api.dto.CommonResult;
import io.github.amzexin.springcloudstudy.passport.api.dto.QueryUserRequestDTO;
import io.github.amzexin.springcloudstudy.passport.api.service.IUserService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LoadBalanceTestController {

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @Resource(name = "loadBalancedRestTemplate")
    private RestTemplate loadBalancedRestTemplate;

    @Resource
    private IUserService userService;

    private static final String SERVICE_ID = "user-service";

    /**
     * 使用RestTemplate完成远程调用
     * 缺点：
     * 1、比较原始的调用方法，需要自己实现负载
     * 2、需要工程师自己拼装URL
     *
     * @param userId 用户唯一编号
     * @return 用户信息
     */
    @GetMapping("/load-balance-test/case1/user/get/{userId}")
    public Object getUserV1(@PathVariable("userId") int userId) {
        // 获取服务所有可用的实例
        List<ServiceInstance> instances = discoveryClient.getInstances(SERVICE_ID);

        // 实现负载，次数默认取第一个
        ServiceInstance serviceInstance = instances.get(0);

        // 组装url
        String url = String.format("%s/user/get/%d", serviceInstance.getUri(), userId);

        // 调用实际服务，并返回结果
        return restTemplate.getForObject(url, CommonResult.class);
    }

    /**
     * 使用RestTemplate + @LoadBalanced完成远程调用
     * spring-cloud-loadbalancer解析服务名，完成负载，工程师不需要自己实现负载
     * 缺点：需要工程师自己拼装URL
     *
     * @param userId 用户唯一编号
     * @return 用户信息
     */
    @GetMapping("/load-balance-test/case2/user/get/{userId}")
    public Object getUserV2(@PathVariable("userId") int userId) {
        String url = String.format("http://%s/user/get/%d", SERVICE_ID, userId);
        return loadBalancedRestTemplate.getForObject(url, CommonResult.class);
    }

    /**
     * 真正实现微服务，工程师只需要引入Jar，需要用到的地方注入该service，即可实现远程调用
     * 负载和http请求被屏蔽
     *
     * @param userId 用户唯一编号
     * @return 用户信息
     */
    @GetMapping("/load-balance-test/case3/user/get/{userId}")
    public Object getUserV3(@PathVariable("userId") int userId) {
        return userService.getUser(userId);
    }

    /**
     * GET请求被莫名其妙转成POST请求方式，问题定位与修复：
     * GET请求参数在Body中，HttpURLConnection（Feign默认使用的请求方式）会自动将GET请求转换成POST请求
     * 源码详见下面两个地方：
     * com.sun.javafx.runtime.async.AbstractRemoteResource.java call 89
     * sun.net.www.protocol.http.HttpURLConnection.java getOutputStream0 871
     * <p>
     * 解决这个问题，需要让Feign使用HttpClient，详见：
     * https://www.jianshu.com/p/d063c40df8d6?utm_campaign
     *
     * @return 用户信息
     */
    @GetMapping("/load-balance-test/case1/user/query")
    public Object queryUser() {
        QueryUserRequestDTO requestDTO = new QueryUserRequestDTO();
        requestDTO.setUsername("xxx");
        return userService.queryUser(requestDTO);
    }
}
