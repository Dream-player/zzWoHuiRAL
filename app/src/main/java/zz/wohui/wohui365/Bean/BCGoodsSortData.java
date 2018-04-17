package zz.wohui.wohui365.Bean;

import java.util.List;

/**
 * 说明：
 * 作者：陈杰宇
 * 时间： 2016/4/21 14:47
 * 版本：V1.0
 * 修改历史：
 */
public class BCGoodsSortData {

    private String CC_GUID;
    private String CC_Name;
    private String CC_ParentGUID;
    private Object CC_Icon;
    private int CC_Level;
    private String CC_Sort;
    private int CC_Count;
    private List<GoodsSortData> CGoodsSortList;

    public List<GoodsSortData> getCGoodsSortList() {
        return CGoodsSortList;
    }

    public void setCGoodsSortList(List<GoodsSortData> CGoodsSortList) {
        this.CGoodsSortList = CGoodsSortList;
    }

    public int getCC_Count() {
        return CC_Count;
    }

    public void setCC_Count(int CC_Count) {
        this.CC_Count = CC_Count;
    }

    public String getCC_GUID() {
        return CC_GUID;
    }

    public void setCC_GUID(String CC_GUID) {
        this.CC_GUID = CC_GUID;
    }

    public String getCC_Name() {
        return CC_Name;
    }

    public void setCC_Name(String CC_Name) {
        this.CC_Name = CC_Name;
    }

    public String getCC_ParentGUID() {
        return CC_ParentGUID;
    }

    public void setCC_ParentGUID(String CC_ParentGUID) {
        this.CC_ParentGUID = CC_ParentGUID;
    }

    public Object getCC_Icon() {
        return CC_Icon;
    }

    public void setCC_Icon(Object CC_Icon) {
        this.CC_Icon = CC_Icon;
    }

    public int getCC_Level() {
        return CC_Level;
    }

    public void setCC_Level(int CC_Level) {
        this.CC_Level = CC_Level;
    }

    public String getCC_Sort() {
        return CC_Sort;
    }

    public void setCC_Sort(String CC_Sort) {
        this.CC_Sort = CC_Sort;
    }
}
