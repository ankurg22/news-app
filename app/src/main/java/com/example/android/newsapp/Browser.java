package com.example.android.newsapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.newsapp.databinding.WebViewBinding;

/**
 * This activity uses webview to load webpage
 */
public class Browser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebViewBinding binding = DataBindingUtil.setContentView(this, R.layout.web_view);

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        binding.webView.loadUrl(url);
    }
}
