package zz.wohui.wohui365.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import zz.wohui.wohui365.Bean.DBgoodsData;
import java.util.List;


import zz.wohui.wohui365.R;

/**
 * 说明：购物车列表适配器
 * 作者：陈杰宇
 * 时间： 2016/2/27 16:48
 * 版本：V1.0
 * 修改历史：
 */
public class ShopCartAdapter extends MyBaseAdapter<DBgoodsData> {

    public ShopCartAdapter(List<DBgoodsData> dataList, Context ctx) {
        super(dataList, ctx);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_shop_cart, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        DBgoodsData data = dataList.get(position);
        vh.tvGoodsName.setText(data.getNameComm());
        vh.tvGoodsPrice.setText("¥"+data.getPriceUnit()+"");
        int count = data.getOrderCount();
        vh.tvGoodsSelectNum.setText(count+"");
        isSelected(count,vh);
        vh.ivGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加商品
                int goodsCount = dataList.get(position).getOrderCount()+1;
                dataList.get(position).setOrderCount(goodsCount);
                mOnGoodsNunChangeListener.onNumAdd(position);
                isSelected(goodsCount,vh);
            }
        });
        vh.ivGoodsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加商品
                int goodsCount = dataList.get(position).getOrderCount()-1;
                dataList.get(position).setOrderCount(goodsCount);
                mOnGoodsNunChangeListener.onNumDecrease(position);
                isSelected(goodsCount,vh);
            }
        });
        return convertView;
    }
    /**
     * 判断商品是否有添加到购物车中
     *
     * @param i  条目下标
     * @param vh ViewHolder
     */
    private void isSelected(int i, ViewHolder vh) {
        if (i == 0) {
            vh.tvGoodsSelectNum.setVisibility(View.GONE);
            vh.ivGoodsMinus.setVisibility(View.GONE);
        } else {
            vh.tvGoodsSelectNum.setVisibility(View.VISIBLE);
            vh.tvGoodsSelectNum.setText(i + "");
            vh.ivGoodsMinus.setVisibility(View.VISIBLE);
        }
    }
    public interface OnShopCartGoodsChangeListener {
        void onNumAdd(int position);
        void onNumDecrease(int position);
    }
    private OnShopCartGoodsChangeListener mOnGoodsNunChangeListener = null;

    public void setOnShopCartGoodsChangeListener(OnShopCartGoodsChangeListener e) {
        mOnGoodsNunChangeListener = e;
    }
    private class ViewHolder {
        public final ImageView ivGoodsMinus;
        public final TextView tvGoodsSelectNum;
        public final ImageView ivGoodsAdd;
        public final LinearLayout llIcon;
        public final TextView tvGoodsPrice;
        public final TextView tvGoodsName;
        public final TextView tvGoodsSpec;
        public final View root;

        public ViewHolder(View root) {
            ivGoodsMinus = (ImageView) root.findViewById(R.id.ivGoodsMinus);
            tvGoodsSelectNum = (TextView) root.findViewById(R.id.tvGoodsSelectNum);
            ivGoodsAdd = (ImageView) root.findViewById(R.id.ivGoodsAdd);
            llIcon = (LinearLayout) root.findViewById(R.id.llIcon);
            tvGoodsPrice = (TextView) root.findViewById(R.id.tvGoodsPrice);
            tvGoodsName = (TextView) root.findViewById(R.id.tvGoodsName);
            tvGoodsSpec = (TextView) root.findViewById(R.id.tvGoodsSpec);
            this.root = root;
        }
    }
}
