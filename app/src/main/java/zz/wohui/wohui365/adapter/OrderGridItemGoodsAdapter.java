package zz.wohui.wohui365.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import zz.wohui.wohui365.Bean.OrderDetailData;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/7/6 15:04
 * 版本：V1.0
 * 修改历史：
 */
public class OrderGridItemGoodsAdapter extends MyBaseAdapter<OrderDetailData.DetailEntity>{
    //用于图片加载
    private DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;
    public OrderGridItemGoodsAdapter(List<OrderDetailData.DetailEntity> dataList, Context ctx) {
        super(dataList, ctx);
        mImageLoader= ImageLoader.getInstance();
        mImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.drawable.icon_logo_image_default)
                .showImageOnLoading(R.drawable.icon_logo_image_default)
                .showImageOnFail(R.drawable.icon_logo_image_default)
                .build();
    }
    public void setDataList(List<OrderDetailData.DetailEntity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            convertView = View.inflate(ctx,R.layout.item_order_goods,null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvOrderItemGoods.setText(dataList.get(position).getCS_Name());
        String imgUrl = dataList.get(position).getCS_MainImg();
        LogUtils.e("NearbyShopAdapter"+imgUrl);
        mImageLoader.displayImage(imgUrl, vh.ivOrderItemGoods, mImageOptions);//给商品设值图片
        return convertView;
    }
    private class ViewHolder {
        public final ImageView ivOrderItemGoods;
        public final TextView tvOrderItemGoods;
        public final View root;
        public ViewHolder(View root) {
            ivOrderItemGoods = (ImageView) root.findViewById(R.id.iv_order_item_goods);
            tvOrderItemGoods = (TextView) root.findViewById(R.id.tv_order_item_goods);
            this.root = root;
        }
    }
}
