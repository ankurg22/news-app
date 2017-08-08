package com.example.android.newsapp;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.newsapp.databinding.ActivityMainBinding;

/**
 * Main Activity with spinner and fragment
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Setup toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        binding.spinner.setAdapter(new MyAdapter(
                binding.toolbar.getContext(),
                new String[]{
                        getString(R.string.home),
                        getString(R.string.business),
                        getString(R.string.education),
                        getString(R.string.environment),
                        getString(R.string.fashion),
                        getString(R.string.film),
                        getString(R.string.money),
                        getString(R.string.politics),
                        getString(R.string.sport),
                        getString(R.string.technology),
                        getString(R.string.weather),
                        getString(R.string.world),
                }));
        //Attach listener on spinner
        binding.spinner.setOnItemSelectedListener(itemSelectedListener());

    }

    /**
     * Adapter class for Spinner
     */
    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    //Listener provider for spinner
    private OnItemSelectedListener itemSelectedListener() {
        return new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                String sectionName = (String) parent.getItemAtPosition(position);
                sectionName = sectionName.toLowerCase();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, SectionNewsFragment.newInstance(sectionName))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }
}
