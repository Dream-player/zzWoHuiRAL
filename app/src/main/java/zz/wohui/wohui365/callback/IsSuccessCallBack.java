package zz.wohui.wohui365.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.wohui365.Bean.IsSuccessBean;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/11 9:18
 * 版本：V1.0
 * 修改历史：
 */
public abstract class IsSuccessCallBack extends Callback<IsSuccessBean>{


    @Override
    public IsSuccessBean parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        return new Gson().fromJson(str,IsSuccessBean.class);
    }
}
