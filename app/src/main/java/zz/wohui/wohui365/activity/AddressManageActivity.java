package zz.wohui.zz.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.Call;
import zz.wohui.zz.Bean.AddressBean;
import zz.wohui.zz.Bean.IsSuccessBean;
import zz.wohui.zz.adapter.AddressManageAdapter;
import zz.wohui.zz.callback.AddressCallBack;
import zz.wohui.zz.callback.IsSuccessCallBack;
import zz.wohui.zz.url.ParamsKey;
import zz.wohui.zz.url.Urls;
import zz.wohui.zz.utils.MyOkHttpUtils;
import zz.wohui.zz.utils.MyRequestParams;
import zz.wohui.zz.utils.MyUtils;
import zz.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：管理收货地址页面
 * 作者：陈杰宇
 * 时间： 2016/2/18 22:15
 * 版本：V1.0
 * 修改历史：
 */
public class AddressManageActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvUserAddress;
    private RelativeLayout rlNoAddress;
    private TextView noAddress;
    private RelativeLayout rlAddAddress;
    private TextView addAddress;
    private TextView titleName;
    private RelativeLayout goBack;
    private Intent intent;

    private void assignViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        lvUserAddress = (ListView) findViewById(R.id.lvUserAddress);
        rlNoAddress = (RelativeLayout) findViewById(R.id.rlNoAddress);
        noAddress = (TextView) findViewById(R.id.noAddress);
        rlAddAddress = (RelativeLayout) findViewById(R.id.rlAddAddress);
        addAddress = (TextView) findViewById(R.id.addAddress);
    }

    private Context ctx;
    private String[] tests = {"1", "2", "3"};
    private AddressManageAdapter adapter;
    private MyOnItemClickListener myOnItemClickListener;
    private MyOnItemLongClickListener myOnItemLongClickListener;
    private boolean isFromOrder = false;
    private MyOkHttpUtils myOkHttpUtils;
    private MyRequestParams params;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_address_manage);
        assignViews();
        ctx = this;
        intent = getIntent();
        params = new MyRequestParams();
        myOkHttpUtils = new MyOkHttpUtils();
        isFromOrder = intent.getBooleanExtra("isFromOrder", false);
        if (isFromOrder) {
            titleName.setText("选择收货地址");
        } else {
            titleName.setText("收货地址管理");
        }
        initListener();
    }

    private void initListener() {
        goBack.setOnClickListener(this);
        rlAddAddress.setOnClickListener(this);
        myOnItemClickListener = new MyOnItemClickListener();
        lvUserAddress.setOnItemClickListener(myOnItemClickListener);
        if (!isFromOrder) {
            myOnItemLongClickListener = new MyOnItemLongClickListener();
            lvUserAddress.setOnItemLongClickListener(myOnItemLongClickListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddress();
    }

    /**
     * 获取用户地址信息
     */
    private void getUserAddress() {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        myOkHttpUtils.postRequest(Urls.GetConsigneeList, params, new AddressCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                Toast.makeText(ctx, "联网失败请检查您的网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(AddressBean response) {
                if (response.getState() == 1) {
                    Message msg = new Message();
                    
                }
                fillingList(response.getData());
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    break;
            }
        }
    };
    /**
     * 填充数据
     *
     * @param data
     */
    private void fillingList(List<AddressBean.DataEntity> data) {
        if (data.size() <= 0) {
            rlNoAddress.setVisibility(View.VISIBLE);
            lvUserAddress.setVisibility(View.GONE);
        } else {
            rlNoAddress.setVisibility(View.GONE);
            lvUserAddress.setVisibility(View.VISIBLE);
            adapter = new AddressManageAdapter(data, ctx);
            lvUserAddress.setAdapter(adapter);
        }
        dismissDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleGoBack:
                this.onBackPressed();
                finish();
                break;
            case R.id.rlAddAddress:
                editOrAddAddress(true, null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED,intent);
        finish();
    }

    /**
     * 编辑/添加收货地址
     *
     * @param isAdd 是否是添加地址
     * @param bean
     */
    private void editOrAddAddress(boolean isAdd, AddressBean.DataEntity bean) {
        Intent intent = new Intent(ctx, EditAddressActivity.class);
        intent.putExtra("isAddAddress", isAdd);
        intent.putExtra("AddressBean", bean);
        startActivity(intent);
    }

    /**
     * 条目点击事件类
     */
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AddressBean.DataEntity bean = (AddressBean.DataEntity) parent
                    .getAdapter().getItem(position);
            if (isFromOrder) {
                intent.putExtra("data",bean);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                editOrAddAddress(false, bean);
            }
        }
    }

    /**
     * 条目长按事件类
     */
    private class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            AddressBean.DataEntity data = (AddressBean.DataEntity) parent
                    .getAdapter().getItem(position);
            popDelDialog(data);
            return true;
        }
    }

    /**
     * 提示删除
     *
     * @param data
     */
    private void popDelDialog(final AddressBean.DataEntity data) {
        MyUtils.showAlertDialog(ctx, "提示", "确定删除收货地址？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteAddress(data.getDA_GUID());
            }
        });
    }

    /**
     * 删除收货地址
     *
     * @param serialNumber
     */
    private void deleteAddress(String serialNumber) {
        showDialog(ctx);
        params.clear();
        params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
        params.addStringRequest(ParamsKey.DelConsignee_SerialNumber, serialNumber);
        myOkHttpUtils.postRequest(Urls.DelConsignee, params, new IsSuccessCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                showToast(ctx, MyUtils.networkError);
            }

            @Override
            public void onResponse(IsSuccessBean response) {
                dismissDialog();
                if (response.isIsSuccess()==true) {
                    getUserAddress();
                } else {
                    showToast(ctx, "删除失败");
                }
            }
        });
    }
}
