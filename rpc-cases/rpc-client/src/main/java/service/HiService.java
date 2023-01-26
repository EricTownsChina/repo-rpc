package service;

/**
 * desc:
 *
 * @author EricTowns
 * @date 2023-01-23 20:42
 */
public interface HiService {

    /**
     * 收到信息并响应
     *
     * @param msg 收到的信息
     * @return 响应的信息
     */
    String hello(String msg);

}
