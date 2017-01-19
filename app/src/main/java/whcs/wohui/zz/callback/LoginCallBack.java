package whcs.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.GoodsSortBean;
import whcs.wohui.zz.Bean.LoginBean;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/7 10:22
 * 版本：V1.0
 * 修改历史：
 */
public abstract class LoginCallBack extends Callback<LoginBean>{


    @Override
    public LoginBean parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        return new Gson().fromJson(str,LoginBean.class);
    }
}
