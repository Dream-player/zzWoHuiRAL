package whcs.wohui.zz.listener;

import android.view.View;

/**
 * 说明：listView的Item内部按钮点击事件监听
 * 作者：陈杰宇
 * 时间： 2016/2/24 10:47
 * 版本：V1.0
 * 修改历史：
 */
public abstract class OnItemButtonClickListener implements View.OnClickListener {

    private int ID;
    private int position;
    private Object object;

    public OnItemButtonClickListener(int ID, int position, Object object) {
        this.ID = ID;
        this.position = position;
        this.object = object;
    }
}
