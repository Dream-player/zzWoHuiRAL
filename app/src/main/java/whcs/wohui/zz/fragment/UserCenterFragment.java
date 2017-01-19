package whcs.wohui.zz.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import whcs.wohui.zz.activity.UserInfoActivity;
import whcs.wohui.zz.activity.LoginActivity;
import whcs.wohui.zz.activity.MoreActivity;
import whcs.wohui.zz.activity.AddressManageActivity;
import whcs.wohui.zz.activity.UserCollectionActivity;
import whcs.wohui.zz.utils.MyKey;
import whcs.wohui.zz.whcouldsupermarket.MainActivity;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：个人中心导航页
 * 作者：陈杰宇
 * 时间： 2016/1/7 15:56
 * 版本：V1.0
 * 修改历史：
 */
public class UserCenterFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout rlUserUnLogin;
    private ImageView imageHeard;
    private TextView tvUserName;
    private TextView tvUserAbout;
    private LinearLayout llManageAddress;
    private LinearLayout llUserCollect;
    private LinearLayout llHelpAndFeedBack;
    private LinearLayout llMore;
    private RelativeLayout titleGoBack;
    private TextView titleName;
    private LinearLayout llImSeller;
    private LinearLayout llBasicInfo;

    private void assignViews(View view) {
        titleName = (TextView) view.findViewById(R.id.titleName);
        titleGoBack = (RelativeLayout) view.findViewById(R.id.titleGoBack);
        rlUserUnLogin = (RelativeLayout) view.findViewById(R.id.rlUserIsLogin);
        imageHeard = (ImageView) view.findViewById(R.id.imageHeard);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvUserAbout = (TextView) view.findViewById(R.id.tvUserAbout);
        llManageAddress = (LinearLayout) view.findViewById(R.id.llManageAddress);
        llUserCollect = (LinearLayout) view.findViewById(R.id.llUserCollect);
        llHelpAndFeedBack = (LinearLayout) view.findViewById(R.id.llHelpAndFeedBack);
        llMore = (LinearLayout) view.findViewById(R.id.llMore);
        llImSeller = (LinearLayout) view.findViewById(R.id.llImSeller);
        llBasicInfo = (LinearLayout) view.findViewById(R.id.llBasicInfo);
    }

    private Context ctx;
    private MainActivity activity;

    @Override
    public View initView() {
        ctx = UserCenterFragment.this.getContext();
        activity = (MainActivity) getActivity();
        View v = View.inflate(ctx, R.layout.usercenter_fragment, null);
        assignViews(v);
        initData();
        return v;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        rlUserUnLogin.setOnClickListener(this);
        llMore.setOnClickListener(this);
        llBasicInfo.setOnClickListener(this);
        llManageAddress.setOnClickListener(this);
        llUserCollect.setOnClickListener(this);
        llHelpAndFeedBack.setOnClickListener(this);
        titleGoBack.setVisibility(View.GONE);
        titleName.setText("我的");
    }

    @Override
    public void initEachData() {

    }

    @Override
    protected void onResumeLazy() {
        if (activity.isLogin()) {
            SharedPreferences sp = activity
                    .getSharedPreferences(MyKey.userConfig, Context.MODE_PRIVATE);
            String userName = sp.getString("userCard", "");
            tvUserName.setText(userName);
            tvUserAbout.setText("欢迎使用我惠云超");
        }
        super.onResumeLazy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rlUserIsLogin:
                if (activity.isLogin()) {
                    intent.setClass(ctx, UserInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    login(intent);
                }
                break;
            case R.id.llMore:
                intent.setClass(ctx, MoreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.llBasicInfo:
                if (activity.isLogin()) {
                    intent.setClass(ctx, UserInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    login(intent);
                }
                break;
            case R.id.llManageAddress:
                if (activity.isLogin()) {
                    intent.setClass(ctx, AddressManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    login(intent);
                }
                break;
            case R.id.llUserCollect:
                if (activity.isLogin()) {
                    intent.setClass(ctx, UserCollectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    login(intent);
                }
                break;
            case R.id.llHelpAndFeedBack:
                showToast(ctx,"该功能暂未开放,敬请期待!");
                break;
        }
    }



}