package whcs.wohui.zz.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/7/6 16:29
 * 版本：V1.0
 * 修改历史：
 */
public class MyGridView extends GridView{
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyGridView(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
