package com.example.tal.wikipediagame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;

public class PlayMode extends AppCompatActivity {

    static WebView wv;
    static TextView destinationTextView;
    static Button butt;
    static TextView youWinTv;
    static String destinationUrl;
    static Button playAgainButton;
    static int pagesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);
        wv = (WebView) findViewById(R.id.wikipediaWebView);
        assert wv != null;

        final String randomArticle = "https://en.wikipedia.org/wiki/Special:Random";

        destinationTextView = (TextView) findViewById(R.id.destinationTextView);
        assert destinationTextView != null;

        youWinTv = (TextView) findViewById(R.id.youWinTv);




        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);


        final WebView utilityWebView = new WebView(getApplicationContext());
        WebViewClient utilityWebViewClient = new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (!url.equals(randomArticle)) {
                    destinationUrl = view.getUrl();
                    destinationTextView.setText("Destination: " + destinationUrl);
                }
            }
        };
        utilityWebView.setWebViewClient(utilityWebViewClient);
        utilityWebView.loadUrl(randomArticle);


        //Initialize and set the play again button
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a new random destination article
                utilityWebView.loadUrl(randomArticle);

                // Load a new starting point article
                wv.loadUrl(randomArticle);
                wv.setVisibility(View.VISIBLE);
                playAgainButton.setVisibility(View.INVISIBLE);
                youWinTv.setVisibility(View.INVISIBLE);

                pagesCount = 0;
            }
        });

        pagesCount = 0;

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (url.equals(destinationUrl)) {
                    view.setVisibility(View.INVISIBLE);
                    youWinTv.setText("You win! Great job! it only took you " + pagesCount + " moves :)");

                    playAgainButton.setVisibility(View.VISIBLE);

                    youWinTv.setVisibility(View.VISIBLE);
                } else {
                    pagesCount++;
                }
            }
        });
        wv.loadUrl("https://en.wikipedia.org/wiki/Special:Random");

    }
}
