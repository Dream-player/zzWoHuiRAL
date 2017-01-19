package whcs.wohui.zz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import whcs.wohui.zz.Bean.ShopCollectData;
import whcs.wohui.zz.activity.ShopMapActivity;
import whcs.wohui.zz.activity.UserCollectionActivity;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：我的收藏
 * 作者：陈杰宇
 * 时间： 2016/2/19 21:44
 * 版本：V1.0
 * 修改历史：
 */
public class UserCollectAdapter extends MyBaseAdapter<ShopCollectData> {

    public UserCollectAdapter(List<ShopCollectData> dataList, Context ctx) {
        super(dataList, ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_shop_collect, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        ShopCollectData data = dataList.get(position);
        final double latitude = Double.parseDouble(data.getMapLatitude());
        final double longitude = Double.parseDouble(data.getMapLongitude());
        vh.tvShopName.setText(data.getNameSM());
        vh.tvShopAddress.setText(data.getAddrArea()+data.getAddrDetail());
        //TODO 给tvLocation添加监听
        vh.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ShopMapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                ctx.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public final TextView tvShopName;
        public final TextView tvShopAddress;
        public final TextView tvLocation;
        public final View root;

        public ViewHolder(View root) {
            tvShopName = (TextView) root.findViewById(R.id.tvShopName);
            tvShopAddress = (TextView) root.findViewById(R.id.tvShopAddress);
            tvLocation = (TextView) root.findViewById(R.id.tvLocation);
            this.root = root;
        }
    }
}
