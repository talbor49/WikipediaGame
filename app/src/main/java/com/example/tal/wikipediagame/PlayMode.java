package com.example.tal.wikipediagame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class PlayMode extends AppCompatActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        assert myToolbar != null;
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("");

        destinationArticle  = new Article();

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
                if (!url.equals(tempDestUrl) && !url.startsWith("https://en.wikipedia.org/w/load.php?")) {
                    destinationArticle.setUrl(view.getUrl());
                    String title = view.getTitle();
                    if (title.contains("- Wikipedia")) {
                        String articleName = title.substring(0, title.indexOf("- Wikipedia"));
                        destinationArticle.setName(articleName);
                        String descriptionText = "Destination article title: " + destinationArticle.getName();
                        destinationTextView.setText(descriptionText);
                        myToolbar.setTitle(Html.fromHtml(articleName));
                    } else {
                        Log.i("DEBUGGING", " Extracting title from destination page error. title: " + title + " url: " + url + " tempDestUrl: " + tempDestUrl);
                    }

                }
            }
        };
        utilityWebView.setWebViewClient(utilityWebViewClient);


        //Initialize and set the play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame(ArticleUtils.getRandomArticleId(), ArticleUtils.getRandomArticleId());
            }
        });
        replayTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(ArticleUtils.getRandomArticleId(), ArticleUtils.getRandomArticleId());
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


        startNewGame(ArticleUtils.getRandomArticleId(), ArticleUtils.getRandomArticleId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exitToMainMenu:
                Intent mainMenuIntent = new Intent(this, StartingScreen.class);
                startActivity(mainMenuIntent);
                return true;

            case R.id.restartMatch:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                startNewGame(ArticleUtils.getRandomArticleId(), ArticleUtils.getRandomArticleId());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private void startNewGame(int destinationPageId, int startingPageId) {
        // Get a new random destination article. sets automatically
        destinationArticle.setPageid(ArticleUtils.getRandomArticleId());
        tempDestUrl = "http://en.wikipedia.org/?curid=" + destinationPageId;
        utilityWebView.loadUrl(tempDestUrl);

        // Load a new starting point article
        wikipediaWv.loadUrl("http://en.m.wikipedia.org/?curid=" + startingPageId);

        //Hide and show relevant views
        wikipediaWv.setVisibility(View.VISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);
        youWinTv.setVisibility(View.INVISIBLE);

        pagesCount = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
