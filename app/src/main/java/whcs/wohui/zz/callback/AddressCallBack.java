package whcs.wohui.zz.callback;

import android.location.Address;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.AddressBean;

/**
 * 说明：收货地址CallBacK类
 * 作者：陈杰宇
 * 时间： 2016/3/9 15:51
 * 版本：V1.0
 * 修改历史：
 */
public abstract class AddressCallBack extends Callback<AddressBean> {
    @Override
    public AddressBean parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        return new Gson().fromJson(str, AddressBean.class);
    }
}
