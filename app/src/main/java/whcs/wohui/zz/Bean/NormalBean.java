package whcs.wohui.zz.Bean;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/3/8 14:05
 * 版本：V1.0
 * 修改历史：
 */
public class NormalBean {

    /**
     * State : 1
     * Data : 0E1FCE4A1851483CBFE60E0075355B4D
     * Msg : 登陆成功
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

    @Override
    public String toString() {
        return "NormalBean{" +
                "State=" + State +
                ", Data='" + Data + '\'' +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
