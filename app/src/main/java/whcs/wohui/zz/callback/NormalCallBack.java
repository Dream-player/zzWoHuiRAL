package whcs.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.NormalBean;
import whcs.wohui.zz.utils.LogUtils;

/**
 * 说明：联网返回一般字符串的CallBack类
 * 作者：陈杰宇
 * 时间： 2016/3/8 11:14
 * 版本：V1.0
 * 修改历史：
 */
public abstract class NormalCallBack extends Callback<NormalBean> {
    @Override
    public NormalBean parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        LogUtils.outLog(string);
        NormalBean bean = new Gson().fromJson(string, NormalBean.class);
        return bean;
    }
}
