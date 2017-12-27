package hello;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class ConfigClientApplication {

  public static void main(String[] args) {
      SpringApplication.run(ConfigClientApplication.class, args);
  }
}

@RestController
@RibbonClient(name = "a-bootiful-client")
class MessageRestController {

    @Value("${message:Hello default}")
    private String message;

    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
      return new RestTemplate() {{
        setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder
          .create()
          .setConnectionManager(new PoolingHttpClientConnectionManager() {{
            setDefaultMaxPerRoute(20);
            setMaxTotal(200);
          }}).build()));
      }};
    }
    
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/message")
    String getMessage() {
      return this.restTemplate.getForObject("http://a-bootiful-client/message", String.class);
    }
}
