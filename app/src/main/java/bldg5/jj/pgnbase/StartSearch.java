package bldg5.jj.pgnbase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StartSearch extends AppCompatActivity {
    // boolean bIsEnabled = true;
    @BindView(R2.id.editTextWhite) EditText white;
    @BindView(R2.id.editTextBlack) EditText black;
    @BindView(R2.id.btnFindGames) Button btnSearch;
    InterstitialAd mInterstitialAd;
    private Handler mHandler;       // Handler to display the ad on the UI thread
    private Runnable displayAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_search);
        ButterKnife.bind(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1882113672777118/7975817786");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                navToResults();
            }
        });

        requestNewInterstitial();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    navToResults();
                }
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
            // .addTestDevice("C31D6F0EEC94CC04BB45B192065B0ED8")
            .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void navToResults() {
        String strWhite = white.getText().toString().trim();
        String strBlack = black.getText().toString().trim();

        Bundle params = new Bundle();
        params.putString("White", strWhite);
        params.putString("Black", strBlack);

        Intent navMain = new Intent(StartSearch.this.getApplicationContext(), ListPlayers.class);
        navMain.putExtras(params);

        StartSearch.this.startActivity(navMain);
    }
}
