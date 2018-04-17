package zz.wohui.wohui365.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.ABCGoodsSortData;
import zz.wohui.wohui365.Bean.BCGoodsSortData;
import zz.wohui.wohui365.Bean.GoodsSortBean;
import zz.wohui.wohui365.Bean.GoodsSortData;
import zz.wohui.wohui365.activity.ShopActivity;
import zz.wohui.wohui365.adapter.GoodsACategoryListAdapter;
import zz.wohui.wohui365.adapter.GoodsBCategoryListAdapter;
import zz.wohui.wohui365.callback.GoodsSortCallBack;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * 说明：商家商品页面
 * 作者：陈杰宇
 * 时间： 2016/2/28 11:18
 * 版本：V1.0
 * 修改历史：
 */
public class ShopTabGoodsFragment extends BaseFragment {

    private ListView lvFirstCategory;
    private ListView lvSecondCategory;
    private Context ctx;
    private ShopActivity activity;
    private List<GoodsSortData> goodsSortList;
    private List<GoodsSortData> goodsSortListA;
    private List<GoodsSortData> goodsSortListB;
    private List<GoodsSortData> goodsSortListC;
    private List<BCGoodsSortData> goodsSortListBC;
    private List<ABCGoodsSortData> goodsSortListABC;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;
    private static final int GET_GOODSSORT_SUCCESS = 11220;
    private static final int GET_GOODSSORT_FAILED = 8888;
    private String shopSerialNumber;

    private void assignViews(View v) {
        lvFirstCategory = (ListView) v.findViewById(R.id.lvFirstCategory);
        lvSecondCategory = (ListView) v.findViewById(R.id.lvSecondCategory);
    }
    @Override
    public View initView() {
        ctx = getContext();
        View v = View.inflate(ctx, R.layout.shop_tab_fragment_goods, null);
        assignViews(v);
        initData();
        initAList();
        initListener();
        return v;
    }
    private void initData(){

        activity = (ShopActivity)getActivity();
        goodsSortList = new ArrayList<>();
        goodsSortListA = new ArrayList<>();
        goodsSortListB = new ArrayList<>();
        goodsSortListC = new ArrayList<>();
        goodsSortListBC = new ArrayList<>();
        goodsSortListABC = new ArrayList<>();
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
    }
    /**
     * 网络请求获取超市列表参数
     * @param shopSerialNumber
     */
    private void getGoodsSort(String shopSerialNumber){
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey,activity.getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.GoodsSort_shopSerialNo, shopSerialNumber);
        String strUrl = Urls.GetGoodsSort;
        LogUtils.e(""+strUrl);
        myOkHttpUtils.postRequest(strUrl, params, new GoodsSortCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                LogUtils.e("网络请求失败+++"+e.toString());
                LogUtils.e(call.toString());
                handler.sendEmptyMessage(GET_GOODSSORT_FAILED);
            }

            @Override
            public void onResponse(GoodsSortBean response) {
                if (response.getState() == 1) {
                    LogUtils.e("网络请求++++++++++++" + response.toString());
                    Message msg = new Message();
                    msg.what = GET_GOODSSORT_SUCCESS;
                    msg.obj = response;
                    handler.sendMessage(msg);

                }
            }
        });
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_GOODSSORT_FAILED:
                    LogUtils.e("网络请求失败");
                    dismissDialog();
                    break;
                case GET_GOODSSORT_SUCCESS:
                    GoodsSortBean data = (GoodsSortBean) msg.obj;
                    //把网络请求的商品类别数据，进行一级二级三级分类，放在各自对应的集合当中
