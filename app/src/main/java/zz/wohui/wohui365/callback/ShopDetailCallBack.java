package zz.wohui.wohui365.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.wohui365.Bean.ShopDetailBean;
import zz.wohui.wohui365.utils.LogUtils;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/7 9:56
 * 版本：V1.0
 * 修改历史：
 */
public abstract class ShopDetailCallBack extends Callback<ShopDetailBean >{
    @Override
    public ShopDetailBean  parseNetworkResponse(Response response) throws Exception {
        String json = response.body().string();
        LogUtils.outLog(json);
        return new Gson().fromJson(json, ShopDetailBean .class);
    }
}
