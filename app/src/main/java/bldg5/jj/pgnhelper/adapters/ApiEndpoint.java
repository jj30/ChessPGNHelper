package bldg5.jj.pgnhelper.adapters;

import java.util.List;

import bldg5.jj.pgnhelper.common.Game;
import bldg5.jj.pgnhelper.common.Games;
import bldg5.jj.pgnhelper.common.Player;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndpoint {
    @GET("/find_player/")
    Call<Games> getPlayers(
        @Query("search") String q
    );

    /* @GET("/find_games/")
    Call<List<Game>> getGames(
        @Query("white_id") String white_id,
        @Query("black_id") String black_id
    );*/
}
