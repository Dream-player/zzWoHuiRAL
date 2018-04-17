package zz.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.zz.Bean.AdListBean;
import zz.wohui.zz.utils.LogUtils;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/29 17:35
 * 版本：V1.0
 * 修改历史：
 */
public abstract class AdListCallBack extends Callback<AdListBean>{
    @Override
    public AdListBean parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        LogUtils.e("网络请求返回数据:" + str);
        return new Gson().fromJson(str,AdListBean.class);
    }
}
