package zz.wohui.wohui365.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 说明：在ListView内部使用的GridView
 * 作者：陈杰宇
 * 时间： 2016/2/28 16:07
 * 版本：V1.0
 * 修改历史：
 */
public class MyGridViewInListView extends GridView {
    public MyGridViewInListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridViewInListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyGridViewInListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
