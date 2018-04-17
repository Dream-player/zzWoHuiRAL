package zz.wohui.wohui365.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.wohui365.Bean.OrderCommentBean;
import zz.wohui.wohui365.utils.LogUtils;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/13 11:09
 * 版本：V1.0
 * 修改历史：
 */
public abstract class OrderCommentCallBack extends Callback<OrderCommentBean>{
    @Override
    public OrderCommentBean parseNetworkResponse(Response response) throws Exception {
        String json = response.body().string();
        LogUtils.outLog(json);
        return new Gson().fromJson(json, OrderCommentBean.class);
    }
}
