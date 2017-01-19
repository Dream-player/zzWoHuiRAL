package whcs.wohui.zz.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import whcs.wohui.zz.Bean.ShopCollectData;
import whcs.wohui.zz.Bean.ShopDetailBean;


/**
 * 说明：商店收藏数据会保存在数据库中，该类为收藏工具类，内部封装一个SQLiteOpenHelper内部类
 * 作者：陈杰宇
 * 时间： 2016/5/5 10:01
 * 版本：V1.0
 * 修改历史：
 */
public class ShopCellectDBHelper {

    private final MySQLHelper myHelper;
    private String tableName;
    public ShopCellectDBHelper(Context ctx){
        tableName ="ShopCollection";
        myHelper = new MySQLHelper(ctx,"ShopCollection.db",null,5);
    }

    /**
     * 收藏商店
     * @param data 参数为商店详情
     */
    public void shopCollectAdd(ShopDetailBean.DataBean data){
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("SerialNumber",data.getS_GUID());
        cValues.put("NameSM",data.getS_Name());
        cValues.put("AddrArea",data.getSI_Province());
        cValues.put("AddrCity",data.getSI_City());
        cValues.put("AddrDistrict",data.getSI_District());
        cValues.put("AddrTown",data.getSI_Towns()+"*");
        cValues.put("AddrDetail",data.getSI_Address());
        cValues.put("MapLongitude",data.getS_Long());
        cValues.put("MapLatitude",data.getS_Lat());
        db.insert(tableName,null,cValues);
        db.close();
    }

    /**
     * 取消收藏
     * @param _id 参数为该收藏在数据库中的唯一标示 _id
     */
    public void shopCollectDecrease(int _id){
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String sql = "delete from "+tableName+" where _id = "+_id;
        db.execSQL(sql);
        db.close();
    }
    public void shopCollectDecrease(String shopSerialNumber){
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String sql = "delete from "+tableName+" where SerialNumber = "+"'"+shopSerialNumber+"'";
        db.execSQL(sql);
        db.close();
    }
    /**
     * 判断是否收藏了该商店
     * @param numberSM 商店编号
     * @return
     */
    public boolean isCollect(String numberSM){
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //根据商品序号得到数据库中对应的商品
        String where = "SerialNumber='"+numberSM+"'";
        Cursor cursor = db.query(tableName, null, where, null, null, null, null);
        int i = cursor.getCount();
        LogUtils.e("数据数量，"+i);
        db.close();
        if(i==0){
            return false;
        }else if (i==1){
            return true;
        }else {
            LogUtils.e("商店重复收藏");
            return true;
        }
    }

    /**
     * 得到所有收藏的商店
     * @return
     */
    public List<ShopCollectData> getAllShop(){
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String orderBy = "_id desc";
        List<ShopCollectData> dataList = new ArrayList<>();
        Cursor cursor = db.query(tableName, null, null, null, null,null,orderBy);
        int count = cursor.getCount();
        if(count>0){
            for (int i=0;i<count;i++){
                ShopCollectData data = new ShopCollectData();
                cursor.moveToPosition(i);
                data.set_id(cursor.getInt(0));
                data.setSerialNumber(cursor.getString(1));
                data.setNameSM(cursor.getString(2));
                data.setAddrArea(cursor.getString(3)
                        +cursor.getString(4)
                        +cursor.getString(5)
                        +cursor.getString(6));

                data.setAddrDetail(cursor.getString(7));
                data.setMapLongitude(cursor.getString(8));
                data.setMapLatitude(cursor.getString(9));
                dataList.add(data);
            }
        }
        db.close();
        return dataList;
    }

    private class MySQLHelper extends SQLiteOpenHelper{

        public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String table = "create table "+tableName
                    +"(_id integer primary key autoincrement,"
                    +"SerialNumber text,"
                    +"NameSM,"
                    +"AddrArea,"
                    +"AddrCity,"
                    +"AddrDistrict,"
                    +"AddrTown,"
                    +"AddrDetail,"
                    +"MapLongitude,"
                    +"MapLatitude)";
                    db.execSQL(table);
            LogUtils.e("MySQLHelper", "onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion>oldVersion){
                String table = "create table if not exists "+tableName
                        +"(_id integer primary key autoincrement,"
                        +"SerialNumber text,"
                        +"NameSM,"
                        +"AddrArea,"
                        +"AddrCity,"
                        +"AddrDistrict,"
                        +"AddrTown,"
                        +"AddrDetail,"
                        +"MapLongitude,"
                        +"MapLatitude)";
                db.execSQL(table);
                LogUtils.e("MySQLHelper", "onUpgrade");
            }
            LogUtils.e("MySQLHelper","onUpgrade");
        }
    }
}
