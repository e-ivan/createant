package com.qnyy.re.base.enums;

/**
 *
 * @author lianghaifeng
 * @version Id: DataServer.java, v 0.1 2019.5.31 031 14:30 lianghaifeng Exp $$
 */
public interface DataAsyncServer {
    /**
     * 请求服务
     */
    void asyncOperate(MethodUrlEnum methodUrl, String key, String value, String type, boolean replace);
}