//                    listSort(data.getData());
                    goodsSortListABC.clear();
                    goodsSortListABC.addAll(turnToABC(data.getData()));
                    aListAdapter.notifyDataSetChanged();
                    //默认展示一级分类的第一项
                    getBCategoryList(0);
                    LogUtils.e("abcabcabcabca"+goodsSortListABC.size());
                    dismissDialog();
                    break;
            }
        }
    };

    /**
     * 把网络请求的数据进行分类
     */
    public static List<ABCGoodsSortData> turnToABC(List<GoodsSortData> datas) {
        List<GoodsSortData> categoryListA = new ArrayList<>();
        List<GoodsSortData> categoryListB = new ArrayList<>();
        List<GoodsSortData> categoryListC = new ArrayList<>();
        List<BCGoodsSortData> categoryListBC = new ArrayList<>();
        List<ABCGoodsSortData> categoryListABC = new ArrayList<>();
        for(GoodsSortData data:datas){//先份三组分别对应一级二级三级分类
            if(data.getCC_Level()==1){
                categoryListA.add(data);

            }else if (data.getCC_Level()==2){
                categoryListB.add(data);
            }else if (data.getCC_Level()==3){
                categoryListC.add(data);
            }
        }
        LogUtils.e(categoryListA.size()+"**"+categoryListB.size()+"**"+categoryListC.size());
        for (GoodsSortData data:categoryListB){//把二级列表给goodsSortListBC
            BCGoodsSortData bcData = new BCGoodsSortData();
            bcData.setCC_GUID(data.getCC_GUID());
            bcData.setCC_Name(data.getCC_Name());
            bcData.setCC_ParentGUID(data.getCC_ParentGUID());
            bcData.setCC_Icon(data.getCC_Icon());
            bcData.setCC_Level(data.getCC_Level());
            bcData.setCC_Sort(data.getCC_Sort());
            bcData.setCC_Count(data.getCC_Count());
            List<GoodsSortData> list = new ArrayList<>();
            bcData.setCGoodsSortList(list);
            categoryListBC.add(bcData);
        }
        LogUtils.e(categoryListBC.size() + "**");
        for (GoodsSortData data:categoryListC){//继续把二级三级分类合为一个组
            String numberParent = data.getCC_ParentGUID();
            for (BCGoodsSortData bcData:categoryListBC){
                if (numberParent.equals(bcData.getCC_GUID())){
                    LogUtils.e("2222222222");
                    bcData.getCGoodsSortList().add(data);
                    break;
                }
            }
        }

        LogUtils.e(categoryListBC.size() + "**");
        for (GoodsSortData data:categoryListA){//把一级列表给goodsSortListABC
            ABCGoodsSortData abcData = new ABCGoodsSortData();
            abcData.setCC_GUID(data.getCC_GUID());
            abcData.setCC_Name(data.getCC_Name());
            abcData.setCC_ParentGUID(data.getCC_ParentGUID());
            abcData.setCC_Icon(data.getCC_Icon());
            abcData.setCC_Level(data.getCC_Level());
            abcData.setCC_Sort(data.getCC_Sort());
            abcData.setCC_Count(data.getCC_Count());
            List<BCGoodsSortData> list = new ArrayList<>();
            abcData.setBcGoodsSortList(list);
            categoryListABC.add(abcData);
        }
        LogUtils.e(categoryListABC.size() + "**");
        for (BCGoodsSortData data:categoryListBC){

            String numberParent = data.getCC_ParentGUID();
            for (ABCGoodsSortData abcData:categoryListABC){

                if (numberParent.equals(abcData.getCC_GUID())){

                    abcData.getBcGoodsSortList().add(data);
                    break;
                }
            }
        }
        LogUtils.e(categoryListABC.size() + "**");
        return categoryListABC;
    }
    private GoodsACategoryListAdapter aListAdapter;
    private GoodsBCategoryListAdapter bListAdapter;
    private List<BCGoodsSortData> bCategoryList;

    @Override
    public void initEachData() {
        //通过shopActivity得到附近商家信息，然后的到商家序号
        shopSerialNumber = activity.getShopSerialNo();
        getGoodsSort(shopSerialNumber);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        lvFirstCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aListAdapter.setCheckID(position);
                getBCategoryList(position);//获取对应一级下的二级列表
                aListAdapter.notifyDataSetChanged();
                LogUtils.e("条目点击" + position);
            }
        });

    }
    /**
     * 获取二级列表
     */
    private void getBCategoryList(int position) {
        LogUtils.e("abc商品列表数量" + goodsSortListABC.size());
        if(goodsSortListABC.size()!=0){
            //获取对应一级下的二级列表
            List<BCGoodsSortData> bCategoryList = goodsSortListABC.get(position).getBcGoodsSortList();
            LogUtils.e("二级列表"+bCategoryList.size());
            bListAdapter = new GoodsBCategoryListAdapter(bCategoryList, ctx,shopSerialNumber);
            bListAdapter.setGoTOGoodsListListener(new GoodsBCategoryListAdapter.GoTOGoodsListListener() {
                @Override
                public void goToGoodsList(String numberCate) {
                    activity.setnumberCate(numberCate);
                    activity.setpageIndexnum(1);
                    activity.getindicatorViewPager().setCurrentItem(0,true);
                    activity.getmyIndicatorFragmentAdapter().notifyDataSetChanged();

//                    Intent intent = new Intent(ctx, ShopGoodsActivity.class);
//                    intent.putExtra("numberCate",numberCate);
//                    intent.putExtra("shopSerialNo",shopSerialNumber);
//                    intent.putExtra("shopName",activity.getShopName());
//                    intent.putExtra("freight",activity.getShopDetail().getData().getS_Freight());
//                    intent.putExtra("minMoney",activity.getMinMoney());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    ctx.startActivity(intent);
                }
            });
            lvSecondCategory.setAdapter(bListAdapter);
        }else {
            showToast(ctx,"暂无商品，请选择其他超市");
        }

    }

    /**
     * 填充一级分类列表
     */
    private void initAList() {

        aListAdapter = new GoodsACategoryListAdapter(goodsSortListABC, ctx,shopSerialNumber);
        lvFirstCategory.setAdapter(aListAdapter);
        lvFirstCategory.setItemChecked(0, true);
    }

}
