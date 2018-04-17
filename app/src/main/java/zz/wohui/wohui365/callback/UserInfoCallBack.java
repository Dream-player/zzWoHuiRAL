package zz.wohui.wohui365.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.wohui365.Bean.UserInfoBean;

/**
 * 说明：用户信息类返回数据CallBack类
 * 作者：陈杰宇
 * 时间： 2016/3/9 13:53
 * 版本：V1.0
 * 修改历史：
 */
public abstract class UserInfoCallBack extends Callback<UserInfoBean> {
    @Override
    public UserInfoBean parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        return new Gson().fromJson(str, UserInfoBean.class);
    }
}
