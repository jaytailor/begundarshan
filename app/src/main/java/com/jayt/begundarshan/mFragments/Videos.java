package com.jayt.begundarshan.mFragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.adapter.VideoAdapter;
import com.jayt.begundarshan.model.YoutubeVideo;

public class Videos extends Fragment{
    View view;


    // Recycler View Field
    RecyclerView recyclerView;

    // Vector for video URL
    Vector<YoutubeVideo> youtubeVideos = new Vector<YoutubeVideo>();

    public Videos() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.videos, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load video List but clear from earlier call
        youtubeVideos.clear();

        youtubeVideos.add(new YoutubeVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/xplGGQq9zwE\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>"));

        VideoAdapter videoAdapter = new VideoAdapter(getActivity(), youtubeVideos);
        recyclerView.setAdapter(videoAdapter);

        return view;
    }
}

