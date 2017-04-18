package whcs.wohui.zz.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import whcs.wohui.zz.Bean.DBgoodsData;
import whcs.wohui.zz.Bean.GoodsListBean;
import whcs.wohui.zz.Bean.GoodsListBean.DataEntity.GoodsEntity;


/**
 * 说明：操纵购物车数据库工具类
 * 作者：刘志海
 * 时间： 2017/4/8 9:37
 * 版本：
 * 修改历史：
 */
public class ShopCartDatabaseHelper {
    private static final String TAG = "ShopCartDatabaseHelper";
    private MySQLHelper myHelper;
    private Context ctx;
    private String user;
    private String shopSerialNo;
    private final String sql;

    public ShopCartDatabaseHelper(String user,String shopSerialNo,Context context){
        this.user=user;
        this.ctx = context;
        shopSerialNo = shopSerialNo.replace("-","x");
        shopSerialNo = "x"+shopSerialNo;
        this.shopSerialNo = shopSerialNo;
        sql = user+"a.db";
        myHelper = new MySQLHelper(context,sql,null,1);
    }

    /**
     * 添加数据库中对应购物车商品
     *
     */
    public  void goodsAdd(GoodsListBean.DataEntity.GoodsEntity entity){
        //获取商品编号
        String goodsSerialNo = entity.getSMC_GUID();
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        LogUtils.e("添加数据库，判断商品序号"+goodsSerialNo);
        //根据商品序号得到数据库中对应的商品
        String where = "NumberComm = '"+goodsSerialNo+"'";
        Cursor cursor = db.query(shopSerialNo,null,where,null,null,null,null);
        //判断数据库中是否有商品序号所对应的商品
        int count = cursor.getCount();
        LogUtils.e("添加游标数"+count+where);

        if(count==0){
            ContentValues cValues = new ContentValues();
            cValues.put("SerialNumber",entity.getCS_GUID());
            cValues.put("NumberSM",entity.getSMC_SMGUID());
            cValues.put("NumberComm",goodsSerialNo);
            cValues.put("PriceUnit",entity.getSMC_UnitPrice());
            cValues.put("Integral",entity.getSMC_Integral());
            cValues.put("CountCollected",entity.getSMC_CollectNum());
            cValues.put("CountSaleTotal",entity.getSMC_SalesNum());
            cValues.put("CountStockTotal",entity.getSMC_StockNum());
            cValues.put("NameComm",entity.getCS_Name());
            cValues.put("BriefProduct",entity.getCS_Name());
            cValues.put("UrlListImage",entity.getCS_MainImg());
            cValues.put("OrderCount", 1);
            LogUtils.e(cValues.getAsString("NumberSM") + "添加的NumberSM");
            LogUtils.e(shopSerialNo);

            long l = db.insert(shopSerialNo, null, cValues);
            LogUtils.e(l+"添加返回："+l);
            cValues.clear();

        }else if(count==1){
            //得到所要购买商品的数量
            cursor.moveToFirst();
            Integer orderCount = cursor.getInt(12);

            orderCount = orderCount+1;
            //修该数据表中改行数据
            String sql = "update "+shopSerialNo+" set OrderCount = "+orderCount+" WHERE "+where;
            db.execSQL(sql);
        }else {
            LogUtils.e("数据表添加错误+++++++++++++++++++++++++++++++++++++++++++");
        }
        db.close();
    }
    public  void goodsAdd(DBgoodsData entity){
        //获取商品编号
        String goodsSerialNo = entity.getNumberComm();
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        LogUtils.e("添加数据库，判断商品序号"+goodsSerialNo);
        //根据商品序号得到数据库中对应的商品
        String where = "NumberComm = '"+goodsSerialNo+"'";
        Cursor cursor = db.query(shopSerialNo,null,where,null,null,null,null);
        //判断数据库中是否有商品序号所对应的商品
        int count = cursor.getCount();
        LogUtils.e("添加游标数"+count+where);
        if(count==0){
            ContentValues cValues = new ContentValues();
            cValues.put("SerialNumber",entity.getSerialNumber());
            cValues.put("NumberSM",entity.getNumberSM());
            cValues.put("NumberComm",goodsSerialNo);
            cValues.put("PriceUnit",entity.getPriceUnit());
            cValues.put("Integral",entity.getIntegral());
            cValues.put("CountCollected",entity.getCountCollected());
            cValues.put("CountSaleTotal",entity.getCountSaleTotal());
            cValues.put("CountStockTotal",entity.getCountStockTotal());
            cValues.put("NameComm",entity.getNameComm());
            cValues.put("BriefProduct",entity.getBriefProduct());
            cValues.put("UrlListImage",entity.getUrlListImage());
            cValues.put("OrderCount", 1);
            LogUtils.e(cValues.getAsString("NumberSM") + "添加的NumberSM");
            LogUtils.e(shopSerialNo);

            long l = db.insert(shopSerialNo, null, cValues);
            LogUtils.e(l+"添加返回："+l);
            cValues.clear();

        }else if(count==1){
            //得到所要购买商品的数量
            cursor.moveToFirst();
            Integer orderCount = cursor.getInt(12);

            orderCount = orderCount+1;
            //修该数据表中改行数据
            String sql = "update "+shopSerialNo+" set OrderCount = "+orderCount+" WHERE "+where;
            db.execSQL(sql);
        }else {
            LogUtils.e("数据表添加错误+++++++++++++++++++++++++++++++++++++++++++");
        }
        db.close();
    }
    /**
     * 减少数据库中对应购物车商品
     * @param entity
     */
    public void goodsDecrease(GoodsEntity entity){
        //获取商品编号
        String goodsSerialNo = entity.getSMC_GUID();
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //根据商品序号得到数据库中对应的商品
        String where = "NumberComm = '"+goodsSerialNo+"'";
        Cursor cursor = db.query(shopSerialNo,null,where,null,null,null,null);
        //判断数据库中是否有商品序号所对应的商品
        int count = cursor.getCount();
        LogUtils.e("减少数据库，游标数："+count);
        if(count==1){
            //得到所要购买商品的数量
            cursor.moveToFirst();
            Integer orderCount = cursor.getInt(12);
            LogUtils.e("商品单价:"+"float:"+cursor.getFloat(4)+"double"+cursor.getDouble(4));
            if(orderCount==1){//删除数据表中该行数据
                String sql = "DELETE FROM "+shopSerialNo+" WHERE "+where;
                db.execSQL(sql);
                LogUtils.e("删除该条目————————————————————————");
            }else if (orderCount>1){//修该数据表中改行数据
                orderCount = orderCount-1;
                String sql = "update "+shopSerialNo+" set OrderCount = "+orderCount+" WHERE "+where;
                db.execSQL(sql);
            }else {
                LogUtils.e("_____________商品数量"+orderCount);
            }
        }else if (count==0){
            LogUtils.e("数据表减少，没有该商品");
        }
        db.close();
    }
    public void goodsDecrease(DBgoodsData entity){
        //获取商品编号
        String goodsSerialNo = entity.getNumberComm();
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //根据商品序号得到数据库中对应的商品
        String where = "NumberComm = '"+goodsSerialNo+"'";
        Cursor cursor = db.query(shopSerialNo,null,where,null,null,null,null);
        //判断数据库中是否有商品序号所对应的商品
        int count = cursor.getCount();
        LogUtils.e("减少数据库，游标数："+count);
        if(count==1){
            //得到所要购买商品的数量
            cursor.moveToFirst();
            Integer orderCount = cursor.getInt(12);
            if(orderCount==1){//删除数据表中该行数据
                String sql = "DELETE FROM "+shopSerialNo+" WHERE "+where;
                db.execSQL(sql);
                LogUtils.e("删除该条目————————————————————————");
            }else if (orderCount>1){//修该数据表中改行数据
                orderCount = orderCount-1;
                String sql = "update "+shopSerialNo+" set OrderCount = "+orderCount+" WHERE "+where;
                db.execSQL(sql);
            }else {
                LogUtils.e("_____________商品数量"+orderCount);
            }
        }else if (count==0){
            LogUtils.e("数据表减少，没有该商品");
        }
        db.close();
    }

