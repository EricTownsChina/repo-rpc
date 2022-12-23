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

    public RpcResponse() {}

    private RpcResponse(Builder builder) {
        this.status = builder.status;
        this.returnValue = builder.returnValue;
        this.headers = builder.headers;
        this.exception = builder.exception;
    }

    public static Builder success() {
        return new Builder().setStatus("SUCCESS");
    }

    public static Builder fail() {
        return new Builder().setStatus("FAIL");
    }

    public static class Builder {
        private String status;
        private Object returnValue;
        private Map<String, String> headers = new HashMap<>();
        private Exception exception;

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setReturnValue(Object returnValue) {
            this.returnValue = returnValue;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder addHeader(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setException(Exception exception) {
            this.exception = exception;
            return this;
        }

        public RpcResponse build() {
            return new RpcResponse(this);
        }
    }

}
