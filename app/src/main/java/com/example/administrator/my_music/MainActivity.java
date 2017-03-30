package com.example.administrator.my_music;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    //实现在线抓取音乐的功能
    private EditText edt_search;
    private Button btn_search;
    private TextView tv_result;
    private ListView listView;
    private Myadpter myadpter;
    private List<String> songs = new ArrayList<>();
    private List<String> singers = new ArrayList<>();
    private List<String>  download = new ArrayList<>();
    private MediaPlayer mediaPlayer;

    //专辑图片地址
    private List<String> imgurl = new ArrayList<>();
    //专辑歌词地址;
    private List<String> lyricurl = new ArrayList<>();



    private List<String> s1 = new ArrayList<>();
    private List<String> s2 = new ArrayList<>();
    private List<String> s3 = new ArrayList<>();



    String  output = "";

    //搜索关键字地址
    public static  String KEY_SEARCH_URL = "http://www.xiami.com/search/song?key=";
    //ID接口
    public  static  String  ID_SEARCH_URL ="http://www.xiami.com/song/playlist/id/";

    Document document = null;
    List<String> ids = new ArrayList<String>();
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        initView();

    }

    private void initView() {
        edt_search = (EditText) findViewById(R.id.edt_search);
        btn_search = (Button) findViewById(R.id.btn);
        listView = (ListView) findViewById(R.id.listview);
        tv_result = (TextView) findViewById(R.id.tv_result);

        myadpter = new Myadpter(MainActivity.this,singers,songs,imgurl);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String input = edt_search.getText().toString();
                            KEY_SEARCH_URL +=input;
                            document = Jsoup.connect(KEY_SEARCH_URL).get();
                            Elements elements =  document.getElementsByClass("track_list");
                            if(elements.size()!=0){
                                Elements all = elements.get(0).getElementsByClass("chkbox");
                                int size = all.size();
                                for (int i = 0; i <size ; i++) {
                                    String id = all.get(i).select("input").attr("value");
                                    ids.add(id);
                                    result += id + " "+"\n";
                                }
                            }
                            singers.clear();
                            songs.clear();
                            imgurl.clear();


                            //获取歌曲图片，歌名，以及歌手
                            int  idsize = ids.size();
                            for(int  i = 0;i<idsize;i++){
                                String postUrl = ID_SEARCH_URL + ids.get(i);
                                Document d = Jsoup.connect(postUrl).get();
                                Elements element = d.select("trackList");
                                for(Element e:element ){
                                    singers.add(e.select("songName").text());
                                    songs.add(e.select("artist").text());
                                    download.add(StringUtils.decodeMusicUrl(e.select(
                                            "location").text()));
                                    imgurl.add(e.select("pic").text());
                                    lyricurl.add(e.select("lyric").text());
                                    Log.d("AAA",e.select("pic").text());
                                }
                            }

                           /* myadpter.changData(singers,songs,imgurl);*/

                            //原本的位置


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //myadpter.notifyDataSetChanged();
                                    myadpter = new Myadpter(MainActivity.this,singers,songs,imgurl);

                                    listView.setAdapter(myadpter);
                                    myadpter.notifyDataSetChanged();
                                    result="";
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String url = download.get(position);

                                String geciurl = lyricurl.get(position);
                                try {
     /*                               mediaPlayer.reset();*/
                                    mediaPlayer.setDataSource(url);
                                    mediaPlayer.prepare();;
                                    mediaPlayer.start();


                                    Intent intent = new Intent();
                                    intent.putExtra("url",geciurl);
                                    Log.d("BBB", geciurl);
                                    intent.setClass(MainActivity.this,Activity_geci.class);
                                    startActivity(intent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }
    //截取出歌曲名字
    public String  subSong(String song_name){
           song_name =  song_name.substring(8,-3);
        return song_name;
    }
}
