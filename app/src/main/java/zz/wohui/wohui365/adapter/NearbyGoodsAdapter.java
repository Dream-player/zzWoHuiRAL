package zz.wohui.zz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import zz.wohui.zz.Bean.GoodsListBean.DataEntity.GoodsEntity;
import zz.wohui.zz.utils.LogUtils;
import zz.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/12 16:48
 * 版本：V1.0
 * 修改历史：
 */
public class NearbyGoodsAdapter extends MyBaseAdapter<GoodsEntity> {
    //用于图片加载
    private DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;
    public NearbyGoodsAdapter(List<GoodsEntity> dataList, Context ctx) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //第一步，定义ViewHolder
        ViewHolder vh;
        //第二部，判读convView是否为空，为空则新建
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_goods_list1, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        //第三部，给convertView充填数据
        vh = (ViewHolder) convertView.getTag();
        GoodsEntity goodsDetail = dataList.get(position);
        vh.goodsCategoryName.setText(goodsDetail.getCS_Name());
        vh.tvGoodsSales.setText("销量："+goodsDetail.getSMC_SalesNum());
        vh.tvGoodsDescription.setText(goodsDetail.getCS_Name());
        vh.tvGoodsPrice.setText("¥"+goodsDetail.getSMC_UnitPrice());
        vh.tvGoodsIntegral.setText("返利：" + goodsDetail.getSMC_XFJJ());
        String imgUrl = goodsDetail.getCS_MainImg();
        LogUtils.e("商品图片路径："+imgUrl);
        mImageLoader.displayImage(imgUrl,vh.ivGoodsImage,mImageOptions);//给商品设值图片

        return convertView;
    }


    private class ViewHolder {
        public final ImageView ivGoodsImage;
        public final TextView goodsCategoryName;
        public final TextView tvGoodsSales;
        public final TextView tvGoodsDescription;
        public final LinearLayout goodsInfo;
        public final TextView tvGoodsPrice;
        public final TextView tvGoodsIntegral;
        public final ImageView ivGoodsMinus;
        public final TextView tvGoodsSelectNum;
        public final ImageView ivGoodsAdd;
        public final View root;

        public ViewHolder(View root) {
            ivGoodsImage = (ImageView) root.findViewById(R.id.ivGoodsImage);
            goodsCategoryName = (TextView) root.findViewById(R.id.goodsCategoryName);
            tvGoodsSales = (TextView) root.findViewById(R.id.tvGoodsSales);
            tvGoodsDescription = (TextView) root.findViewById(R.id.tvGoodsDescription);
            goodsInfo = (LinearLayout) root.findViewById(R.id.goodsInfo);
            tvGoodsPrice = (TextView) root.findViewById(R.id.tvGoodsPrice);
            tvGoodsIntegral = (TextView) root.findViewById(R.id.tvGoodsIntegral);
            ivGoodsMinus = (ImageView) root.findViewById(R.id.ivGoodsMinus);
            tvGoodsSelectNum = (TextView) root.findViewById(R.id.tvGoodsSelectNum);
            ivGoodsAdd = (ImageView) root.findViewById(R.id.ivGoodsAdd);
            this.root = root;
        }
    }
}
