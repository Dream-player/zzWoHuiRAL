package whcs.wohui.zz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import whcs.wohui.zz.Bean.ShopCollectData;
import whcs.wohui.zz.adapter.UserCollectAdapter;
import whcs.wohui.zz.listener.OnItemButtonClickListener;
import whcs.wohui.zz.utils.ShopCellectDBHelper;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：我的收藏页面
 * 作者：陈杰宇
 * 时间： 2016/2/19 21:17
 * 版本：V1.0
 * 修改历史：
 */
public class UserCollectionActivity extends BaseActivity {

    private RelativeLayout rlNoData, goBack;
    private TextView tvNoData, titleName;
    private ListView lvUserCollect;
    private List<ShopCollectData> shopCollectDataList;
    private ShopCellectDBHelper myDBHelper;

    private void assignViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        rlNoData = (RelativeLayout) findViewById(R.id.rlNoData);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        lvUserCollect = (ListView) findViewById(R.id.lvUserCollect);
    }

    private static Context ctx;
    private UserCollectAdapter adapter;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_collection);
        assignViews();
        ctx = this;
        myDBHelper = new ShopCellectDBHelper(ctx);
        init();
        getShopCollect();
        initListener();

    }
    private void init(){
        titleName.setText("我的收藏");
        shopCollectDataList = new ArrayList<>();
        adapter = new UserCollectAdapter(shopCollectDataList, ctx);
        lvUserCollect.setAdapter(adapter);
    }
    private void initListener(){
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCollectionActivity.this.onBackPressed();
                finish();
            }
        });
        lvUserCollect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserCollectionActivity.this,ShopActivity.class);
                intent.putExtra("serialNumber",shopCollectDataList.get(position).getSerialNumber());
                startActivity(intent);
            }
        });
    }
    /**
     * 获取用户收藏列表
     */
    private void getShopCollect() {
        shopCollectDataList.clear();
        shopCollectDataList.addAll(myDBHelper.getAllShop());
        adapter.notifyDataSetChanged();
        if (adapter.isEmpty()){
            rlNoData.setVisibility(View.VISIBLE);
        }else {
            rlNoData.setVisibility(View.GONE);
        }
    }

    /**
     * 查看商家在地图上的位置
     */
    private static void checkShopLocation() {
        Intent intent = new Intent(ctx, ShopLocationActivity.class);
        ctx.startActivity(intent);
    }

}
