package com.example.android.newsapp;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.android.newsapp.databinding.FragmentSectionNewsBinding;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * Created by Ankur Gupta on 7/8/17.
 * guptaankur.gupta0@gmail.com
 */

public class SectionNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    //Guardian API related strings
    private static final String BASE_URL = "https://content.guardianapis.com/search";
    private static final String QUERY_SECTION = "section";
    private static final String QUERY_API_KEY = "api-key";
    private static final String SECTION_NAME = "sectionName";

    private String selectedSection;
    private FragmentSectionNewsBinding binding;
    private NewsAdapter adapter;

    public SectionNewsFragment() {
    }

    public static SectionNewsFragment newInstance(String section) {
        SectionNewsFragment fragment = new SectionNewsFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_NAME, section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedSection = getArguments().getString(SECTION_NAME);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_section_news, container, false);

        //MAke and set adapter on ListView
        adapter = new NewsAdapter(getActivity(), new ArrayList<News>());
        binding.listNews.setAdapter(adapter);

        //Set Empty View on ListView
        binding.listNews.setEmptyView(binding.empty);

        //Load only if internet is working else show proper message
        if (isNetworkWorking()) {
            android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else {
            binding.progress.setVisibility(View.GONE);
            binding.empty.setText(getString(R.string.no_internet));
        }

        binding.listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the url from adapter
                String webpage = ((News) adapterView.getItemAtPosition(i)).getUrl();
                //Pass url and start browser activity.
                Intent intent = new Intent(getActivity(), Browser.class);
                intent.putExtra("URL", webpage);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    @Override
    public android.support.v4.content.Loader<List<News>> onCreateLoader(int id, Bundle args) {
        String query = buildQueryUrl();
        return new NewsListLoader(getActivity(), query);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<News>> loader, List<News> data) {

        //Data has loaded so remove progress bar
        binding.progress.setVisibility(View.GONE);
        //Set empty text view
        binding.empty.setText(R.string.empty_text);
        //Clear adapter before setting
        adapter.clear();

        //Add all data in adapter
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<News>> loader) {
        adapter.clear();
    }

    /**
     * Method to create the query url based on the selected album
     *
     * @return String url
     */
    private String buildQueryUrl() {
        if (selectedSection.equals("home")) {
            return Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(QUERY_API_KEY, "bde31df0-7941-4d23-93f5-5fa63008b05b")
                    .build().toString();
        } else {
            return Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(QUERY_SECTION, selectedSection)
                    .appendQueryParameter(QUERY_API_KEY, "bde31df0-7941-4d23-93f5-5fa63008b05b")
                    .build().toString();
        }
    }

    /**
     * Method to check whether the internet connection is working or not
     *
     * @return boolean value
     */
    private boolean isNetworkWorking() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