    /**
     * 查询数据库中代表购物车的某件商品
     * @param goodsSerialNo
     * @return
     */
    public DBgoodsData goodsQuery(String goodsSerialNo){
        //获取数据库对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        //根据商品序号得到数据库中对应的商品
        String where = "NumberComm='"+goodsSerialNo+"'";
        Cursor cursor = db.query(shopSerialNo,null,where,null,null,null,null);
        DBgoodsData dBgoodsData =  new DBgoodsData();
        if(cursor.getCount()==1){
            cursor.moveToFirst();
            dBgoodsData.setSerialNumber(cursor.getString(1));
            dBgoodsData.setNumberSM(cursor.getString(2));
            dBgoodsData.setNumberComm(cursor.getString(3));
            dBgoodsData.setPriceUnit(cursor.getDouble(4));
            dBgoodsData.setIntegral(cursor.getDouble(5));
            dBgoodsData.setCountCollected(cursor.getInt(6));
            dBgoodsData.setCountSaleTotal(cursor.getInt(7));
            dBgoodsData.setCountStockTotal(cursor.getInt(8));
            dBgoodsData.setNameComm(cursor.getString(9));
            dBgoodsData.setBriefProduct(cursor.getString(10));
            dBgoodsData.setUrlListImage(cursor.getString(11));
            dBgoodsData.setOrderCount(cursor.getInt(12));
            db.close();
            return dBgoodsData;
        }else {
            db.close();
            return null;
        }

    }

