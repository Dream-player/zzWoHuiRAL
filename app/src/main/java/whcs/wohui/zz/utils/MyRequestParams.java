package whcs.wohui.zz.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：封装Post请求参数的类
 * 作者：陈杰宇
 * 时间： 2016/1/23 16:34
 * 版本：V1.0
 * 修改历史：
 */
public class MyRequestParams {

    private static Map<String, String> queryStringParams;

    /**
     * 添加String类型的请求参数
     *
     * @param key   键
     * @param value 值
     */
    public void addStringRequest(String key, String value) {

        if (queryStringParams == null) {
            queryStringParams = new HashMap<>();
        }
        queryStringParams.put(key, value);

    }

    /**
     * 清空
     */
    public void clear() {
        if (queryStringParams != null) {
            queryStringParams.clear();
        }
    }

    /**
     * 获取到请求的参数列表
     *
     * @return 参数列表
     */
    public Map<String, String> getQueryStringParams() {
        return queryStringParams;
    }
}
