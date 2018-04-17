package zz.wohui.wohui365.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import zz.wohui.wohui365.Bean.GoodsListBean;
import zz.wohui.wohui365.utils.LogUtils;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/18 15:49
 * 版本：V1.0
 * 修改历史：
 */
public abstract class GoodsListCallBack extends Callback<GoodsListBean>{

    @Override
    public GoodsListBean parseNetworkResponse(Response response) throws Exception {
            String str = response.body().string();
        LogUtils.e("网络请求返回数据:"+str);
        return new Gson().fromJson(str,GoodsListBean.class);
    }
}
