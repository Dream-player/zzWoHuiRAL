package whcs.wohui.zz.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 说明：一行显示不完时永远带有滚动效果的TextView
 * 作者：陈杰宇
 * 时间： 2016/1/22 16:49
 * 版本：V1.0
 * 修改历史：
 */
public class MyAlwaysRunTextView extends TextView{

    public MyAlwaysRunTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAlwaysRunTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public MyAlwaysRunTextView(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
