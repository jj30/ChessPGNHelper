package bldg5.jj.pgnhelper;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bldg5.jj.pgnhelper.adapters.ApiEndpoint;
import bldg5.jj.pgnhelper.adapters.ListViewAdapter;
import bldg5.jj.pgnhelper.common.Game;
import bldg5.jj.pgnhelper.common.Games;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListPlayers extends AppCompatActivity {
    private static String BASE_URL = "http://ec2-54-158-98-180.compute-1.amazonaws.com:8080/find_player/";
    private Games results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ListView listView = (ListView) findViewById(R.id.playersList);

        getPlayers(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Bundle params = new Bundle();


                String player_name = ((TextView) view.findViewById(R.id.txtName)).getText().toString();
                params.putString("SelectedPlayer", player_name);
                params.putSerializable("DisplayedGames", results);

                Intent navListGames = new Intent(ListPlayers.this, ListGames.class);
                navListGames.putExtras(params);
                ListPlayers.this.startActivity(navListGames);
            }
        });
    }

    public void getPlayers(final ListView lv) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoint service = retrofit.create(ApiEndpoint.class);
        Call<Games> call = service.getPlayers("vish");

        call.enqueue(new Callback<Games>() {

            @Override
            public void onResponse(Call<Games> call, Response<Games> response) {
                boolean bSuccess = response.isSuccessful();

                HashMap<String, String> hashMap = new HashMap<String, String>() {};
                if (bSuccess) {
                    results = response.body();

                    for(final Game g: results.listGames) {
                        hashMap.put(g.getWhite(), String.valueOf(g.getWhite()));
                    }
                }

                ListViewAdapter playersListAdapter = new ListViewAdapter(hashMap);
                lv.setAdapter(playersListAdapter);
            }

            @Override
            public void onFailure(Call<Games> call, Throwable t) {
                Log.e("PGNHelper", t.toString());
            }
        });
    }

}
