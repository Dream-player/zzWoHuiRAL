package whcs.wohui.zz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import whcs.wohui.zz.Bean.OrderDetailData;
import whcs.wohui.zz.Bean.OrderListBean;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：订单列表中嵌套商品列表ListView的Adapter
 * 作者：陈杰宇
 * 时间： 2016/3/15 16:26
 * 版本：V1.0
 * 修改历史：
 */
public class OrderListItemGoodsAdapter extends MyBaseAdapter<OrderDetailData.DetailEntity> {


    public OrderListItemGoodsAdapter(List<OrderDetailData.DetailEntity> dataList, Context ctx) {
        super(dataList, ctx);
    }

    public void setDataList(List<OrderDetailData.DetailEntity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_order_list_goods, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvGoodsName.setText(dataList.get(position).getCS_Name());
        LogUtils.e("OrderListItemGoodsAdapter"+dataList.get(position).getCS_Name());
        vh.tvGoodsQuantity.setText(dataList.get(position).getOD_BuyNum()+"");
        return convertView;
    }

    private class ViewHolder {
        public final TextView tvGoodsName;
        public final TextView tvGoodsQuantity;
        public final View root;

        public ViewHolder(View root) {
            tvGoodsName = (TextView) root.findViewById(R.id.tvGoodsName);
            tvGoodsQuantity = (TextView) root.findViewById(R.id.tvGoodsQuantity);
            this.root = root;
        }
    }
}
