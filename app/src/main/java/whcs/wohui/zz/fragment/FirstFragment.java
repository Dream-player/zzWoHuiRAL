package whcs.wohui.zz.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.adapter.CBPageAdapter;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.util.ArrayList;
import java.util.List;

import whcs.wohui.zz.service.LocationService;
import whcs.wohui.zz.utils.LogUtils;
import whcs.wohui.zz.utils.MyOkHttpUtils;
import whcs.wohui.zz.whcouldsupermarket.MainActivity;
import whcs.wohui.zz.whcouldsupermarket.MyApplication;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：首页
 * 作者：刘志海
 * 时间： 2016/4/12 15:56
 * 版本：
 * 修改历史：
 */
public class FirstFragment extends BaseFragment{



    private Context ctx;
    private MainActivity activity;
    private ConvenientBanner convenientBanner;
    private MyOkHttpUtils myOkHttpUtils;
    private BDLocation location;

    private double latitude;
    private double longitude;
    private LocationService locService;

    @Override
    public View initView() {
        ctx = FirstFragment.this.getContext();
        activity = (MainActivity) getActivity();
        View v = View.inflate(ctx, R.layout.shouye_fragment, null);
        convenientBanner = (ConvenientBanner) v.findViewById(R.id.convenientBanner);
        myOkHttpUtils = new MyOkHttpUtils(ctx);

        List<String> imageList = new ArrayList<>();
        imageList.add("http://img10.360buyimg.com/cms/jfs/t3163/17/1319630042/109911/4d61d2e0/57c94d2cN0928fc96.jpg");
        imageList.add("http://img11.360buyimg.com/cms/jfs/t3304/208/1257512630/85008/8158e0f/57c94d41Nb1910bf7.jpg");
        imageList.add("http://img11.360buyimg.com/cms/jfs/t3019/227/1696083172/101137/8f0d5551/57c94d5cN315fd854.jpg");
        imageList.add("http://img10.360buyimg.com/cms/jfs/t3301/335/1291165781/347071/c6d47d5c/57c94d72N0080bb9a.jpg");


        initListHead(imageList);
        initThisData();


        return v;
    }

    private void initThisData() {

        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//进行权限判断
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    33);
        }else {
            doLocation();
        }
    }

    @Override
    public void initEachData() {

    }

    private void initListHead(List<String> imageList) {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, imageList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.startTurning(3000);
        convenientBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                swipeRefLayout.setEnabled(false);
                LogUtils.outLog("checked");
                return true;
            }
        });

    }

    /**
     * convenientBanner加载图片
     */
    public class LocalImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        public void UpdateUI(Context context, final int position, String data) {
            myOkHttpUtils.downLoadImage(data, imageView);
        }
    }

    /**
     * 进行定位
     */
    private void doLocation() {
        showDialog(ctx);
        LogUtils.outLog("doLocation()");
        locService = ((MyApplication) activity.getApplication()).locationService;
        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        mOption.setCoorType("bd09ll");
        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        locService.start();
    }



    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDLocationListener listener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location1) {
            LogUtils.e("定位类型：" + location1.getLocType());

            if (location1 != null && (location1.getLocType() == 161 || location1.getLocType() == 66)) {
                location = location1;

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LogUtils.e("TAG","latitude和longitude是"+latitude+longitude);

//                EventBus.getDefault().post(location);
                Toast.makeText(ctx, latitude+"定位成功", Toast.LENGTH_SHORT).show();
                dismissDialog();
                ((MainActivity)getActivity()).setmLocation(location);

            } else {
                Toast.makeText(ctx, "定位失败", Toast.LENGTH_SHORT).show();
                dismissDialog();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 33) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doLocation();
            } else {
                showToast(ctx,"请设置允许定位权限!");
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.outLog("onDestroy");
        if (locService != null) {
            locService.unregisterListener(listener);
            locService.stop();
        }
    }
    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        if (locService != null) {
            locService.unregisterListener(listener);
            locService.stop();
        }
    }
}