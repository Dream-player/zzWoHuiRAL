package zz.wohui.wohui365.fragment;

import android.content.Context;
import android.view.View;

import zz.wohui.wohui365.MainActivity;
import zz.wohui.wohui365.R;

/**
 * 说明：个人中心导航页
 * 作者：陈杰宇
 * 时间： 2016/1/7 15:56
 * 版本：V1.0
 * 修改历史：
 */
public class ShopCarFragment extends BaseFragment{



    private Context ctx;
    private MainActivity activity;

    @Override
    public View initView() {
        ctx = ShopCarFragment.this.getContext();
        activity = (MainActivity) getActivity();
        View v = View.inflate(ctx, R.layout.shopcar_fragment, null);

        return v;
    }

    @Override
    public void initEachData() {

    }


}