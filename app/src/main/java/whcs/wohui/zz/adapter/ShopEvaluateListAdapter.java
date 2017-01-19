package whcs.wohui.zz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;


import whcs.wohui.zz.Bean.ShopCommentListBean.DataEntity;
import whcs.wohui.zz.whcouldsupermarket.R;

/**
 * 说明：超市商品评价列表用Adapter
 * 作者：陈杰宇
 * 时间： 2016/1/30 15:51
 * 版本：V1.0
 * 修改历史：
 */
public class ShopEvaluateListAdapter extends MyBaseAdapter<DataEntity> {


    public ShopEvaluateListAdapter(List<DataEntity> dataList, Context ctx) {
        super(dataList, ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_shop_tab_evaluate, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        DataEntity commentEndity = dataList.get(position);
        vh.tvUsername.setText("匿名");
        vh.roomRatingbar.setRating(commentEndity.getOC_Score());
        vh.tvRatingBar.setText(commentEndity.getOC_Score()+"星");
//        vh.tvFirstCommentTime.setText(commentEndity.getFirstDateSubmit());
        vh.tvFirstCommentContent.setText(commentEndity.getOC_CommentMsg());
//        if(true){
//            vh.llSecondCommentBar.setVisibility(View.VISIBLE);
//            vh.tvSecondCommentContent.setVisibility(View.VISIBLE);
//            vh.tvSecondCommentTime.setText(commentEndity.getSecondDateSubmit());
//            vh.tvSecondCommentContent.setText(commentEndity.getSecondContentSubmit());
//        }else{
//            vh.llSecondCommentBar.setVisibility(View.GONE);
//            vh.tvSecondCommentContent.setVisibility(View.GONE);
//        }
        return convertView;
    }
        private class ViewHolder{
            public final TextView tvUsername;
            public final RatingBar roomRatingbar;
            public final TextView tvRatingBar;
            public final TextView tvFirstCommentTime;
            public final TextView tvFirstCommentContent;
            public final LinearLayout llSecondCommentBar;
            public final TextView tvSecondCommentTime;
            public final TextView tvSecondCommentContent;
            public final View root;
            public ViewHolder(View root){
                this.root = root;
                tvUsername = (TextView) root.findViewById(R.id.tv_username);
                roomRatingbar = (RatingBar) root.findViewById(R.id.room_rating_bar);
                tvRatingBar = (TextView) root.findViewById(R.id.tv_rating_bar);
                tvFirstCommentTime = (TextView) root.findViewById(R.id.tv_first_comment_time);
                tvFirstCommentContent = (TextView) root.findViewById(R.id.tv_first_comment_content);
                llSecondCommentBar = (LinearLayout) root.findViewById(R.id.ll_second_comment_bar);
                tvSecondCommentTime = (TextView) root.findViewById(R.id.tv_second_comment_time);
                tvSecondCommentContent = (TextView) root.findViewById(R.id.tv_second_comment_content);
            }
        }

}
