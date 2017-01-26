package bldg5.jj.pgnbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import bldg5.jj.pgnbase.adapters.ListViewAdapter;
import bldg5.jj.pgnbase.common.Game;
import bldg5.jj.pgnbase.common.Games;

public class ListGames extends AppCompatActivity {
    private static final String identifier =  "ID: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_games);

        Intent intent = getIntent();
        final Games allGames = (Games) intent.getSerializableExtra("DisplayedGames");
        String selected_player = intent.getStringExtra("SelectedPlayer");

        HashMap<String, String> hashMap = new HashMap<String, String>() {};

        int i = 0;
        for (Game g : allGames.listGames) {
            CB getDesc = new CB(this.getApplicationContext(), g);

            if (g.getWhite().equals(selected_player) || g.getBlack().equals(selected_player)) {
                String game_desc = getDesc.getInfo();
                hashMap.put(game_desc, identifier + String.valueOf(i));
            }

            i++;
        }

        final ListView listView = (ListView) findViewById(R.id.gamesList);
        ListViewAdapter playersListAdapter = new ListViewAdapter(hashMap);
        listView.setAdapter(playersListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Bundle params = new Bundle();

                TextView txtName = (TextView) view.findViewById(R.id.txtName);
                TextView txtId = (TextView) view.findViewById(R.id.txtId);

                String gameInfo = txtName.getText().toString();
                Integer nGameIdx = Integer.valueOf(txtId.getText().toString().replace(identifier, ""));
                Game gameSelected = allGames.listGames.get(nGameIdx);

                params.putString("GameInfo", gameInfo);
                params.putSerializable("SelectedGame", gameSelected);

                Intent navShowGame = new Intent(ListGames.this, MainActivity.class);
                navShowGame.putExtras(params);
                ListGames.this.startActivity(navShowGame);
            }
        });
    }
}
