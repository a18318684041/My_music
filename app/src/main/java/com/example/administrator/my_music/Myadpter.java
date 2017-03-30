package com.example.administrator.my_music;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/3/28 0028.
 */
public class Myadpter extends BaseAdapter{

    private Context context;
    private List<String> singers;
    private List<String>  songs;
    private List<String> imgurl;
    public Myadpter(Context context,List<String> singers,List<String> songs,List<String> imgurl){
        this.context = context;
        this.singers = singers;
        this.songs = songs;
        this.imgurl = imgurl;

    }

    public void changData(List<String> singers,List<String>  songs, List<String> imgurl) {
        this.singers = singers;
        this.songs = songs;
        this.imgurl = imgurl;

        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return singers.size();
    }

    @Override
    public Object getItem(int position) {
        return singers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView tv_songs = (TextView) convertView.findViewById(R.id.songs);
        tv_songs.setText(songs.get(position));
        TextView tv_singer = (TextView) convertView.findViewById(R.id.singer);
        tv_singer.setText(singers.get(position));
        final ImageView img = (ImageView) convertView.findViewById(R.id.head);

        //将网络图片加载出来
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .get()//
                        .url(imgurl.get(position))//
                        .build()//
                        .execute(new BitmapCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(Bitmap response, int id) {
                                img.setImageBitmap(response);
                            }
                        });

            }
        }).start();
        return convertView;
    }

}
