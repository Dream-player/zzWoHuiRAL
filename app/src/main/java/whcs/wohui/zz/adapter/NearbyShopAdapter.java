package whcs.wohui.zz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

import whcs.wohui.zz.Bean.NearbyShopBean;
import whcs.wohui.zz.activity.BDMapActivity;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/12 16:48
 * 版本：V1.0
 * 修改历史：
 */
public class NearbyShopAdapter extends MyBaseAdapter<NearbyShopBean.DataEntity> {
    //用于图片加载
    private DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;
    public NearbyShopAdapter(List<NearbyShopBean.DataEntity> dataList, Context ctx) {
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
            convertView = View.inflate(ctx, R.layout.item_shop, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        //第三部，给convertView充填数据
        vh = (ViewHolder) convertView.getTag();
        NearbyShopBean.DataEntity shopEntity = dataList.get(position);
        final double latitude = shopEntity.getS_Lat();
        final double longitude = shopEntity.getS_Long();
        vh.tvShopName.setText(shopEntity.getS_Name());
        vh.rbshopstar.setRating(shopEntity.getS_Score());
//        vh.rbshopstar.setProgress(shopEntity.getS_Score());
        vh.tvshopstar.setText(shopEntity.getS_Score()+"分");
        double distance = shopEntity.getDistance();
        if (distance<1000){
            vh.tvShopRanking.setText(distance+"m");
        }else {
            DecimalFormat df   = new DecimalFormat("######0.0");
            vh.tvShopRanking.setText(df.format(distance/1000)+"km");
        }
        setIsOpen(vh.tvIsOpen, shopEntity.isS_IsBusiness());//设置是否营业

        //TODO 给tvLocation添加监听
        vh.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, BDMapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                ctx.startActivity(intent);
            }
        });
        mImageLoader.displayImage(shopEntity.getS_HeadPhoto(),vh.ivShopHead,mImageOptions);//给商品设值图片

        return convertView;
    }

    /**
     * 设置是否营业中
     * @param tv 展示是否营业的tv组件
     * @param isOpen 是否营业中
     */
    private void setIsOpen(TextView tv,boolean isOpen){
        if (isOpen){
            tv.setText("营业中");
            tv.setTextColor(ctx.getResources().getColor(R.color.color_red));
        }else {
            tv.setText("停止营业");
            tv.setTextColor(ctx.getResources().getColor(R.color.userCenterMenuColor));
        }
    }
    public class ViewHolder {
        public final TextView tvShopName;
        public final TextView tvIsOpen;
        public final RatingBar rbshopstar;
        public final TextView tvshopstar;
        public final TextView tvShopRanking;
        public final TextView tvLocation;
        public final ImageView ivShopHead;
        public final View root;

        public ViewHolder(View root) {
            tvShopName = (TextView) root.findViewById(R.id.tvShopName);
            tvIsOpen = (TextView) root.findViewById(R.id.tvIsOpen);
            rbshopstar = (RatingBar) root.findViewById(R.id.rb_shop_stars);
            tvshopstar = (TextView) root.findViewById(R.id.tv_shop_star);
            tvShopRanking = (TextView) root.findViewById(R.id.tvShopRanking);
            tvLocation = (TextView) root.findViewById(R.id.tvLocation);
            ivShopHead = (ImageView) root.findViewById(R.id.iv_shop_head);
            this.root = root;
        }
    }
}
