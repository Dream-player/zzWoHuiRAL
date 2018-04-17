package zz.wohui.wohui365.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.wohui365.Bean.AddCartBean;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/18 17:36
 * 版本：V1.0
 * 修改历史：
 */
public abstract class AddCartCallBack extends Callback<AddCartBean>{
    @Override
    public AddCartBean parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        return new Gson().fromJson(str, AddCartBean.class);
    }
}
