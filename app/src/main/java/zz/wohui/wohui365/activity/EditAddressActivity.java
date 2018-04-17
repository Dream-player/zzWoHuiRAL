package zz.wohui.wohui365.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import zz.wohui.wohui365.Bean.AddressBean;
import zz.wohui.wohui365.Bean.IsSuccessBean;
import zz.wohui.wohui365.Bean.ProCityZoneBean;
import zz.wohui.wohui365.callback.IsSuccessCallBack;
import zz.wohui.wohui365.url.ParamsKey;
import zz.wohui.wohui365.url.Urls;
import zz.wohui.wohui365.utils.MyKey;
import zz.wohui.wohui365.utils.MyOkHttpUtils;
import zz.wohui.wohui365.utils.MyRequestParams;
import zz.wohui.wohui365.utils.MyUtils;
import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * 说明：编辑/添加收货地址信息
 * 作者：陈杰宇
 * 时间： 2016/2/19 14:00
 * 版本：V1.0
 * 修改历史：
 */
public class EditAddressActivity extends BaseActivity implements View.OnClickListener {

    private EditText etEditAddName;
    private EditText etEditAddPhone;
    private EditText etEditAddArea;
    private EditText etEditAddDetails;
    private Button btnSubmitAddress;
    private TextView titleName,tvDetailAddress;
    private RelativeLayout goBack;
    private SwitchButton sbIsDefault;
    private boolean isAdd;
    private void assignViews() {
        titleName = (TextView) findViewById(R.id.titleName);
        goBack = (RelativeLayout) findViewById(R.id.titleGoBack);
        etEditAddName = (EditText) findViewById(R.id.etEditAddName);
        etEditAddPhone = (EditText) findViewById(R.id.etEditAddPhone);
        etEditAddArea = (EditText) findViewById(R.id.etEditAddArea);
        etEditAddDetails = (EditText) findViewById(R.id.etEditAddDetails);
        btnSubmitAddress = (Button) findViewById(R.id.btnSubmitAddress);
        sbIsDefault = (SwitchButton) findViewById(R.id.sbIsDefault);
        tvDetailAddress = (TextView) findViewById(R.id.tvDetailAddress);
    }

    private Context ctx;
    private ArrayList<String> optionsItemPro = new ArrayList<String>();
    private ArrayList<ArrayList<String>> options2City = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Zone = new ArrayList<ArrayList<ArrayList<String>>>();
    OptionsPickerView pvOptions;
    private List<ProCityZoneBean.Datas> Prodatas;
    private AddressBean.DataEntity addressBean;
    private String provice;
    private String city;
    private String district;
    private String town = " ";
    private String addressDetail;

