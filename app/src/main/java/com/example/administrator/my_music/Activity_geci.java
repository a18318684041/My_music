package com.example.administrator.my_music;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Activity_geci extends AppCompatActivity  {


    private ImageView img;
    private TextView tv_geci;
    private  String url = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geci);


        //接受主页面传来歌词的地址

        initView();
        Intent intent  = getIntent();
        url = intent.getStringExtra("url");
        tv_geci.setText(url);
    }

    private void initView() {

        img = (ImageView) findViewById(R.id.fanhui);
        tv_geci  = (TextView) findViewById(R.id.geci);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Activity_geci.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
