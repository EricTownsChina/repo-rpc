package priv.eric.cases.server.service.impl;

import priv.eric.cases.server.service.HelloService;
import priv.eric.starter.entity.annotation.ServiceExpose;

/**
 * desc:
 *
 * @author EricTowns
 * @date 2023-01-21 21:51
 */
@ServiceExpose
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello() {
        return null;
    }

}
