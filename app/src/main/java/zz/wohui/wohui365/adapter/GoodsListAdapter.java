package zz.wohui.wohui365.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import zz.wohui.wohui365.Bean.GoodsListBean.DataEntity.GoodsEntity;
import zz.wohui.wohui365.activity.GoodsDetailActivity;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.ShopCartDatabaseHelper;
import zz.wohui.wohui365.MyApplication;
import zz.wohui.wohui365.R;

/**
 * 说明：商家详情中商品列表
 * 作者：陈杰宇
 * 时间： 2016-2-29 11:13:14
 * 版本：V1.0
 * 修改历史：
 */
public class GoodsListAdapter extends MyBaseAdapter<GoodsEntity> {

    private Activity mActivity;
    private TextView shopCart;
    private ImageView buyImg;

    private ViewGroup animMaskLayout;
    private List<Integer> goodsNum;

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }
    //用于图片加载
    private DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;
    private int buyNum;
    private MyApplication application;
    private ShopCartDatabaseHelper myDBHelper;
    public GoodsListAdapter(List<GoodsEntity> dataList
            , Context ctx,ShopCartDatabaseHelper myDBHelper) {
        super(dataList, ctx);

        this.myDBHelper = myDBHelper;
        initGoodsNum();
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

    /**
     * 初始化各个商品的购买数量
     */
    public void initGoodsNum() {
        int leng = dataList.size();
        goodsNum = new ArrayList();
        goodsNum.clear();
        for (int i = 0; i < leng; i++) {
            goodsNum.add(0);
        }
    }


    public void setShopCart(TextView shopCart) {
        this.shopCart = shopCart;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(super.ctx, R.layout.item_goods_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        GoodsEntity goodsDetail = dataList.get(position);
        vh.goodsCategoryName.setText(goodsDetail.getCS_Name());
        vh.tvGoodsSales.setText("销量："+goodsDetail.getSMC_SalesNum());
        vh.tvGoodsDescription.setText(goodsDetail.getCS_Name());
        vh.tvGoodsPrice.setText("¥"+goodsDetail.getSMC_UnitPrice());
        vh.tvGoodsIntegral.setText("返利：" + goodsDetail.getSMC_XFJJ());
        String imgUrl = goodsDetail.getCS_MainImg();
        LogUtils.e("商品图片路径："+imgUrl);
        mImageLoader.displayImage(imgUrl,vh.ivGoodsImage,mImageOptions);//给商品设值图片


        isSelected(goodsNum.get(position), vh);
        vh.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = dataList.get(position).getCS_Describe();
                String shopName = dataList.get(position).getCS_Name();
                String id= dataList.get(position).getCS_GUID();
                Intent intent = new Intent(ctx, GoodsDetailActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("shopName",shopName);
                intent.putExtra("id",id);
                ctx.startActivity(intent);
            }
        });

        vh.goodsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = dataList.get(position).getCS_Describe();
                String shopName = dataList.get(position).getCS_Name();
                String id= dataList.get(position).getCS_GUID();
                Intent intent = new Intent(ctx, GoodsDetailActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("shopName",shopName);
                intent.putExtra("id",id);
                ctx.startActivity(intent);
            }
        });
        vh.goodsPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = dataList.get(position).getCS_Describe();
                String shopName = dataList.get(position).getCS_Name();
                String id= dataList.get(position).getCS_GUID();
                Intent intent = new Intent(ctx, GoodsDetailActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("shopName",shopName);
                intent.putExtra("id",id);
                ctx.startActivity(intent);
            }
        });

        vh.ivGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer poi = goodsNum.get(position)+1;
                goodsNum.set(position,poi);
                // TODO: 2016/2/27 添加购物车
                int[] start_location = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(start_location);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                buyImg = new ImageView(mActivity);
                buyImg.setBackgroundResource(R.drawable.icon_goods_add_item);// 设置buyImg的图片
                buyNum++;
                setAnim(buyImg, start_location);// 开始执行动画
                mOnGoodsNunChangeListener.onNumAdd(position);
                isSelected(poi, vh);
            }
        });
        vh.ivGoodsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer poi = goodsNum.get(position);
                if (poi > 0) {
                    poi = poi - 1;
                    goodsNum.set(position,poi);
                    // TODO: 2016/2/27 删除购物车内容

                    buyNum--;
                    changeShopCart();
                    mOnGoodsNunChangeListener.onNumDecrease(position);
                } else {

                }
                isSelected(poi, vh);
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

    /**
     * 设置动画
     *
     * @param v              要显示的内容
     * @param start_location 坐标
     */
    private void setAnim(final View v, int[] start_location) {
        animMaskLayout = null;
        animMaskLayout = createAnimLayout();
        animMaskLayout.addView(v);// 把动画小球添加到动画层
        final View view = addViewToAnimLayout(animMaskLayout, v,
                start_location);
        int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
        shopCart.getLocationInWindow(end_location);// shopCart是那个购物车
        // 计算位移
        int endX = 0 - start_location[0] + 40;// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);
        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                changeShopCart();
            }
        });
    }

    /**
     * 修改购物车状态
     */
    private void changeShopCart() {
        if (buyNum > 0) {
            shopCart.setVisibility(View.VISIBLE);
            shopCart.setText(buyNum + "");
        } else {
            shopCart.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化动画图层
     *
     * @return
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) mActivity.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(mActivity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * 将View添加到动画图层
     *
     * @param vg
     * @param view
     * @param location
     * @return
     */
    private View addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public interface OnShopCartGoodsChangeListener {
        void onNumAdd(int position);
        void onNumDecrease(int position);
    }

    private OnShopCartGoodsChangeListener mOnGoodsNunChangeListener = null;

    public void setOnShopCartGoodsChangeListener(OnShopCartGoodsChangeListener e) {
        mOnGoodsNunChangeListener = e;
    }

    public void setGoodsNum(List<Integer> goodsNum) {
        this.goodsNum = goodsNum;
    }

    private class ViewHolder {
        public final ImageView ivGoodsImage;
        public final TextView goodsCategoryName;
        public final TextView tvGoodsSales;
        public final TextView tvGoodsDescription;
        public final LinearLayout goodsInfo;
        public final LinearLayout goodsPrice;
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
            goodsPrice = (LinearLayout) root.findViewById(R.id.goodsPrice);
            tvGoodsPrice = (TextView) root.findViewById(R.id.tvGoodsPrice);
            tvGoodsIntegral = (TextView) root.findViewById(R.id.tvGoodsIntegral);
            ivGoodsMinus = (ImageView) root.findViewById(R.id.ivGoodsMinus);
            tvGoodsSelectNum = (TextView) root.findViewById(R.id.tvGoodsSelectNum);
            ivGoodsAdd = (ImageView) root.findViewById(R.id.ivGoodsAdd);
            this.root = root;
        }
    }
}
