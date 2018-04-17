package zz.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.zz.Bean.ShopCartListBean;
import zz.wohui.zz.utils.LogUtils;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/5/12 16:40
 * 版本：V1.0
 * 修改历史：
 */
public abstract class ShopCartListCallBack extends Callback<ShopCartListBean> {
    @Override
    public ShopCartListBean parseNetworkResponse(Response response) throws Exception {
        String json = response.body().string();
        LogUtils.outLog(json);
        return new Gson().fromJson(json, ShopCartListBean.class);
    }
}
