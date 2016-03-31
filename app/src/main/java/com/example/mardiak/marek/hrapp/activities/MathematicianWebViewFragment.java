package com.example.mardiak.marek.hrapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.mardiak.marek.hrapp.R;

/**
 * Created by mm on 3/31/2016.
 */
public class MathematicianWebViewFragment extends Fragment {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    public static MathematicianWebViewFragment newInstance(String mathematicianName) {
        MathematicianWebViewFragment fragment = new MathematicianWebViewFragment();
        Bundle args = new Bundle();
        args.putString("mathematicianName", mathematicianName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_foo, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        String mathematicianName = getArguments().getString("mathematicianName", "");
        WebView mWebView;
        mWebView = (WebView) view.findViewById(R.id.mathematician_webview2);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        String html = "file:///android_asset/Mathematicians/" + mathematicianName + ".html";
        mWebView.loadUrl(html);
    }
}
