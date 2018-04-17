package zz.wohui.wohui365.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import zz.wohui.wohui365.R;

/**
 * 说明：订单支付页面选择支付方式的控件
 * 作者：陈杰宇
 * 时间： 2016/3/2 22:07
 * 版本：V1.0
 * 修改历史：
 */
public class MyPayMethodLinearLayout extends LinearLayout {

    private ImageView ivPayMethod;
    private TextView tvPayMethodName;
    private TextView tvPayMethodMark;
    private CheckBox cbPayMethodChecked;

    public MyPayMethodLinearLayout(Context context) {
        super(context);
    }


    public MyPayMethodLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = LayoutInflater.from(context).inflate(R.layout.pay_method_linear, this, true);
        ivPayMethod = (ImageView) v.findViewById(R.id.ivPayMethod);
        tvPayMethodName = (TextView) v.findViewById(R.id.tvPayMethodName);
        tvPayMethodMark = (TextView) v.findViewById(R.id.tvPayMethodMark);
        cbPayMethodChecked = (CheckBox) v.findViewById(R.id.cbPayMethodChecked);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyPayMethodLinearLayout);
        int a = typedArray.getIndexCount();
        for (int i = 0; i < a; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MyPayMethodLinearLayout_methodImage:
                    ivPayMethod.setImageDrawable(typedArray.getDrawable(attr));
                    break;
                case R.styleable.MyPayMethodLinearLayout_methodTitle:
                    tvPayMethodName.setText(typedArray.getString(attr));
                    break;
                case R.styleable.MyPayMethodLinearLayout_methodIsMark:
                    if (typedArray.getBoolean(attr, false)) {
                        tvPayMethodMark.setVisibility(VISIBLE);
                    } else {
                        tvPayMethodMark.setVisibility(GONE);
                    }
                    break;
                case R.styleable.MyPayMethodLinearLayout_methodMark:
                    tvPayMethodMark.setText(typedArray.getString(attr));
                    break;
            }
        }
        typedArray.recycle();
    }

    public MyPayMethodLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChecked(boolean isChecked) {
        cbPayMethodChecked.setChecked(isChecked);
    }
}
