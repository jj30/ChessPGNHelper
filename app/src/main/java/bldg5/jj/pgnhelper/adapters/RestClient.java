package bldg5.jj.pgnhelper.adapters;


import android.util.Log;

import java.util.List;

import bldg5.jj.pgnhelper.adapters.ApiEndpoint;
import bldg5.jj.pgnhelper.common.Player;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static String BASE_URL = "http://ec2-54-173-91-238.compute-1.amazonaws.com:8080/find_player/";
    public List<Player> allPlayers;

    public void pullDown(String search)
    {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        ApiEndpoint service = retrofit.create(ApiEndpoint.class);
        Call<List<Player>> call = service.getPlayers(search);

        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                boolean bSuccess = response.isSuccessful();

                if (bSuccess) {
                    allPlayers = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                Log.e("PGNHelper", t.toString());
            }
        });
    }
}