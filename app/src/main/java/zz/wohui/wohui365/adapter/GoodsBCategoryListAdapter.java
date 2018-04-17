package zz.wohui.wohui365.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zz.wohui.wohui365.Bean.BCGoodsSortData;
import zz.wohui.wohui365.myview.MyGridViewInListView;
import zz.wohui.wohui365.utils.LogUtils;
import zz.wohui.wohui365.whcouldsupermarket.R;

/**
 * 说明：商品二到三级列表的适配器
 * 作者：陈杰宇
 * 时间： 2016/2/28 14:45
 * 版本：V1.0
 * 修改历史：
 */
public class GoodsBCategoryListAdapter extends MyBaseAdapter<BCGoodsSortData> {
    private String shopSerialNumber;

    public void setGoTOGoodsListListener(GoTOGoodsListListener goTOGoodsListListener) {
        this.goTOGoodsListListener = goTOGoodsListListener;
    }

    private GoTOGoodsListListener goTOGoodsListListener;
    public GoodsBCategoryListAdapter(List<BCGoodsSortData> dataList, Context ctx,String shopSerialNumber) {
        super(dataList, ctx);
        this.shopSerialNumber = shopSerialNumber;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_goods_category_b_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        GoodsCCategoryListAdapter cCategoryListAdapter
                = new GoodsCCategoryListAdapter(dataList.get(position).getCGoodsSortList(), ctx);
        vh.tvCategoryBName.setText(dataList.get(position).getCC_Name());
        vh.gvCategoryC.setAdapter(cCategoryListAdapter);
        vh.gvCategoryC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int bPosition, long id) {
                String numberCate = dataList.get(position).getCGoodsSortList().get(bPosition).getCC_GUID();
                LogUtils.e(numberCate);
                if (goTOGoodsListListener!=null){
                    goTOGoodsListListener.goToGoodsList(numberCate);
                }
            }
        });
        return convertView;
    }
    public interface GoTOGoodsListListener{
        public void goToGoodsList(String numberCate);
    }

    private class ViewHolder {
        public final TextView tvCategoryBName;
        public final MyGridViewInListView gvCategoryC;
        public final LinearLayout llCategoryBTitle;
        public final View root;

        public ViewHolder(View root) {
            tvCategoryBName = (TextView) root.findViewById(R.id.tvCategoryBName);
            llCategoryBTitle = (LinearLayout) root.findViewById(R.id.llCategoryBTitle);
            gvCategoryC = (MyGridViewInListView) root.findViewById(R.id.gvCategoryC);
            this.root = root;
        }
    }
}
