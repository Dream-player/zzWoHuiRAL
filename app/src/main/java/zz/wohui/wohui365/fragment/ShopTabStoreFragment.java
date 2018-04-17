package zz.wohui.wohui365.fragment;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import zz.wohui.wohui365.Bean.ShopDetailBean;
import zz.wohui.wohui365.activity.ShopActivity;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.ShopCellectDBHelper;
import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * 说明：商家——商家详情
 * 作者：陈杰宇
 * 时间： 2016/1/29 11:16
 * 版本：V1.0
 * 修改历史：
 */
public class ShopTabStoreFragment extends BaseFragment {

    private Context ctx;

    private View v;
    private ShopCellectDBHelper myDBHelper;//收藏数据库工具类
    private String numberSM;
    private ShopDetailBean shopDetail;
    private TextView tvNameSM;
    private RatingBar ratingScoreSM;
    private ImageView ivCollect;
    private TextView tvCollect;
    private TextView tvShopNotification;
    private TextView tvBriefSM;
    private TextView tvBusinessMinMoney;
    private TextView tvBusinessRates;
    private TextView tvBusinessRange;
    private TextView tvBusinessHours;
    private TextView tvShopAddress;
    private TextView tvGradeGoods;
    private TextView tvShopPhone;
    private TextView tvShopQQ;
    private void assignViews() {
        tvBusinessMinMoney = (TextView) v.findViewById(R.id.tvBusinessMinMoney);
        tvBusinessRates = (TextView) v.findViewById(R.id.tvBusinessRates);
        tvBusinessRange = (TextView) v.findViewById(R.id.tvBusinessRange);
        tvNameSM = (TextView) v.findViewById(R.id.tvNameSM);
        ratingScoreSM = (RatingBar) v.findViewById(R.id.ratingScoreSM);
        tvBusinessHours = (TextView) v.findViewById(R.id.tvBusinessHours);
        ivCollect = (ImageView) v.findViewById(R.id.ivCollect);
        tvCollect = (TextView) v.findViewById(R.id.tvCollect);
        tvShopNotification = (TextView) v.findViewById(R.id.tvShopNotification);
        tvBriefSM = (TextView) v.findViewById(R.id.tv_BriefSM);
        tvShopAddress = (TextView) v.findViewById(R.id.tv_shop_address);
        tvGradeGoods = (TextView) v.findViewById(R.id.tvGradeGoods);
        tvShopPhone = (TextView) v.findViewById(R.id.tv_shop_phone);
        tvShopQQ = (TextView) v.findViewById(R.id.tv_shop_qq);
        LogUtils.e("f88888888888888888888888888888888");
    }

    private ShopActivity activity;


    @Override
    public View initView() {
        ctx = getContext();
        activity = (ShopActivity) ShopTabStoreFragment.this.getActivity();
        myDBHelper = new ShopCellectDBHelper(ctx);
        v = View.inflate(ctx, R.layout.shop_tab_fragment_store, null);
        assignViews();


        numberSM = activity.getShopSerialNo();
        shopDetail = activity.getShopDetail();
        setCollectState(myDBHelper.isCollect(numberSM));//设置界面收藏状态
        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtils.e("tvCollect+++++++");
                if (!myDBHelper.isCollect(numberSM)) {
                    myDBHelper.shopCollectAdd(shopDetail.getData());
                    setCollectState(true);
                    LogUtils.e("tvCollect++22222+");
                } else {
                    myDBHelper.shopCollectDecrease(numberSM);
                    setCollectState(false);
                    LogUtils.e("tvCollect+++444444++++");
                }
            }
        });

        if (shopDetail!=null){
            fillView(shopDetail);
        }
        return v;
    }




    private void setCollectState(boolean collectState){
        if(collectState){
            ivCollect.setBackgroundResource(R.drawable.shop_favored);
            tvCollect.setText("已收藏");
        }else {
            ivCollect.setBackgroundResource(R.drawable.shop_unfavored);
            tvCollect.setText("未收藏");
        }
    }
    /**
     * 通过传入超市详情参数，进行对控件的填充
     * @param shopDetail
     */
    private void fillView(ShopDetailBean shopDetail){

        ShopDetailBean.DataBean smRowEntity =
                shopDetail.getData();
        LogUtils.e(tvNameSM + "444444444444444");
        //超市名字
        ratingScoreSM.setRating(smRowEntity.getS_Score());
        tvGradeGoods.setText(smRowEntity.getS_Score()+"分");
        tvNameSM.setText(""+smRowEntity.getS_Name());
        //超市营业时间
        String beginMin = smRowEntity.getS_Business_BeginMin()+"";
        if (smRowEntity.getS_Business_BeginMin()<10){
            beginMin = "0"+smRowEntity.getS_Business_BeginMin();
        }
        String endMin = smRowEntity.getS_Business_EndMin()+"";
        if (smRowEntity.getS_Business_BeginMin()<10){
            endMin = "0"+smRowEntity.getS_Business_EndMin();
        }
        tvBusinessHours.setText(
                "配送时间："
                +smRowEntity.getS_Business_BeginHours()
                +":"
                +beginMin
                +"-"
                +smRowEntity.getS_Business_EndHours()
                +":"
                +endMin);
        //超市最新公告
        tvShopNotification.setText(""+smRowEntity.getSMN_Content());
        tvShopQQ.setText("QQ："+smRowEntity.getS_QQ());
        //商家信息
        tvBriefSM.setText(""+smRowEntity.getSMN_Content());
        tvShopPhone.setText(Html.fromHtml("电话："+smRowEntity.getS_LinkMan_Phone()));
        double range = smRowEntity.getS_BusinessRange();
        if (range<1000){
            tvBusinessRange.setText(Html.fromHtml("配送范围："+"<font color='red'>"+range+"m</font>"));
        }else {
            DecimalFormat df   = new DecimalFormat("######0.0");
            tvBusinessRange.setText(Html.fromHtml("配送范围："+"<font color='red'>"+df.format(range/1000)+"km</font>"));
        }

        tvBusinessMinMoney.setText(Html.fromHtml("起送价："+"<font color='red'>￥"+smRowEntity.getS_MinMoney()+"</font>"));
        tvBusinessRates.setText(Html.fromHtml("配送费："+"<font color='red'>￥"+smRowEntity.getS_Freight()+"</font>"));
        //超市地址

        tvShopAddress.setText("地址："+smRowEntity.getSI_Province()
                +smRowEntity.getSI_City()
                +smRowEntity.getSI_District()
                +smRowEntity.getSI_Towns()
                +smRowEntity.getSI_Address());
    }
    @Override
    public void initEachData() {

    }
}
