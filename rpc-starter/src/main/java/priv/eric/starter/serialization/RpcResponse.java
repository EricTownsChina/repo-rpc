package priv.eric.starter.serialization;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RpcResponse {

    private String status;
    private Object returnValue;
    private Map<String, String> headers = new HashMap<>();
    private Exception exception;
}