    @Override
    public void myOnCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_address);
        assignViews();
        ctx = this;
        Intent intent = getIntent();
        isAdd = intent.getBooleanExtra("isAddAddress", false);
        if (isAdd) {
            titleName.setText("添加地址");
            btnSubmitAddress.setText("确认添加地址");
        } else {
            titleName.setText("编辑地址");
            btnSubmitAddress.setText("确认修改地址");
            addressBean = (AddressBean.DataEntity) intent.getSerializableExtra("AddressBean");
            provice = addressBean.getDA_Province();
            city = addressBean.getDA_City();
            district = addressBean.getDA_District();
            town = addressBean.getDA_Towns();
            addressDetail = addressBean.getDA_Address();
            initView();
        }
        initProCityZone();
        initListener();
    }

    /**
     * 填充布局
     */
    private void initView() {
        etEditAddName.setText(addressBean.getDA_RealName());
        etEditAddPhone.setText(addressBean.getDA_Phone());
        etEditAddArea.setText(addressBean.getDA_Province()
                +addressBean.getDA_City()
                +addressBean.getDA_District());
        tvDetailAddress.setText(addressBean.getDA_Towns());
        etEditAddDetails.setText(addressBean.getDA_Address());
        if (addressBean.isDA_IsDefault()) {
            sbIsDefault.setChecked(true);
        } else {
            sbIsDefault.setChecked(false);
        }
    }

    private void initListener() {
        etEditAddArea.setOnClickListener(this);
        goBack.setOnClickListener(this);
        btnSubmitAddress.setOnClickListener(this);
        tvDetailAddress.setOnClickListener(this);
    }
    private static final int REQUEST_SELECTE_ADDRESS = 12;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etEditAddArea:
                checkAreaDialog();
                break;
            case R.id.titleGoBack:
                this.onBackPressed();
                break;
            case R.id.tvDetailAddress:
                Intent intent = new Intent(ctx,AddUserLocationActivity.class);
                startActivityForResult(intent,REQUEST_SELECTE_ADDRESS);
                break;
            case R.id.btnSubmitAddress:
                String name = etEditAddName.getText().toString().trim();
                String phone = etEditAddPhone.getText().toString().trim();
                String area = etEditAddArea.getText().toString().trim();
                String street = tvDetailAddress.getText().toString().trim();
                String house = etEditAddDetails.getText().toString().trim();
                if (name.equals("")) {
                    etEditAddName.setError("收货人不能为空");
                    break;
                }
                if (phone.equals("")) {
                    etEditAddPhone.setError("电话不能为空");
                    break;
                }
                if (provice.equals("")&&city.equals("")&&district.equals("")) {
                    etEditAddArea.setError("省市县不能为空");
                    break;
                }
                if (street.equals("")) {
                    tvDetailAddress.setError("小区/大厦/学校不能为空");
                    break;
                }
                if (house.equals("")) {
                    etEditAddDetails.setError("楼号/门牌号不能为空");
                    break;
                }
                MyRequestParams params = new MyRequestParams();
                params.addStringRequest(ParamsKey.userKey, getMyApplication().getUserKey());
                if (isAdd) {
                    params.addStringRequest(ParamsKey.AddConsignee_SerialNumber, "");
                } else {
                    params.addStringRequest(ParamsKey.AddConsignee_SerialNumber,
                            addressBean.getDA_GUID());
                }
                params.addStringRequest(ParamsKey.AddConsignee_NumberUser,
                        getMyApplication().getUserKey());
                params.addStringRequest(ParamsKey.AddConsignee_Consignee, name);
                params.addStringRequest(ParamsKey.AddConsignee_Phone, phone);
                params.addStringRequest(ParamsKey.AddConsignee_Province, provice);
                params.addStringRequest(ParamsKey.AddConsignee_City, city);
                params.addStringRequest(ParamsKey.AddConsignee_District, district);
                params.addStringRequest(ParamsKey.AddConsignee_Town, street+"");
                params.addStringRequest(ParamsKey.AddConsignee_AddrDetail, house+"");
                //2016/10/20 添加实际收货地址
                if (latitude==0||longitude==0){
                    longitude = getMyApplication().getLatLng().longitude;
                    latitude = getMyApplication().getLatLng().latitude;
                }
                params.addStringRequest(ParamsKey.AddConsignee_MapLongitude, ""+longitude);
                params.addStringRequest(ParamsKey.AddConsignee_MapLatitude, ""+latitude);
                params.addStringRequest(ParamsKey.AddConsignee_IsDefault, sbIsDefault.isChecked() + "");
                submitAdd(params);
                break;
        }
    }

    /**
     * 提交地址修改/添加
     *
     * @param params 参数
     */
    private void submitAdd(MyRequestParams params) {
        showDialog(ctx);
        MyOkHttpUtils myOkHttpUtils = new MyOkHttpUtils();
        String url = "";
        if (isAdd) {
            url = Urls.AddConsignee;
        } else {
            url = Urls.EditConsignee;
        }
        myOkHttpUtils.postRequest(url, params, new IsSuccessCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                showToast(ctx, MyUtils.networkError);
            }

            @Override
            public void onResponse(IsSuccessBean response) {
                dismissDialog();
                if (response.isIsSuccess() == true) {
                    EditAddressActivity.this.onBackPressed();
                } else {
                    dismissDialog();
                    if (isAdd) {
                        showToast(ctx, "添加失败");
                    } else {
                        showToast(ctx, "更新失败");
                    }

                }
            }
        });
    }
    private double latitude;
    private double longitude;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_SELECTE_ADDRESS&&resultCode==RESULT_OK){
            latitude = data.getDoubleExtra(MyKey.LATITUDE,0);
            longitude = data.getDoubleExtra(MyKey.LONGITUDE,0);
            String poiName = data.getStringExtra(MyKey.POI_NAME);
            tvDetailAddress.setText(poiName+"");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (MyUtils.isHideInput(view, ev)) {
                MyUtils.HideSoftInput(view.getWindowToken(), ctx);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 弹出地址选择器
     */
    private void checkAreaDialog() {
        pvOptions = new OptionsPickerView(this);
        //三级联动效果
        pvOptions.setPicker(optionsItemPro, options2City, options3Zone, true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                provice = optionsItemPro.get(options1);
                city = options2City.get(options1).get(option2);
                district = options3Zone.get(options1).get(option2).get(options3);
                String tx = optionsItemPro.get(options1)
                        + options2City.get(options1).get(option2)
                        + options3Zone.get(options1).get(option2).get(options3);
                etEditAddArea.setText(tx);
            }
        });
        //点击弹出选项选择器
        pvOptions.show();
    }

    /**
     * 初始化地址的数据
     */
    private void initProCityZone() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String json;
                AssetManager am = null;
                am = ctx.getAssets();
                try {
                    InputStream is = am.open("Pro_City_Zone.txt");
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    json = new String(buffer, "utf-8");
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                ProCityZoneBean bean = new Gson().fromJson(json,
                        ProCityZoneBean.class);
                Prodatas = bean.data;
                System.out.println(Prodatas.size());
                ArrayList<String> cc;
                ArrayList<String> d_1_1;
                ArrayList<ArrayList<String>> d_1;
                for (int i = 0; i < Prodatas.size(); i++) {
                    d_1 = new ArrayList<ArrayList<String>>();
                    cc = new ArrayList<String>();
                    for (int j = 0; j < Prodatas.get(i).c.size(); j++) {
                        d_1_1 = new ArrayList<String>();
                        cc.add(Prodatas.get(i).c.get(j).cc);
                        for (int k = 0; k < Prodatas.get(i).c.get(j).d.size(); k++) {
                            d_1_1.add(Prodatas.get(i).c.get(j).d.get(k).dd);
                        }
                        d_1.add(d_1_1);
                    }
                    optionsItemPro.add(Prodatas.get(i).p);
                    options2City.add(cc);
                    options3Zone.add(d_1);
                }
            }
        }).start();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pvOptions != null && pvOptions.isShowing()) {
                pvOptions.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
