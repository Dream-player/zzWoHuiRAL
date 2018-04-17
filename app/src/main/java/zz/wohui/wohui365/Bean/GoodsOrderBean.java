package zz.wohui.zz.Bean;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/5/11 18:00
 * 版本：V1.0
 * 修改历史：
 */
public class GoodsOrderBean {

    /**
     * State : 1
     * Data : [{"Column1":"success","Column2":"2349548F0DB54E0B9BD2EA32C00B0E26"}]
     * Msg :
     */

    private int State;
    private String Data;
    private String Msg;

    public void setState(int State) {
        this.State = State;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getState() {
        return State;
    }

    public String getData() {
        return Data;
    }

    public String getMsg() {
        return Msg;
    }
}
