package zz.wohui.wohui365.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zz.wohui.wohui365.Bean.ABCGoodsSortData;
import zz.wohui.wohui365.R;

/**
 * 说明：商家详情中商品种类一级列表适配器
 * 作者：陈杰宇
 * 时间： 2016-2-24 15:47:48
 * 版本：V1.0
 * 修改历史：
 */
public class GoodsACategoryListAdapter extends MyBaseAdapter<ABCGoodsSortData> {

    private int checkId;

    public GoodsACategoryListAdapter(List<ABCGoodsSortData> dataList, Context ctx,String shopSerialNumber) {
        super(dataList, ctx);
    }

    public void setCheckID(int checkId) {
        this.checkId = checkId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(super.ctx, R.layout.item_goods_category_a_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.goodsCategoryName.setText(dataList.get(position).getCC_Name());

        if (checkId != -1) {
            if (checkId == position) {
                convertView.setBackgroundResource(R.drawable.goods_category_list_bg_select);
                vh.goodsCategoryName.setTextColor(ctx.getResources().getColorStateList(R.color.mainColor));
            } else {
                convertView.setBackgroundResource(R.drawable.goods_category_list_bg_normal);
                vh.goodsCategoryName.setTextColor(ctx.getResources().getColorStateList(R.color.color_666));
            }
        } else {
            convertView.setBackgroundResource(R.drawable.goods_category_list_bg_normal);
            vh.goodsCategoryName.setTextColor(ctx.getResources().getColorStateList(R.color.color_666));
        }

        return convertView;
    }

    private class ViewHolder {
        public final TextView goodsCategoryName;
        public final View root;

        public ViewHolder(View root) {
            goodsCategoryName = (TextView) root.findViewById(R.id.goodsCategoryName);
            this.root = root;
        }
    }
}
