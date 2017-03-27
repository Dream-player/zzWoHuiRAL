package whcs.wohui.zz.fragment;

import android.content.Context;
import android.view.View;

import whcs.wohui.zz.whcouldsupermarket.MainActivity;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：个人中心导航页
 * 作者：陈杰宇
 * 时间： 2016/1/7 15:56
 * 版本：V1.0
 * 修改历史：
 */
public class FirstFragment extends BaseFragment{



    private Context ctx;
    private MainActivity activity;

    @Override
    public View initView() {
        ctx = FirstFragment.this.getContext();
        activity = (MainActivity) getActivity();
        View v = View.inflate(ctx, R.layout.shouye_fragment, null);

        return v;
    }

    @Override
    public void initEachData() {

    }


}