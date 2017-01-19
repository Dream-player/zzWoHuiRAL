package whcs.wohui.zz.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/3/11 16:02
 * 版本：V1.0
 * 修改历史：
 */
public class MyListViewInListView extends ListView {
    public MyListViewInListView(Context context) {
        super(context);
    }

    public MyListViewInListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListViewInListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
