package whcs.wohui.zz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import whcs.wohui.zz.Bean.OrderDetailData;
import whcs.wohui.zz.myview.MyGridView;
import whcs.wohui.zz.myview.MyListViewInListView;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyUtils;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：订单列表Adapter
 * 作者：陈杰宇
 * 时间： 2016/1/31 15:12
 * 版本：V1.0
 * 修改历史：
 */
public class OrderListAdapter extends MyBaseAdapter<OrderDetailData> implements View.OnClickListener {
    public final String TAG = "OrderListAdapter";
    public OrderListAdapter(List<OrderDetailData> dataList, Context ctx, MyItemBtnClickCallback callback) {
        super(dataList, ctx);
        this.mCallBack = callback;
    }


    private int orderState;
    private MyItemBtnClickCallback mCallBack;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_order_tab_all_order, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        OrderDetailData orderRowEntity = dataList.get(position);
        orderState = orderRowEntity.getO_Status();
        int commentState = orderRowEntity.getO_CommentStatus();
        vh.tvTime.setText(MyUtils.switchTimeFormat(orderRowEntity.getO_ArrivaTime()));
        vh.tvGoodsNum.setText(orderRowEntity.getDetail().size() + "");
        vh.tvFactMoney.setText("¥" + orderRowEntity.getO_TotalPrice());
        vh.tvOrderIntegral.setText(orderRowEntity.getO_TotalXFJJ()+"");
        LogUtils.e("orderState:"+orderState);
        if (commentState == 10){
            vh.btnEvaluate.setText("评论");
        }else {
            vh.btnEvaluate.setText("查看评论");
        }
        switch (orderState) {
            case 10://未付款
                vh.tvIsSuccess.setText("未付款");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnConfirm.setVisibility(View.VISIBLE);
                vh.btnRefund.setVisibility(View.GONE);
                vh.btnConfirm.setText("去付款");
                break;
            case 20://已付款
                vh.tvIsSuccess.setText("已付款");
                vh.btnEvaluate.setVisibility(View.GONE);

                vh.btnConfirm.setVisibility(View.GONE);
                // TODO: 2017/1/13 通过时间差判断,是否可以取消订单,更改时间
                int timeBetween = MyUtils.minBetween(orderRowEntity.getO_ArrivaTime());
                LogUtils.e("timeBetween"+timeBetween);
                if (timeBetween>30){
                    vh.btnRefund.setVisibility(View.GONE);
                    vh.btnRefund.setText("取消订单");
                }else {
                    vh.btnRefund.setVisibility(View.GONE);
                }
                break;
//            case 30://超市配货
//                vh.tvIsSuccess.setText("配货中");
//                vh.btnEvaluate.setVisibility(View.GONE);
//                vh.btnRefund.setVisibility(View.GONE);
//                vh.btnConfirm.setVisibility(View.GONE);
//                break;
            case 30://配货中
                vh.tvIsSuccess.setText("配货中");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnConfirm.setVisibility(View.GONE);
                if (orderRowEntity.isReceive()){
                    vh.btnRefund.setClickable(true);
                    vh.btnRefund.setVisibility(View.GONE);
                    vh.btnRefund.setText("申请退款");
                }else {
                    vh.btnRefund.setVisibility(View.GONE);
                }
                break;
            case 31://配送中
                vh.tvIsSuccess.setText("配送中");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnConfirm.setVisibility(View.GONE);
                break;
            case 32://已送达
                vh.tvIsSuccess.setText("已送达");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnConfirm.setVisibility(View.VISIBLE);
                vh.btnConfirm.setText("确认收货");
                vh.btnRefund.setVisibility(View.GONE);
                vh.btnRefund.setText("申请退款");
                break;
            case 40://已签收
                vh.tvIsSuccess.setText("已签收");
                vh.btnEvaluate.setVisibility(View.VISIBLE);
                vh.btnConfirm.setVisibility(View.GONE);
                vh.btnRefund.setVisibility(View.GONE);
                break;
            case 50://已取消
                vh.tvIsSuccess.setText("已取消");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnRefund.setText("查看进度");
                vh.btnConfirm.setVisibility(View.GONE);
                vh.btnRefund.setVisibility(View.GONE);
                break;
            case 60://已删除
                vh.tvIsSuccess.setText("已删除");
                vh.btnConfirm.setVisibility(View.GONE);
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnRefund.setText("申请售后");
                vh.btnRefund.setVisibility(View.GONE);
                break;
            case 70:
                vh.tvIsSuccess.setText("退款处理中");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnRefund.setVisibility(View.GONE);
                vh.btnConfirm.setVisibility(View.GONE);
                vh.btnEvaluate.setText("等待退款");
                vh.btnRefund.setClickable(false);
                break;
            case 80:
                vh.tvIsSuccess.setText("退款完成");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnRefund.setVisibility(View.VISIBLE);
                vh.btnConfirm.setVisibility(View.GONE);
                vh.btnRefund.setText("退款完成");
                vh.btnRefund.setClickable(false);
                break;
            case 90:
                vh.tvIsSuccess.setText("交易关闭");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnConfirm.setVisibility(View.GONE);
                vh.btnRefund.setVisibility(View.GONE);
                break;
            default:
                vh.tvIsSuccess.setText("未知订单类型:"+orderRowEntity.getO_Status()+"");
                vh.btnEvaluate.setVisibility(View.GONE);
                vh.btnRefund.setVisibility(View.GONE);
                vh.btnConfirm.setVisibility(View.GONE);
                break;
        }

