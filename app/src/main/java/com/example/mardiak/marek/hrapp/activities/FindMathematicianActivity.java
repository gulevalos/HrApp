package com.example.mardiak.marek.hrapp.activities;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mardiak.marek.hrapp.R;
import com.example.mardiak.marek.hrapp.mathematicians.Mathematician;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindMathematicianActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ArrayAdapter<String> searchAdapter;
    private List<String> allMathematicians = Arrays.asList(Mathematician.mathematiciansNames);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_mathematicians);
        initAutocompleteSearch();

/*        WebView mWebView;
        mWebView = (WebView) findViewById(R.id.mathematician_webview2);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        String html = "file:///android_asset/Mathematicians/Adams_Edwin.html";
        mWebView.loadUrl(html);*/
    }


    private void initAutocompleteSearch() {
        searchAdapter = new ArrayAdapter<String>(this, R.layout.search_item) {
            private Filter filter;

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_item, parent, false);
                }

                TextView mathematicianName = (TextView) convertView
                        .findViewById(R.id.search_item_mathematician_name);
                final String mathematicianItem = this.getItem(position);
                convertView.setTag(mathematicianItem);
                mathematicianName.setText(mathematicianItem);
                return convertView;
            }

            @Override
            public Filter getFilter() {
                if (filter == null) {
                    filter = new MathematicianFilter();
                }
                return filter;
            }
        };

        toolbar = (Toolbar) findViewById(R.id.toolbarM);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_HOME_AS_UP);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.actionbar_search, null);
        AutoCompleteTextView textView = (AutoCompleteTextView) v.findViewById(R.id.search_box);

        textView.setAdapter(searchAdapter);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "this is my Toast message!!! =)",
                        Toast.LENGTH_LONG).show();
            }
        });
        actionBar.setCustomView(v);
    }


    private class MathematicianFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> list = new ArrayList<String>(allMathematicians);
            FilterResults result = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                result.values = list;
                result.count = list.size();
            } else {
                String substr = constraint.toString().toLowerCase();
                final ArrayList<String> retList = new ArrayList<String>();
                for (String mathematician : list) {
                    if (mathematician.toLowerCase().contains(substr)) {
                        retList.add(mathematician);
                    }
                }
                result.values = retList;
                result.count = retList.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked") //TODO which thread runs this?
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchAdapter.clear();
            if (results.count > 0) {
                for (String o : (ArrayList<String>) results.values) {
                    searchAdapter.add(o);
                }
            }
        }

    }


}
