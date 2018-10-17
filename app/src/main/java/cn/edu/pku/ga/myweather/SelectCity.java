package cn.edu.pku.ga.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SelectCity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn .setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra("cityCode", "101160101"); //为Intent附加一些要传回MainActivity的数据
                setResult(RESULT_OK, i); //在finish()之前，用setResult()函数将数据送回MainActivity.onActivityResult函数
                finish(); //发消息结束
                break;
            default:
                break;
        }
    }
}
