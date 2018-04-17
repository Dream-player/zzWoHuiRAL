package zz.wohui.wohui365.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/3/4 23:35
 * 版本：V1.0
 * 修改历史：
 */
public class  OrderTimeActiveView extends LinearLayout {
    private ImageView ivImage;
    private TextView tvOrderStatus;
    private ImageView ivOrderActive1;
    private ImageView ivOrderActive2;
    private ImageView ivOrderActive3;
    private ImageView ivOrderActive4;
    private TextView tvOrderActive1;
    private TextView tvOrderActive2;
    private TextView tvOrderActive3;
    private TextView tvOrderActive4;
    private TextView tvLineOne;
    private TextView tvLineTwo;
    private TextView tvLineThree;


    private void assignViews(View view) {
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        tvOrderStatus = (TextView) view.findViewById(R.id.tvOrderStatus);
        ivOrderActive1 = (ImageView) view.findViewById(R.id.ivOrderActive1);
        ivOrderActive2 = (ImageView) view.findViewById(R.id.ivOrderActive2);
        ivOrderActive3 = (ImageView) view.findViewById(R.id.ivOrderActive3);
        ivOrderActive4 = (ImageView) view.findViewById(R.id.ivOrderActive4);
        tvOrderActive1 = (TextView) view.findViewById(R.id.tvOrderActive1);
        tvOrderActive2 = (TextView) view.findViewById(R.id.tvOrderActive2);
        tvOrderActive3 = (TextView) view.findViewById(R.id.tvOrderActive3);
        tvOrderActive4 = (TextView) view.findViewById(R.id.tvOrderActive4);
        tvLineOne = (TextView) view.findViewById(R.id.tv_line_one);
        tvLineTwo = (TextView) view.findViewById(R.id.tv_line_two);
        tvLineThree = (TextView) view.findViewById(R.id.tv_line_three);
    }

    public OrderTimeActiveView(Context context) {
        super(context);
    }

    public OrderTimeActiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.order_status_view, this, true);
        assignViews(v);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.OrderTimeActiveView);
        int a = typedArray.getIndexCount();
        for (int i = 0; i < a; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.OrderTimeActiveView_orderNode:
                    initOrderNode(typedArray.getInteger(attr, 0));
                    break;
            }
        }
        typedArray.recycle();
    }

    private void initOrderNode(int integer) {
        switch (integer) {
            case 0:
                //待付款
                tvOrderActive1.setText("待付款");
                tvOrderActive2.setText("待接单");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(false);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(false);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("订单待付款");
                break;
            case 10:
                //待付款 = 10,
                tvOrderActive1.setText("待付款");
                tvOrderActive2.setText("待接单");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(false);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(false);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("等待付款");
                break;
            case 20:
                //已付款 = 20
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("待接单");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("支付完成");
                break;
            case 30:
                // 已接单 = 30,
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("配货中");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("超市配货");
                break;
            case 31:
                // 已接单 = 30,
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("已发货");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("已经发货");
                break;
            case 32:
                // 已接单 = 30,
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("已送达");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setEnabled(true);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setEnabled(true);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("货物送达");
                break;
            case 40:
                //已收货 = 40,
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("已配货");
                tvOrderActive3.setText("已收货");
                tvOrderActive4.setText("已完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setEnabled(true);
                ivOrderActive4.setEnabled(true);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setEnabled(true);
                tvOrderActive4.setEnabled(true);
                tvOrderStatus.setText("订单完成");
                break;
            case 50:
                //已取消 = 50
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("已取消");
//                tvOrderActive3.setText("已签收");
//                tvOrderActive4.setText("已完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setVisibility(GONE);
                ivOrderActive4.setVisibility(GONE);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setVisibility(GONE);
                tvOrderActive4.setVisibility(GONE);
                tvLineTwo.setVisibility(GONE);
                tvLineThree.setVisibility(GONE);
                tvOrderStatus.setText("订单取消");
                break;
            case 60:
                // 已删除= 60,
                tvOrderActive1.setText("待付款");
                tvOrderActive2.setText("待接单");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(false);
                ivOrderActive2.setEnabled(false);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(false);
                tvOrderActive2.setEnabled(false);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("订单已删除");
                break;
            case 70:
                // 退款处理中= 70,
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("已接单");
                tvOrderActive3.setText("等待商家确认");
                tvOrderActive4.setText("未退款");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setEnabled(true);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setEnabled(true);
                tvOrderActive3.setText("等待退款");
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("订单退款中");
                break;
            case 80:
                //退款完成 = 80,
                tvOrderActive1.setText("已付款");
                tvOrderActive2.setText("已接单");
                tvOrderActive3.setText("商家确认");
                tvOrderActive4.setText("已退款");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(true);
                ivOrderActive3.setEnabled(true);
                ivOrderActive4.setEnabled(true);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(true);
                tvOrderActive3.setEnabled(true);
                tvOrderActive4.setEnabled(true);
                tvOrderStatus.setText("退成");
                break;
            case 90:
                //待付款 = 90,
                tvOrderActive1.setText("待付款");
                tvOrderActive2.setText("待接单");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(false);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(false);
                tvOrderActive2.setEnabled(false);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("订单超时失效");
                break;
            default:
                tvOrderActive1.setText("待付款");
                tvOrderActive2.setText("待接单");
                tvOrderActive3.setText("待签收");
                tvOrderActive4.setText("未完成");
                ivOrderActive1.setEnabled(true);
                ivOrderActive2.setEnabled(false);
                ivOrderActive3.setEnabled(false);
                ivOrderActive4.setEnabled(false);
                tvOrderActive1.setEnabled(true);
                tvOrderActive2.setEnabled(false);
                tvOrderActive3.setEnabled(false);
                tvOrderActive4.setEnabled(false);
                tvOrderStatus.setText("位置状态订单");
                break;
        }
    }

    public OrderTimeActiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOrderActive(int active) {
        initOrderNode(active);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
