package whcs.wohui.zz.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 说明：Adapter的基类
 * 作者：陈杰宇
 * 时间： 2016/1/27 10:59
 * 版本：V1.0
 * 修改历史：
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public List<T> dataList;
    public Context ctx;

    public MyBaseAdapter(List<T> dataList, Context ctx) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    public MyBaseAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
