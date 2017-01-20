package bldg5.jj.pgnhelper.adapters;

import bldg5.jj.pgnhelper.common.Games;
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
