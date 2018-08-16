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
    public static final String API_KEY = "AIzaSyCIl3Eqt3STpA0f6XuxRywkHRT8GEzpo70";


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

        youtubeVideos.add(new YoutubeVideo("AtkKzZYVz4U"));
        youtubeVideos.add(new YoutubeVideo("xplGGQq9zwE"));
        youtubeVideos.add(new YoutubeVideo("dHFzS2s-2Sg"));

        VideoAdapter videoAdapter = new VideoAdapter(getActivity(), youtubeVideos);
        recyclerView.setAdapter(videoAdapter);

        return view;
    }
}

