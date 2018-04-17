package zz.wohui.zz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zz.wohui.zz.utils.LogUtils;
import zz.wohui.zz.whcouldsupermarket.R;
import zz.wohui.zz.Bean.ShopCartListBean.DataEntity;

/**
 * 说明：确认订单页面 商品列表适配
 * 作者：陈杰宇
 * 时间： 2016/3/2 16:27
 * 版本：V1.0
 * 修改历史：
 */
public class ConfirmOrderAdapter extends MyBaseAdapter<DataEntity> {
    public ConfirmOrderAdapter(List<DataEntity> dataList, Context ctx) {
        super(dataList, ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_confirm_order_goods, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        LogUtils.e("shujuweizhi" + position);
        DataEntity data = dataList.get(position);
        vh.tvGoodsName.setText(data.getCS_Name());
        vh.tvGoodsNumber.setText("×" + data.getC_BuyNum());
        double d = data.getO_TotalPrice();
        vh.tvGoodsPrice.setText("¥"+d);
        return convertView;
    }
    private class ViewHolder{
        TextView tvGoodsName;
        TextView tvGoodsNumber;
        TextView tvGoodsPrice;
        View root;
        public ViewHolder(View root) {
            this.root = root;
            tvGoodsName = (TextView) root.findViewById(R.id.tv_goods_name);
            tvGoodsNumber = (TextView) root.findViewById(R.id.tv_goods_number);
            tvGoodsPrice = (TextView) root.findViewById(R.id.tv_goods_price);
        }
    }
}
