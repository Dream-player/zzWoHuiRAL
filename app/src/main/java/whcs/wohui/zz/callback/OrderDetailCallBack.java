package whcs.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.OrderDetailBean;
import whcs.wohui.zz.Bean.ShopDetailBean;
import whcs.wohui.zz.utils.LogUtils;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/11 9:32
 * 版本：V1.0
 * 修改历史：
 */
public abstract class OrderDetailCallBack extends Callback<OrderDetailBean>{

    @Override
    public OrderDetailBean parseNetworkResponse(Response response) throws Exception {
        String json = response.body().string();
        LogUtils.outLog(json);
        return new Gson().fromJson(json, OrderDetailBean.class);
    }
}
