package com.example.tal.wikipediagame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class PlayMode extends Activity {

    private Article destinationArticle;
    private WebView wikipediaWv;
    private TextView destinationTextView;
    private TextView youWinTv;
    private String tempDestUrl;
    private Button playAgainButton;
    private ImageButton replayTopButton;
    private WebView utilityWebView;
    private int pagesCount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        destinationArticle  = new Article();
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

        replayTopButton = (ImageButton) findViewById(R.id.replayButton);
        assert replayTopButton != null;



        utilityWebView = new WebView(getApplicationContext());
        WebViewClient utilityWebViewClient = new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (!url.equals(tempDestUrl)) {
                    destinationArticle.setUrl(view.getUrl());
                    destinationArticle.generateName();
                    String descriptionText = "Destination article title: " + destinationArticle.getName();
                    destinationTextView.setText(descriptionText);
                }
            }
        };
        utilityWebView.setWebViewClient(utilityWebViewClient);


        //Initialize and set the play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
        replayTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });

        pagesCount = 0;

        wikipediaWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (url.equals(destinationArticle.getUrl())) {
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
        

        startNewGame();
    }


    private void startNewGame() {
        // Get a new random destination article. sets automatically
        destinationArticle.setPageid(ArticleUtils.getRandomArticleId());
        tempDestUrl = "http://en.wikipedia.org/?curid=" + destinationArticle.getPageid();
        utilityWebView.loadUrl(tempDestUrl);

        // Load a new starting point article
        wikipediaWv.loadUrl("http://en.m.wikipedia.org/?curid=" + ArticleUtils.getRandomArticleId());

        //Hide and show relevant views
        wikipediaWv.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);
        youWinTv.setVisibility(View.INVISIBLE);

        pagesCount = 0;
    }
}
