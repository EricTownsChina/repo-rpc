package priv.eric.starter.entity;

import lombok.Data;

@Data
public class ServiceInstanceInfo {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 接口名称
     */
    private String instanceId;
    /**
     * IP
     */
    private String ip;
    /**
     * 端口
     */
    private Integer port;
    /**
     * class对象
     */
    private Class<?> clazz;
    /**
     * bean对象
     */
    private Object bean;

    public ServiceInstanceInfo() {
    }

    private ServiceInstanceInfo(Builder builder) {
        this.serviceName = builder.serviceName;
        this.instanceId = builder.instanceId;
        this.ip = builder.ip;
        this.port = builder.port;
        this.clazz = builder.clazz;
        this.bean = builder.bean;
    }

    public static Builder n() {
        return new Builder();
    }

    public static final class Builder {
        private String serviceName;
        private String instanceId;
        private String ip;
        private Integer port;
        private Class<?> clazz;
        private Object bean;

        public Builder setServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder setInstanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder setClazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder setBean(Object bean) {
            this.bean = bean;
            return this;
        }

        public ServiceInstanceInfo build() {
            return new ServiceInstanceInfo(this);
        }
    }
}
