package whcs.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.OrderCommentBean;
import whcs.wohui.zz.Bean.ShopCartListBean;
import whcs.wohui.zz.utils.LogUtils;

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
