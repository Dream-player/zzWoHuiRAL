package zz.wohui.zz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zz.wohui.zz.Bean.GoodsSortData;
import zz.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：三级列表适配器
 * 作者：陈杰宇
 * 时间： 2016/2/28 15:03
 * 版本：V1.0
 * 修改历史：
 */
public class GoodsCCategoryListAdapter extends MyBaseAdapter<GoodsSortData> {

    public GoodsCCategoryListAdapter(List<GoodsSortData> dataList, Context ctx) {
        super(dataList, ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_goods_category_c_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvCCategoryName.setText(dataList.get(position).getCC_Name());
        return convertView;
    }

    private class ViewHolder {
        public final TextView tvCCategoryName;
        public final View root;

        public ViewHolder(View root) {
            tvCCategoryName = (TextView) root.findViewById(R.id.tvCCategoryName);
            this.root = root;
        }
    }
}
