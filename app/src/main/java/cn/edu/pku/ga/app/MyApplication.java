package cn.edu.pku.ga.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.ga.bean.City;
import cn.edu.pku.ga.db.CityDB;

public class MyApplication extends Application{
    private static final String TAG = "MyAPP";

    private static MyApplication mApplication;
    private CityDB mCityDB;

    private List<City> mCityList;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"MyApplication->Oncreate");

        mApplication = this;
        mCityDB = openCityDB(); //调用打开复制的数据库的函数

        initCityList(); //初始化城市列表

    }

    public static MyApplication getInstance(){
        return mApplication;
    }

    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }
        ).start();
    }

    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity(); //获取所有城市信息
        int i = 0;
        for (City city : mCityList){
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"i="+i);
        return true;
    }

    public List<City> getCityList() {
        return mCityList;
    }

    private CityDB openCityDB(){
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "db_copy"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG,path);
        if (!db.exists()){ //这条路径上没有数据库db_copy时
            //创建
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "db_copy"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if (!dirFirstFolder.exists()){ //这条路径上没有这个db_copy文件夹时
                //创建文件夹
                dirFirstFolder.mkdirs();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try {
                InputStream is = getAssets().open("city.db"); //打开city.db
                FileOutputStream fos = new FileOutputStream(db); //打开准备复制的db
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1){ //将city.db的内容复制到db_copy中
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }
}
