package com.jayt.begundarshan.mFragments;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jayt.begundarshan.R;
import com.jayt.begundarshan.adapter.EditorialAdapter;
import com.jayt.begundarshan.adapter.VideoAdapter;
import com.jayt.begundarshan.common.Endpoints;
import com.jayt.begundarshan.common.Function;
import com.jayt.begundarshan.model.EditorialModel;
import com.jayt.begundarshan.model.YoutubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class Editorial extends Fragment{

    View view;

    // Recycler View Field
    RecyclerView recyclerView;

    // Vector for articles
    Vector<EditorialModel> articleList = new Vector<EditorialModel>();

    public Editorial() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.editorial, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.editorialView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetArticleList().execute();

        return view;
    }

    class GetArticleList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String articles = "";

            String urlParameters = "";
            try{
                articles = Function.excuteGet(Endpoints.SERVER_URL+"getalleditorial", urlParameters);

                if(articles == null){
                    Toast.makeText(getActivity(),"No Articles returned from server...Try after sometime...",
                            Toast.LENGTH_SHORT).show();
                }

                if(articles.length()>10){ // Just checking if not empty

                    try {
                        //Load editorial List but clear from earlier call
                        articleList.clear();

                        JSONObject jsonResponse = new JSONObject(articles);
                        JSONArray jsonArray = jsonResponse.optJSONArray("article_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            EditorialModel article = new EditorialModel();

                            article.setEditorial_title(jsonObject.getString("title"));
                            article.setEditorial_content(jsonObject.getString("content"));
                            article.setEditorial_writer(jsonObject.getString("writer"));
                            article.setEditorial_image(jsonObject.getString("image"));
                            article.setEditorial_published_at(jsonObject.getString("published_at"));

                            articleList.add(i, article);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }catch (RuntimeException e){
                e.printStackTrace();
            }

            return articles;
        }

        @Override
        protected void onPostExecute(String xml) {

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    EditorialAdapter editorialAdapterAdapter = new EditorialAdapter(getActivity(), articleList);
                    recyclerView.setAdapter(editorialAdapterAdapter);
                }
            });
        }
    }
}
