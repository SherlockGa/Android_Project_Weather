package cn.edu.pku.ga.myweather;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.ga.app.MyApplication;
import cn.edu.pku.ga.bean.City;

public class SelectCity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackBtn;

    private ListView mlistView;

    private String[] data={"第1组","第2组","第3组","第4组","第5组","第6组","第7组","第8组"
            ,"第9组","第10组","第11组","第12组","第13组","第14组","第15组","第16组","第17组","第18组"
            ,"第19组","第20组","第21组","第22组","第23组","第24组","第25组","第26组","第27组","第28组"};

    private String[] name = new String[2587];
    private String[] code = new String[2587];

    private TextView nowcityname;
    private List<City> mcitylist;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn .setOnClickListener(this);

        MyApplication mApplication = (MyApplication) getApplication();
        mcitylist = mApplication.getCityList();

        int i = 0;
        for (City city : mcitylist){
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            name[i] = cityName;
            code[i] = cityCode;
            i++;
        }


        final List<Map<String,Object>> listems = new ArrayList<Map<String,Object>>();
        for(i = 0; i < name.length; i++){
            Map<String,Object> listem = new HashMap<String, Object>();
            listem.put("name",name[i]);
            listems.add(listem);
        }

        mlistView = (ListView) findViewById(R.id.list_view);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listems,R.layout.item,
                new String[]{"name"},new int[]{R.id.name});
        mlistView.setAdapter(simpleAdapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SelectCity.this, "你单击了："+name[position], Toast.LENGTH_SHORT).show();
                //nowcityname = (TextView) findViewById(R.id.title_name);
               // nowcityname.setText("当前城市："+name[position]);
                showInfo(name[position],code[position]);
            }
        });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                Intent i = new Intent();
                SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
                String code = sharedPreferences.getString("main_city_code","101010100");
                Log.d("WACC",code);
                i.putExtra("cityCode", code); //为Intent附加一些要传回MainActivity的数据
                setResult(RESULT_OK, i); //在finish()之前，用setResult()函数将数据送回MainActivity.onActivityResult函数
                finish(); //发消息结束
                break;
            default:
                break;
        }
    }

    public void showInfo(final String city, final String code){
        new AlertDialog.Builder(this).setMessage("你确定选择:"+city)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nowcityname = (TextView) findViewById(R.id.title_name);
                        nowcityname.setText("当前城市："+city);
                        SharedPreferences settings = (SharedPreferences) getSharedPreferences("config", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("main_city_code",code);
                        editor.commit();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


}
