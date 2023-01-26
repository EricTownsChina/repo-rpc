package service.impl;

import org.springframework.stereotype.Service;
import priv.eric.cases.server.service.HelloService;
import priv.eric.starter.entity.annotation.ServiceRef;
import service.HiService;

/**
 * desc:
 *
 * @author EricTowns
 * @date 2023-01-23 21:38
 */
@Service
public class HiServiceImpl implements HiService {

    @ServiceRef
    private HelloService helloService;

    @Override
    public String hello(String msg) {
        String serverMsg = helloService.hello();
        return null;
    }
}