    /**
     * 获得数据库中，添加到购物车商品中的数量，如果没有则为0；
     * @param goodsList
     * @return
     */
    public List<Integer> getGoodsOrderCount(List<GoodsEntity> goodsList){
        ArrayList<Integer> countList = new ArrayList<>();
        if(goodsList.size()==0){

        }else {
            SQLiteDatabase db = myHelper.getReadableDatabase();
            for (GoodsEntity entity:goodsList){
                String goodsSerialNo =entity.getSMC_GUID();
                //根据商品序号得到数据库中对应的商品
                String where = "NumberComm='"+goodsSerialNo+"'";
                Cursor cursor = db.query(shopSerialNo,new String[]{"OrderCount"},where,null,null,null,null);
                if(cursor.moveToFirst()){
                    countList.add(cursor.getInt(0));
                }else {
                    countList.add(0);
                }
            }
            db.close();
        }
        return countList;
    }

    public int getAllGoodsNumber(){
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        //查询获得游标
        Cursor cursor = db.query (shopSerialNo,null,null,null,null,null,null);
        int number = cursor.getCount();
        LogUtils.e(TAG,"getAllGoodsNumber:"+number);
        db.close();
        return number;
    }
    /**
     * 得到所有商品
     * @return
     */
    public List<DBgoodsData> getAllGoods(){
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        LogUtils.e("运行到得到所有数据");
        //查询获得游标
        Cursor cursor = db.query (shopSerialNo,null,null,null,null,null,null);
        List<DBgoodsData> dataList = new ArrayList<>();
        if(cursor.moveToFirst()){

            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                DBgoodsData dBgoodsData = new DBgoodsData();
                dBgoodsData.setSerialNumber(cursor.getString(1));
                dBgoodsData.setNumberSM(cursor.getString(2));
                dBgoodsData.setNumberComm(cursor.getString(3));
                dBgoodsData.setPriceUnit(cursor.getDouble(4));
                dBgoodsData.setIntegral(cursor.getDouble(5));
                dBgoodsData.setCountCollected(cursor.getInt(6));
                dBgoodsData.setCountSaleTotal(cursor.getInt(7));
                dBgoodsData.setCountStockTotal(cursor.getInt(8));
                dBgoodsData.setNameComm(cursor.getString(9));
                dBgoodsData.setBriefProduct(cursor.getString(10));
                dBgoodsData.setUrlListImage(cursor.getString(11));
                dBgoodsData.setOrderCount(cursor.getInt(12));
                dataList.add(dBgoodsData);
            }
        }
        db.close();
        return dataList;
    }

    /**
     * 清空数据表
     */
    public void clearAllGoods(){
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String sql = "DELETE FROM "+shopSerialNo;
        db.execSQL(sql);
        db.close();
    }

    /**
     * 删除商品编号对应的行
     * @param goodsSerialNo
     */
    public void clearGoods(String goodsSerialNo){
        //获取数据库对象
        SQLiteDatabase db = myHelper.getWritableDatabase();
        String where = " WHERE NumberComm = '"+goodsSerialNo+"'";
        String sql = "DELETE FROM "+shopSerialNo+where;
        db.execSQL(sql);
        db.close();
    }

    public void setShopSerialNo(String shopSerialNo) {
        shopSerialNo = shopSerialNo.replace("-","x");
        shopSerialNo = "x"+shopSerialNo;
        this.shopSerialNo = shopSerialNo;
        myHelper = new MySQLHelper(ctx, sql,null,1);
    }
    public double getTotalPrice(){
        List<DBgoodsData> goodsList = getAllGoods();
        double price = 0;
        for (DBgoodsData data :goodsList){
            price = MyUtils.add(price,MyUtils.mul(data.getOrderCount(),data.getPriceUnit()));
        }
        return price;
    }
    /**
     * 手动创建表格
     */
    public void createTable(){
        SQLiteDatabase db= myHelper.getWritableDatabase();
        String table = "create table if not exists "
                +shopSerialNo+
                "(_id integer primary key autoincrement,"
                +"SerialNumber text,NumberSM text,"
                +"NumberComm text,"
                +"PriceUnit real,"
                +"Integral real,"
                +"CountCollected integer,"
                +"CountSaleTotal integer,"
                +"CountStockTotal integer,"
                +"NameComm text,"
                +"BriefProduct text,"
                +"UrlListImage text,"
                +"OrderCount integer)";
        db.execSQL(table);
        db.close();
        LogUtils.e(shopSerialNo+"++++++*******创建表格**************++");
    }
    private class MySQLHelper extends SQLiteOpenHelper{
        public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
             String table = "create table if not exists "
                    +shopSerialNo+
                    "(_id integer primary key autoincrement,"
                    +"SerialNumber text,NumberSM text,"
                    +"NumberComm text,"
                    +"PriceUnit real,"
                    +"Integral real,"
                    +"CountCollected integer,"
                    +"CountSaleTotal integer,"
                    +"CountStockTotal integer,"
                    +"NameComm text,"
                    +"BriefProduct text,"
                    +"UrlListImage text,"
                    +"OrderCount integer)";
            db.execSQL(table);
            LogUtils.e(shopSerialNo+"++++++*******创建表格**************++");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
