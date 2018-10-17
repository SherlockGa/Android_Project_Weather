package cn.edu.pku.ga.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.ga.bean.City;

public class CityDB {
    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db;

    public CityDB(Context context, String path){
        db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null); //创建数据库
    }

    public List<City> getAllCity() {
        List<City> list = new ArrayList<City>();
        Cursor c = db.rawQuery("SELECT * from "+ CITY_TABLE_NAME, null); //查询得到数据集合c
        while (c.moveToNext()){ //逐行遍历，得到每一列的值
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));

            City item = new City(province, city, number, firstPY, allPY, allFirstPY); //将每一列的值放入构造函数，生成一个city
            list.add(item);
        }
        return list;
    }
}
