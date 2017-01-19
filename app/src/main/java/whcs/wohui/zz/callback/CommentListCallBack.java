package whcs.wohui.zz.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import whcs.wohui.zz.Bean.ShopCommentListBean;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/7 13:37
 * 版本：V1.0
 * 修改历史：
 */
public abstract class CommentListCallBack extends Callback<ShopCommentListBean>{
    @Override
    public ShopCommentListBean parseNetworkResponse(Response response) throws Exception {
        String str = response.body().string();
        return new Gson().fromJson(str,ShopCommentListBean.class);
    }
}
