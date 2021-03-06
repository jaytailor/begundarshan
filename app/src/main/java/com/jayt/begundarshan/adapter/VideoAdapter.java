package com.jayt.begundarshan.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.jayt.begundarshan.R;
import com.jayt.begundarshan.model.YoutubeVideo;
import com.squareup.picasso.Picasso;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    private Context ctx;
    private TextView videoTitle;

    private List<YoutubeVideo> youtubeVideos;
    private ImageView playButton;


    public VideoAdapter(Context c, List<YoutubeVideo> youtubeVideos) {
        this.ctx = c;
        this.youtubeVideos = youtubeVideos;
    }

    private String youtubekey = "AIzaSyCIl3Eqt3STpA0f6XuxRywkHRT8GEzpo70";

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {

        videoTitle.setText(youtubeVideos.get(position).getTitle());

        String fullVideoThumbnail = "https://img.youtube.com/vi/" +youtubeVideos.get(position).getUrl()+ "/0.jpg";
        Picasso.with(ctx)
                .load(fullVideoThumbnail).fit()
                .into(playButton);

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

        holder.youTubeThumbnailView.initialize(youtubekey, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(youtubeVideos.get(position).getUrl());
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
        return this.youtubeVideos.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //WebView videoMob;
        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;

         VideoViewHolder(View itemView) {

            super(itemView);
            playButton = (ImageView)itemView.findViewById(R.id.btnYoutube_player);
            videoTitle = (TextView) itemView.findViewById(R.id.videoTitle);

            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);

        }

        @Override
        public void onClick(View v) {

            if(getLayoutPosition() != -1){
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx,
                        youtubekey,
                        youtubeVideos.get(getLayoutPosition()).getUrl(),
                        100,     //after this time, video will start automatically
                        true,   //autoplay or not
                        true);

                ctx.startActivity(intent);
            }

        }
    }
}

