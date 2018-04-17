package zz.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.zz.Bean.GoodsSortBean;
import zz.wohui.zz.utils.LogUtils;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/21 14:56
 * 版本：V1.0
 * 修改历史：
 */
public abstract class GoodsSortCallBack extends Callback<GoodsSortBean>{

    @Override
    public GoodsSortBean parseNetworkResponse(Response response) throws Exception {
        LogUtils.e("网络请求返回    +++++++++++++++");
        String str = response.body().string();
        return new Gson().fromJson(str,GoodsSortBean.class);
    }
}
