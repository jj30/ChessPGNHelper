package bldg5.jj.pgnhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import bldg5.jj.pgnhelper.adapters.ListViewAdapter;
import bldg5.jj.pgnhelper.common.Game;
import bldg5.jj.pgnhelper.common.Games;

public class ListGames extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_games);

        Intent intent = getIntent();
        final Games allGames = (Games) intent.getSerializableExtra("DisplayedGames");
        // String selected_player = intent.getStringExtra("SelectedPlayer");

        HashMap<String, String> hashMap = new HashMap<String, String>() {};

        int i = 0;
        for (Game g : allGames.listGames) {
            CB getDesc = new CB(this.getApplicationContext(), g);

            String game_desc = getDesc.getInfo();
            hashMap.put(game_desc, String.valueOf(i++));
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
                Integer nGameIdx = Integer.valueOf(txtId.getText().toString());
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
