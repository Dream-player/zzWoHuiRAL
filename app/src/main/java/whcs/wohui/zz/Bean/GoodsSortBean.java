package whcs.wohui.zz.Bean;

import java.util.List;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/21 13:37
 * 版本：V1.0
 * 修改历史：
 */
public class GoodsSortBean {

    /**
     * State : 1
     * Data : []
     * Msg :
     */

    private int State;
    private String Msg;
    private List<GoodsSortData> Data;

    public void setState(int State) {
        this.State = State;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public void setData(List<GoodsSortData> Data) {
        this.Data = Data;
    }

    public int getState() {
        return State;
    }

    public String getMsg() {
        return Msg;
    }

    public List<GoodsSortData> getData() {
        return Data;
    }
}
