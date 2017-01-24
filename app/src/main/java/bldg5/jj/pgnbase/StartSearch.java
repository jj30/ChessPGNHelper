package bldg5.jj.pgnbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartSearch extends AppCompatActivity {
    boolean bIsEnabled = true;
    @BindView(R2.id.editTextWhite) EditText white;
    @BindView(R2.id.editTextBlack) EditText black;
    @BindView(R2.id.btnFindGames) Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_search);
        ButterKnife.bind(this);

        // Button btnSearch = (Button) findViewById(R.id.btnFindGames);

        // final EditText white = (EditText) findViewById(R.id.editTextWhite);
        // final EditText black = (EditText) findViewById(R.id.editTextBlack);

        // CheckBox chkIgnoreGames = (CheckBox) findViewById(R.id.chkIgnoreColor);
        // final TextView txtWhite = (TextView) findViewById(R.id.txtWhite);
        /* chkIgnoreGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                bIsEnabled = !bIsEnabled;
                txtWhite.setText(bIsEnabled ? "White" : "Player");
                black.setEnabled(bIsEnabled);
            }
        });*/

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (white.getText().toString().equals("White")){
                    white.setText("");
                }
            }
        });

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (black.getText().toString().equals("Black")){
                    black.setText("");
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strWhite = white.getText().toString();
                String strBlack = black.getText().toString();

                Bundle params = new Bundle();
                params.putString("White", strWhite);
                params.putString("Black", strBlack);

                Intent navMain = new Intent(StartSearch.this.getApplicationContext(), ListPlayers.class);
                navMain.putExtras(params);
                StartSearch.this.startActivity(navMain);
            }
        });
    }
}