        if (vh.orderGridItemGoodsAdapter == null){
            vh.orderGridItemGoodsAdapter = new OrderGridItemGoodsAdapter(orderRowEntity.getDetail(),ctx);
            vh.gvOrderItemGoods.setAdapter(vh.orderGridItemGoodsAdapter);
        }else {
            vh.orderGridItemGoodsAdapter.setDataList(orderRowEntity.getDetail());
        }
        vh.ivDelOrder.setOnClickListener(this);
        vh.ivDelOrder.setTag(position);
        vh.btnConfirm.setOnClickListener(this);
        vh.btnConfirm.setTag(position);
        vh.btnRefund.setOnClickListener(this);
        vh.btnRefund.setTag(position);
        vh.btnEvaluate.setOnClickListener(this);
        vh.btnEvaluate.setTag(position);
        return convertView;
    }
    /**
     * 自定义接口用于回调按钮点击事件到OrderFragment
     */
    public interface MyItemBtnClickCallback {
        void itemBtnClick(View v);
    }

    @Override
    public void onClick(View v) {
        mCallBack.itemBtnClick(v);
    }

    private class ViewHolder {
        public final TextView tvIsSuccess;
        public final TextView tvTime;
        public final ImageView ivDelOrder;
        public final MyListViewInListView lvGoods;
        public final TextView tvGoodsNum;
        public final TextView tvFactMoney;
        public final TextView tvOrderIntegral;
        public final Button btnEvaluate;
        public final Button btnRefund;
        public final Button btnConfirm;
        public final MyGridView gvOrderItemGoods;
        public final View root;

        public OrderListItemGoodsAdapter orderListItemGoodsAdapter;
        public OrderGridItemGoodsAdapter orderGridItemGoodsAdapter;

        public ViewHolder(View root) {
            tvIsSuccess = (TextView) root.findViewById(R.id.tvIsSuccess);
            tvTime = (TextView) root.findViewById(R.id.tvTime);
            ivDelOrder = (ImageView) root.findViewById(R.id.ivDelOrder);
            lvGoods = (MyListViewInListView) root.findViewById(R.id.lvGoods);
            tvGoodsNum = (TextView) root.findViewById(R.id.tvGoodsNum);
            tvFactMoney = (TextView) root.findViewById(R.id.tvFactMoney);
            tvOrderIntegral = (TextView) root.findViewById(R.id.tvOrderIntegral);
            btnEvaluate = (Button) root.findViewById(R.id.btnEvaluate);
            btnRefund = (Button) root.findViewById(R.id.btnRefund);
            btnConfirm = (Button) root.findViewById(R.id.btnConfirm);
            gvOrderItemGoods = (MyGridView) root.findViewById(R.id.gv_order_goods);
            this.root = root;
        }
    }
}
