package bldg5.jj.pgnbase.adapters;

import org.json.JSONArray;

import bldg5.jj.pgnbase.common.Games;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndpoint {
    @GET("/find_player_wic/")
    Call<Games> getPlayers(
        @Query("w") String white,
        @Query("b") String black,
        @Query("ic") int ic
    );

    @GET("/log_err/")
    Call<Void> logError(
        @Query("err") String err
    );

    @GET("/auto_white/")
    Call<String[]> autoWhite(
            @Query("w") String w
    );

    @GET("/auto_black/")
    Call<String[]> autoBlack(
        @Query("w") String w,
        @Query("b") String b
    );
}
