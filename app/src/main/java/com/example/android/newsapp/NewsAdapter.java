package com.example.android.newsapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.newsapp.databinding.ListItemNewsBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ankur Gupta on 8/8/17.
 * guptaankur.gupta0@gmail.com
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private ArrayList<News> newsList;
    private int flag = 0;
    private Context mContext;

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        super(context, 0, newsList);
        this.newsList = newsList;
        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (convertView == null) {
            ListItemNewsBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_news, parent, false);
            convertView = binding.getRoot();
            viewHolder = new ViewHolder(binding);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();
        News news = newsList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'", Locale.getDefault());;
        try {
             news.setPublicationDate(format.parse(news.getPublicationDate()).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.bind(news);
        return convertView;
    }

    private class ViewHolder {
        private ListItemNewsBinding itemBinding;

        public ViewHolder(ListItemNewsBinding binding) {
            this.itemBinding = binding;
        }

        public void bind(News news) {
            itemBinding.setNews(news);
            if (flag==5) flag =0;
            itemBinding.back.setBackgroundColor(mContext.getResources().getIntArray(R.array.colors)[flag]);
            itemBinding.frameText.setBackgroundColor(mContext.getResources().getIntArray(R.array.colors)[flag]);
            flag++;
            itemBinding.executePendingBindings();
        }
    }
}
