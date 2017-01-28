package bldg5.jj.pgnbase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import bldg5.jj.pgnbase.adapters.ApiEndpoint;
import bldg5.jj.pgnbase.adapters.ListViewAdapter;
import bldg5.jj.pgnbase.common.Error;
import bldg5.jj.pgnbase.common.Game;
import bldg5.jj.pgnbase.common.Games;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StartSearch extends AppCompatActivity {
    // boolean bIsEnabled = true;
    @BindView(R2.id.editTextWhite) AutoCompleteTextView white;
    @BindView(R2.id.editTextBlack) AutoCompleteTextView black;
    @BindView(R2.id.btnFindGames) Button btnSearch;
    @BindView(R2.id.chkIgnoreColor) CheckBox chkIgnoreColor;
    InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_search);
        ButterKnife.bind(this);

        // bind text watcher to the white EditText
        white.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 3) {
                    popAutoComplete("w", str);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        // bind text watcher to the black EditText
        black.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 3) {
                    popAutoComplete("b", white.getText().toString(), str);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

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

    private void popAutoComplete(final String wb, final String w_player) {
        popAutoComplete(wb, w_player, "");
    }

    private void popAutoComplete(final String wb, final String w_player, final String b_player) {
        final String BASE_URL = "http://ec2-54-158-98-180.compute-1.amazonaws.com:8080";
        final String URL = wb == "w" ? BASE_URL + "/auto_white/" : "/auto_black/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoint service = retrofit.create(ApiEndpoint.class);
        Call<String[]> call = wb == "w" ? service.autoWhite(w_player) : service.autoBlack(w_player, b_player);

        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                boolean bSuccess = response.isSuccessful();
                String[] for_autocomplete = new String[]{};

                if (bSuccess) {
                    for_autocomplete = (String[]) response.body();
                }

                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(wb == "w" ? R.id.editTextWhite : R.id.editTextBlack);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(StartSearch.this, android.R.layout.simple_dropdown_item_1line, for_autocomplete);
                textView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                Error.sendError(t.getMessage());
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
        int nIC = chkIgnoreColor.isChecked() ? 1: 0;

        Bundle params = new Bundle();
        params.putString("White", strWhite);
        params.putString("Black", strBlack);
        params.putInt("IgnoreColor", nIC);

        Intent navMain = new Intent(StartSearch.this.getApplicationContext(), ListPlayers.class);
        navMain.putExtras(params);

        StartSearch.this.startActivity(navMain);
    }
}
