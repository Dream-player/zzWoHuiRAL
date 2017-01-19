package whcs.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.OrderListBean;
import whcs.wohui.zz.utils.LogUtils;

/**
 * 说明：订单列表CallBack
 * 作者：陈杰宇
 * 时间： 2016/3/12 10:01
 * 版本：V1.0
 * 修改历史：
 */
public abstract class OrderCallBack extends Callback<OrderListBean> {
    @Override
    public OrderListBean parseNetworkResponse(Response response) throws Exception {
        String json = response.body().string();
        LogUtils.outLog(json);
        return new Gson().fromJson(json, OrderListBean.class);
    }
}
