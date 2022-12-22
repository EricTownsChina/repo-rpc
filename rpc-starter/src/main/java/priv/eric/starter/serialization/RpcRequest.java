package priv.eric.starter.serialization;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@ToString
public class RpcRequest implements Serializable {

    private String serviceName;
    private String methodName;
    private Map<String, String> headers = new HashMap<>();
    private Class<?>[] parameterTypes;
    private Object[] parameters;

}
