package com.ck.almos.currencyconverter;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class HelpWebView extends AppCompatActivity {
    private WebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/Help.html");
    }
}
