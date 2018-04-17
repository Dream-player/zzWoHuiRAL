package zz.wohui.wohui365.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zz.wohui.wohui365.Bean.AddressBean;
import zz.wohui.wohui365.R;

/**
 * 说明：收货地址管理
 * 作者：陈杰宇
 * 时间： 2016/2/19 10:02
 * 版本：V1.0
 * 修改历史：
 */
public class AddressManageAdapter extends MyBaseAdapter<AddressBean.DataEntity> {

    private static AddressBean.DataEntity data;

    public AddressManageAdapter(List<AddressBean.DataEntity> dataList, Context ctx) {
        super(dataList, ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_user_address_manage, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        data = dataList.get(position);
        if (data.isDA_IsDefault()){
            vh.ivAddressEdit.setImageResource(R.drawable.icon_adress_default);
        }
        vh.tvRecieverName.setText(data.getDA_RealName());
        vh.tvRecieverPhone.setText(data.getDA_Phone());
        vh.tvRecieverAddress.setText(data.getDA_Province()
                + data.getDA_City()
                + data.getDA_District()
                + data.getDA_Towns()
                + data.getDA_Address());
        return convertView;
    }

    private class ViewHolder {
        public final TextView tvRecieverName;
        public final TextView tvRecieverPhone;
        public final TextView tvRecieverAddress;
        public final ImageView ivAddressEdit;
        public final View root;

        public ViewHolder(View root) {
            tvRecieverName = (TextView) root.findViewById(R.id.tvRecieverName);
            tvRecieverPhone = (TextView) root.findViewById(R.id.tvRecieverPhone);
            tvRecieverAddress = (TextView) root.findViewById(R.id.tvRecieverAddress);
            ivAddressEdit = (ImageView) root.findViewById(R.id.ivAddressEdit);
            this.root = root;
        }
    }
}
