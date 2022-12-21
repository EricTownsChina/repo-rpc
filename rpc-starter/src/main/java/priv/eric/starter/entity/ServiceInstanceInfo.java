package priv.eric.starter.entity;

import lombok.Data;

@Data
public class ServiceInstanceInfo {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 实例ID
     */
    private String instanceId;
    /**
     * IP
     */
    private String ip;
    /**
     * 端口
     */
    private String port;
    /**
     * class对象
     */
    private Class<?> clazz;
    /**
     * bean对象
     */
    private Object bean;
}
