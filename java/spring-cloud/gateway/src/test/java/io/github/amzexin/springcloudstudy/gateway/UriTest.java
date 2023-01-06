package io.github.amzexin.springcloudstudy.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.containsEncodedParts;

/**
 * UriTest
 *
 * @author Zexin Li
 * @date 2023-01-06 10:11
 */
public class UriTest {

    @Test
    public void filter() throws URISyntaxException {
        URI uri = new URI("https://public-domain.com/service-a/one");
        boolean encoded = containsEncodedParts(uri);
        URI routeUri = new URI("http://private-domain.com:8080/one/abc");

        URI mergedUrl = UriComponentsBuilder.fromUri(uri)
                // .uri(routeUri)
                .scheme(routeUri.getScheme()).host(routeUri.getHost())
                .port(routeUri.getPort()).build(encoded).toUri();
        System.out.println(mergedUrl);
    }
}
