package cn.edu.pku.ga.myweather;

import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.pku.ga.bean.TodayWeather;
import cn.edu.pku.ga.util.NetUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{ //主线程（UI线程）——能直接操纵UI控件

    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mUpdateBtn;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    //Handler处理来自子线程的消息
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER : //子线程中点击更新了天气
                    updateTodayWeather((TodayWeather)msg.obj); //调用更新天气的函数，参数是子线程传过来的
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this) !=  NetUtil.NETWORN_NONE) {
            Log.d("myWeather","ok");
            Toast.makeText(MainActivity.this,"网络Ok",Toast.LENGTH_LONG).show();
        }else {
            Log.d("myWeather","网络挂了");
            Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
        }

        initView();
    }

    //初始化控件
    void initView(){
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }

    void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+"发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力"+todayWeather.getFengli());

        int pm25;
        if (todayWeather.getPm25() == null)
            pm25 = 0;
        else
            pm25 = Integer.parseInt(todayWeather.getPm25());

        Log.d("MyWeather","pm25="+pm25);
        if (pm25 >= 0 && pm25 <= 50){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }
        else if (pm25 >= 51 && pm25 <= 100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        }
        else if (pm25 >= 101 && pm25 <= 150){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        }
        else if (pm25 >= 151 && pm25 <= 200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }
        else if (pm25 >= 201 && pm25 <= 300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }
        else if (pm25 > 300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }

        Log.d("MyWeather",todayWeather.getType());

        if (todayWeather.getType().equals("晴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        }
        else if (todayWeather.getType().equals("暴雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }
        else if (todayWeather.getType().equals("大雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }
        else if (todayWeather.getType().equals("中雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }
        else if (todayWeather.getType().equals("小雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }
        else if (todayWeather.getType().equals("阵雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }
        else if (todayWeather.getType().equals("雨夹雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }
        else if (todayWeather.getType().equals("暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }
        else if (todayWeather.getType().equals("大暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }
        else if (todayWeather.getType().equals("特大暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }
        else if (todayWeather.getType().equals("大雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }
        else if (todayWeather.getType().equals("雷阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }
        else if (todayWeather.getType().equals("阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }
        else if (todayWeather.getType().equals("中雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }
        else if (todayWeather.getType().equals("小雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }
        else if (todayWeather.getType().equals("多云")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }
        else if (todayWeather.getType().equals("沙尘暴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }
        else if (todayWeather.getType().equals("雷阵雨冰雹")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }
        else if (todayWeather.getType().equals("雾")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        }
        else if (todayWeather.getType().equals("阴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        }

        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }

    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather",address);
        //子线程
        new Thread( new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address); //构造一个URL对象
                    con = (HttpURLConnection)url.openConnection(); //通过URL使用HttpURLConnection打开连接
                    con.setRequestMethod("GET"); //使用GET方法
                    con.setConnectTimeout(8000); //设置连接超时的毫秒
                    con.setReadTimeout(8000); //设置读取超时的毫秒
                    InputStream in = con.getInputStream();//得到网络返回的内容（流）
                    //为输出创建BufferedReader
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    //使用循环来读取获得的数据
                    while ((str = reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather",str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather",responseStr);

                    //获取网络数据后调用解析函数
                    TodayWeather todayWeather1 = todayWeather.parseXML(responseStr);
                    if (todayWeather1 != null){
                        Log.d("myWeather",todayWeather1.toString());

                        Message msg = new Message(); //创建消息
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather1; //需要传给主线程的内容
                        mHandler.sendMessage(msg); //发消息给主线程
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (con != null){
                        con.disconnect();
                    }
                }
            }
        } ).start(); //启动线程
    }

    //为更新按钮添加单击事件
    @Override
    public void onClick(View view){
        if (view.getId() == R.id.title_update_btn){
            //SharedPreference中取出存储的页面数据
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            //SharedPreference中读取城市id，如果没有定义则用缺省值
            String cityCode = sharedPreferences.getString("main_city_code","101020100");
            Log.d("myWeather",cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络OK");
                queryWeatherCode(cityCode); //点击查询更新页面
            }else{
                Log.d("myWeather","网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
            }
        }
    }
}
