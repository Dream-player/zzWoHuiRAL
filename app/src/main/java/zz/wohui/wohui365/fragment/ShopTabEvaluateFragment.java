package zz.wohui.wohui365.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.ShopCommentListBean;
import zz.wohui.wohui365.Bean.ShopDetailBean;
import zz.wohui.wohui365.activity.ShopActivity;
import zz.wohui.wohui365.adapter.ShopEvaluateListAdapter;
import zz.wohui.wohui365.callback.CommentListCallBack;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.utils.MyUtils;
import zz.wohui.wohui365.R;

/**
 * 说明：商家——评价
 * 作者：陈杰宇
 * 时间： 2016/1/29 11:16
 * 版本：V1.0
 * 修改历史：
 */
public class ShopTabEvaluateFragment extends BaseFragment {

    private PullToRefreshListView evaluatePullList;

    private void assignViews(View v) {
        evaluatePullList = (PullToRefreshListView) v.findViewById(R.id.evaluatePullList);
    }
    private TextView tvShopGrade;
    private TextView tvShopRanking;
    private RatingBar ratingBarService;
    private TextView tvGradeService;
    private RatingBar ratingBarGoods;
    private TextView tvGradeGoods;

    /**
     * Head的控件获取
     * @param v head
     */
    private void assignViewsHead(View v) {
        tvShopGrade = (TextView) v.findViewById(R.id.tvShopGrade);
        tvShopRanking = (TextView) v.findViewById(R.id.tvShopRanking);
        ratingBarService = (RatingBar) v.findViewById(R.id.ratingBarService);
        tvGradeService = (TextView) v.findViewById(R.id.tvGradeService);
        ratingBarGoods = (RatingBar) v.findViewById(R.id.ratingBarGoods);
        tvGradeGoods = (TextView) v.findViewById(R.id.tvGradeGoods);
    }


    private Context ctx;
    private ShopActivity activity;
    private ListView evaluateListView;
    private String[] mStrings = {"1","2","3","3","3","3"};
    private ShopEvaluateListAdapter shopEvaluateListAdapter;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;
    private int nowPage = 1;//当前展示页数
    //定义存放评价列表的list
    private List<ShopCommentListBean.DataEntity> commentList;
    private static final int GET_COMMETNLIST_SUCCESS = 1011;
    private static final int GET_COMMETNLIST_FAILED = 1012;

    @Override
    public View initView() {
        ctx = ShopTabEvaluateFragment.this.getContext();
        activity = (ShopActivity)getActivity();
        View v = View.inflate(ctx, R.layout.shop_tab_fragment_evaluate, null);
        assignViews(v);
        initData();
        initHeadView();
        return v;
    }



    /**
     * 初始化控件
     */
    private void initData() {
        MyUtils.initPullList(evaluatePullList, ctx);
        evaluateListView = evaluatePullList.getRefreshableView();
        View v = View.inflate(ctx, R.layout.shop_tab_evaluate_head, null);
        //获取并添加listView头部
        assignViewsHead(v);
        evaluateListView.addHeaderView(v);
        //创建list，adapter，并给ListView设置adapter
        commentList = new ArrayList<>();
        shopEvaluateListAdapter = new ShopEvaluateListAdapter(commentList,ctx);
        evaluateListView.setAdapter(shopEvaluateListAdapter);
        //通过shopActivity得到附近商家信息，然后的到商家序号
        String shopSerialNumber = activity.getShopSerialNo();
        //获取商家评价列表
        getShopCommentList(nowPage, shopSerialNumber);
    }

    /**
     * 初始化头部布局
     */
    private void initHeadView() {
        ShopDetailBean shopDetail = activity.getShopDetail();
        tvShopGrade.setText(shopDetail.getData().getS_Score()+"");
        ratingBarGoods.setRating(shopDetail.getData().getS_Score());
        tvGradeGoods.setText(shopDetail.getData().getS_Score()+"");
    }
    /**
     * 通过网络请求获取商家评价列表
     */
    private void getShopCommentList(final int nowpage,String shopSerialNumber){
        myOkHttpUtils = new MyOkHttpUtils();
        params = new MyRequestParams();
        params.clear();
        params.addStringRequest(ParamsKey.userKey,activity.getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.CommentList_nowPage,nowpage+"");
        params.addStringRequest(ParamsKey.ShopSerialNumber,shopSerialNumber);
        String strUrl = Urls.GetCommentList;
        myOkHttpUtils.postRequest(strUrl, params, new CommentListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                handler.sendEmptyMessage(GET_COMMETNLIST_FAILED);
            }
            @Override
            public void onResponse(ShopCommentListBean response) {
                if(response.getState()==1){
                    LogUtils.e("网络请求++++++++++++");
                    nowPage++;
                    Message msg = new Message();
                    msg.what = GET_COMMETNLIST_SUCCESS;
                    msg.obj = response;
                    handler.sendMessage(msg);
                }
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_COMMETNLIST_SUCCESS://当评价列表请求成功时
                    if(nowPage==1){
                        commentList.clear();
                    }
                    ShopCommentListBean bean= (ShopCommentListBean)msg.obj;
                    if (bean.getData().size()>0){
                        commentList.addAll(bean.getData());
                        shopEvaluateListAdapter.notifyDataSetChanged();
                    }
                    break;
                case GET_COMMETNLIST_FAILED://当评价列表请求失败时
                    Toast.makeText(ctx,"网络请求失败",Toast.LENGTH_SHORT);
                    break;
            }
        }
    };


    /**
     * 每次切换页面都要重新加载的方法
     */
    @Override
    public void initEachData() {

    }

}
