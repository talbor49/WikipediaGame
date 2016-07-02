package com.example.tal.wikipediagame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    enum State {RANDOM_ARTICLE_EXPOSURE, IN_SEARCH_OF_DESTINATION}
    private Article destinationArticle;
    private WebView wikipediaWv;
    private TextView youWinTv;
    private String tempDestUrl;
    private Button playAgainButton;
    private State state;
    private int pagesCount;
    private CountDownTimer cdt;
    public static final int SECONDS_TO_VIEW_DESTINATION = 5;
    private final String WIKIPEDIA_PAGE_BY_PAGEID_URL_PREFIX = "http://en.m.wikipedia.org/?curid=";
    private Toolbar myToolbar;
    private boolean loadedRandomArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);

        /*
        1. Generate destination article
        2. Show it on the main screen for N seconds.
        3. Change to random starting article, make title the destination article.
         */

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        assert myToolbar != null;
        myToolbar.setTitle("");
        setSupportActionBar(myToolbar);

        destinationArticle  = new Article();

        wikipediaWv = (WebView) findViewById(R.id.wikipediaWebView);
        assert wikipediaWv != null;
        WebSettings webSettings = wikipediaWv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        youWinTv = (TextView) findViewById(R.id.youWinTv);
        assert youWinTv != null;

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        assert playAgainButton != null;


        //Initialize and set the play again button
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == State.IN_SEARCH_OF_DESTINATION) {
                    startNewGame();
                }
            }
        });


        wikipediaWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (state == State.RANDOM_ARTICLE_EXPOSURE && !loadedRandomArticle) {
                    loadedRandomArticle = true;
                    extractRandomArticle(url, view);
                    cdt.start();
                } else if (state == State.IN_SEARCH_OF_DESTINATION) {
                    if (view.getTitle().equals(destinationArticle.getTitle())) {
                        view.setVisibility(View.INVISIBLE);
                        String victoryText = "You win! Great job! it only took you " + pagesCount + " moves!";
                        youWinTv.setText(victoryText);

                        playAgainButton.setVisibility(View.VISIBLE);

                        youWinTv.setVisibility(View.VISIBLE);
                    } else {
                        pagesCount++;
                    }
                }
            }


        });




        cdt = new CountDownTimer(SECONDS_TO_VIEW_DESTINATION * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                myToolbar.setTitle(Math.round(millisUntilFinished / 1000.0) + " seconds left!");
            }

            @Override
            public void onFinish() {
                // Load a new starting point article
                wikipediaWv.loadUrl(WIKIPEDIA_PAGE_BY_PAGEID_URL_PREFIX + ArticleUtils.getRandomArticleId());
                myToolbar.setTitle(destinationArticle.getName());
                pagesCount = 0;
                state = State.IN_SEARCH_OF_DESTINATION;
            }
        };

        startNewGame();
    }

    private void extractRandomArticle(String url, WebView view) {
        destinationArticle.setUrl(view.getUrl());
        String title = view.getTitle();
        if (title.contains("- Wikipedia")) {
            String articleName = title.substring(0, title.indexOf("- Wikipedia"));
            destinationArticle.setName(articleName);
            destinationArticle.setTitle(title);
        } else {
            Log.i("DEBUGGING", " Extracting title from destination page error. title: " + title + " url: " + url + " tempDestUrl: " + tempDestUrl);
        }
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
                if (state == State.IN_SEARCH_OF_DESTINATION) {
                    startNewGame();
                    return true;
                } else {
                    return false;
                }


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        // Prevent it from leaving the app.
    }

    private void startNewGame() {
        state = State.RANDOM_ARTICLE_EXPOSURE;
        int destinationPageId = ArticleUtils.getRandomArticleId();
        myToolbar.setTitle("");

        // Get a new random destination article. sets automatically
        destinationArticle.setPageid(destinationPageId);
        tempDestUrl = WIKIPEDIA_PAGE_BY_PAGEID_URL_PREFIX + destinationPageId;
        wikipediaWv.loadUrl(tempDestUrl);
        loadedRandomArticle = false;

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
