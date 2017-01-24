package bldg5.jj.pgnbase.adapters;

import bldg5.jj.pgnbase.common.Games;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndpoint {
    @GET("/find_player/")
    Call<Games> getPlayers(
        @Query("w") String white,
        @Query("b") String black
    );
}
