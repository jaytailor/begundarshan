package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.List;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.YoutubeVideo;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    Context c;
    List<YoutubeVideo> youtubeVideos;

    public VideoAdapter() {
    }

    public VideoAdapter(Context c, List<YoutubeVideo> youtubeVideos) {
        this.c = c;
        this.youtubeVideos = youtubeVideos;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.videoMob.loadData( youtubeVideos.get(position).getVideoUrl(), "text/html" , "utf-8" );
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        WebView videoMob;


        public VideoViewHolder(View itemView) {

            super(itemView);

            videoMob = (WebView) itemView.findViewById(R.id.videoWebView);
            videoMob.getSettings().setJavaScriptEnabled(true);
            videoMob.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            videoMob.clearCache(true);
            videoMob.clearHistory();
            videoMob.setWebChromeClient(new WebChromeClient());
        }
    }
}

