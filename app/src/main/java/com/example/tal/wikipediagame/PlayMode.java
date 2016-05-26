package com.example.tal.wikipediagame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class PlayMode extends AppCompatActivity {

    static WebView wikipediaWv;
    static TextView destinationTextView;
    static Button butt;
    static TextView youWinTv;
    static String destinationUrl;
    static String destionationName;
    static Button playAgainButton;
    static int pagesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);
        wikipediaWv = (WebView) findViewById(R.id.wikipediaWebView);
        assert wikipediaWv != null;
        WebSettings webSettings = wikipediaWv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        destinationTextView = (TextView) findViewById(R.id.destinationTextView);
        assert destinationTextView != null;

        youWinTv = (TextView) findViewById(R.id.youWinTv);
        assert youWinTv != null;

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        assert playAgainButton != null;

        final String randomArticle = "https://en.wikipedia.org/wiki/Special:Random";








        final WebView utilityWebView = new WebView(getApplicationContext());
        WebViewClient utilityWebViewClient = new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (!url.equals(randomArticle)) {
                    destinationUrl = view.getUrl();
                    destionationName = destinationUrl.substring(destinationUrl.indexOf("wiki/") + "wiki/".length() + 1);
                    destinationTextView.setText("Destination: " + destionationName);
                }
            }
        };
        utilityWebView.setWebViewClient(utilityWebViewClient);
        utilityWebView.loadUrl(randomArticle);


        //Initialize and set the play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a new random destination article
                utilityWebView.loadUrl(randomArticle);

                // Load a new starting point article
                wikipediaWv.loadUrl(randomArticle);
                wikipediaWv.setVisibility(View.VISIBLE);
                playAgainButton.setVisibility(View.INVISIBLE);
                youWinTv.setVisibility(View.INVISIBLE);

                pagesCount = 0;
            }
        });

        pagesCount = 0;

        wikipediaWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (url.equals(destinationUrl)) {
                    view.setVisibility(View.INVISIBLE);
                    String victoryText = "You win! Great job! it only took you " + pagesCount + " moves!";
                    youWinTv.setText(victoryText);

                    playAgainButton.setVisibility(View.VISIBLE);

                    youWinTv.setVisibility(View.VISIBLE);
                } else {
                    pagesCount++;
                }
            }
        });
        
        wikipediaWv.loadUrl("https://en.wikipedia.org/wiki/Special:Random");

    }
}
