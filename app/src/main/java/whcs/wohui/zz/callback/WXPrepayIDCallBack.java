package whcs.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.WXPrepayIDBean;
import whcs.wohui.zz.utils.LogUtils;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/5/14 9:48
 * 版本：V1.0
 * 修改历史：
 */
public abstract class WXPrepayIDCallBack extends Callback<WXPrepayIDBean>{
    @Override
    public WXPrepayIDBean parseNetworkResponse(Response response) throws Exception {
        String json = response.body().string();
        LogUtils.e("网络请求返回数据:" + json);
        return new Gson().fromJson(json,WXPrepayIDBean.class);
    }
}
