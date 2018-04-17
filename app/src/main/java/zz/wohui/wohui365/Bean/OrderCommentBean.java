package zz.wohui.wohui365.Bean;

/**
 * 说明：
 * 作者：朱世元
 * 时间： 2016/9/13 11:09
 * 版本：V1.0
 * 修改历史：
 */
public class OrderCommentBean {

    /**
     * State : 1
     * Data : {"S_Name":"我惠期间超市","OC_GUID":"F5E0D05F2C004711B23BEEF1DCF7FF08","OC_OrderGUID":"E75F886C3FF5467E83B3B564BACEA4A0","OC_SMGUID":"4E1AFD6B196341E68C49C1B3AB07E30F","OC_UserGUID":"702876A0882E4E989CE1CFC867E64396","OC_Score":4,"OC_CommentMsg":"fjksdjlkfdjksdjlkfjk房间打开了附件里三级分类束带结发","OC_ReplyMsg":"","OC_IsDelete":false}
     * Msg :
     */

    private int State;
    /**
     * S_Name : 我惠期间超市
     * OC_GUID : F5E0D05F2C004711B23BEEF1DCF7FF08
     * OC_OrderGUID : E75F886C3FF5467E83B3B564BACEA4A0
     * OC_SMGUID : 4E1AFD6B196341E68C49C1B3AB07E30F
     * OC_UserGUID : 702876A0882E4E989CE1CFC867E64396
     * OC_Score : 4
     * OC_CommentMsg : fjksdjlkfdjksdjlkfjk房间打开了附件里三级分类束带结发
     * OC_ReplyMsg :
     * OC_IsDelete : false
     */

    private DataEntity Data;
    private String Msg;

    public void setState(int State) {
        this.State = State;
    }

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public int getState() {
        return State;
    }

    public DataEntity getData() {
        return Data;
    }

    public String getMsg() {
        return Msg;
    }

    public static class DataEntity {
        private String S_Name;
        private String OC_GUID;
        private String OC_OrderGUID;
        private String OC_SMGUID;
        private String OC_UserGUID;
        private float OC_Score;
        private String OC_CommentMsg;
        private String OC_ReplyMsg;
        private boolean OC_IsDelete;

        public void setS_Name(String S_Name) {
            this.S_Name = S_Name;
        }

        public void setOC_GUID(String OC_GUID) {
            this.OC_GUID = OC_GUID;
        }

        public void setOC_OrderGUID(String OC_OrderGUID) {
            this.OC_OrderGUID = OC_OrderGUID;
        }

        public void setOC_SMGUID(String OC_SMGUID) {
            this.OC_SMGUID = OC_SMGUID;
        }

        public void setOC_UserGUID(String OC_UserGUID) {
            this.OC_UserGUID = OC_UserGUID;
        }

        public void setOC_Score(float OC_Score) {
            this.OC_Score = OC_Score;
        }

        public void setOC_CommentMsg(String OC_CommentMsg) {
            this.OC_CommentMsg = OC_CommentMsg;
        }

        public void setOC_ReplyMsg(String OC_ReplyMsg) {
            this.OC_ReplyMsg = OC_ReplyMsg;
        }

        public void setOC_IsDelete(boolean OC_IsDelete) {
            this.OC_IsDelete = OC_IsDelete;
        }

        public String getS_Name() {
            return S_Name;
        }

        public String getOC_GUID() {
            return OC_GUID;
        }

        public String getOC_OrderGUID() {
            return OC_OrderGUID;
        }

        public String getOC_SMGUID() {
            return OC_SMGUID;
        }

        public String getOC_UserGUID() {
            return OC_UserGUID;
        }

        public float getOC_Score() {
            return OC_Score;
        }

        public String getOC_CommentMsg() {
            return OC_CommentMsg;
        }

        public String getOC_ReplyMsg() {
            return OC_ReplyMsg;
        }

        public boolean isOC_IsDelete() {
            return OC_IsDelete;
        }
    }
}
