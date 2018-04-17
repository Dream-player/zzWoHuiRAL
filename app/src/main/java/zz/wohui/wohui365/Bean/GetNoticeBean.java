package zz.wohui.wohui365.Bean;

import java.io.Serializable;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/12 15:55
 * 版本：V1.0
 * 修改历史：
 */
public class GetNoticeBean implements Serializable{


    /**
     * State : 1
     * Data : 滚动字幕
     * Msg :
     */

    private int State;
    private String Msg;
    private String Data;

    public void setData(String Data) {
        this.Data = Data;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }


    public void setState(int State) {
        this.State = State;
    }


    public String getData() {
        return Data;
    }

    public int getState() {
        return State;
    }

    public String getMsg() {
        return Msg;
    }


}
