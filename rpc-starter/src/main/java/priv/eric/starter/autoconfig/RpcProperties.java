package priv.eric.starter.autoconfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "priv.eric.rpc")
public class RpcProperties {

    private String registerType;

    private String name;

    private String address;

    private Integer port = 2181;

    private String protocol = "java";
}
