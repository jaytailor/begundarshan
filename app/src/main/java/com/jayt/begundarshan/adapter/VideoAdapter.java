package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.YoutubeVideo;
import com.squareup.picasso.Picasso;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    //these ids are the unique id for each video
    String[] VideoID = {"AtkKzZYVz4U", "NkXBll2DpuQ", "dHFzS2s-2Sg"};
    Context ctx;

    List<YoutubeVideo> youtubeVideos;
    YouTubePlayerView youTubePlayerView;


    public VideoAdapter() {
    }

    public VideoAdapter(Context c, List<YoutubeVideo> youtubeVideos) {
        this.ctx = c;
        this.youtubeVideos = youtubeVideos;
    }

    public VideoAdapter(Context c) {
        this.ctx = c;
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        //holder.videoMob.loadData( youtubeVideos.get(position).getVideoUrl(), "text/html" , "utf-8" );
        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        holder.youTubeThumbnailView.initialize("AIzaSyCIl3Eqt3STpA0f6XuxRywkHRT8GEzpo70", new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(youtubeVideos.get(position).getVideoUrl());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                youTubeThumbnailLoader.release();
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });

    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public int getItemCount() {
        //return youtubeVideos.size();
        return VideoID.length;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //WebView videoMob;
        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;


        public VideoViewHolder(View itemView) {

            super(itemView);
            playButton=(ImageView)itemView.findViewById(R.id.btnYoutube_player);
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);

            Picasso.with(ctx)
                    .load("http://img.youtube.com/vi/AtkKzZYVz4U/hqdefault.jpg").fit()
                    .into(playButton);

        }

        @Override
        public void onClick(View v) {

            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx,
                    "AIzaSyCIl3Eqt3STpA0f6XuxRywkHRT8GEzpo70",
                    youtubeVideos.get(getLayoutPosition()).getVideoUrl(),
                    100,     //after this time, video will start automatically
                    true,   //autoplay or not
                    true);

            ctx.startActivity(intent);
        }
    }
}

