package bldg5.jj.pgnhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ListGames extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);

        Intent intent = getIntent();
        Integer nPlayerId = intent.getIntExtra("PlayerId", 0);

        Log.i("PGNHelper", String.valueOf(nPlayerId));
    }
}
