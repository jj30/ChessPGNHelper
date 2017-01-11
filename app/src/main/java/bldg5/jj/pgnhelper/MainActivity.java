package bldg5.jj.pgnhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int nMoveNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnFirst = (Button) findViewById(R.id.btnFirst);
        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        Button btnNext = (Button) findViewById(R.id.btnNext);
        Button btnLast = (Button) findViewById(R.id.btnLast);
        Button btnSwitch = (Button) findViewById(R.id.btnSwitch);
        final TextView txtMove = (TextView) findViewById(R.id.txtCurrentMove);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CB boardShowing = (CB) findViewById(R.id.boardShowing);
                nMoveNumber = boardShowing.getMoveNumber() + 1;

                boardShowing.setMoveNumber(nMoveNumber);

                // the pgn is set on instancing of CB so set the text view
                // but this line has to come after the move number is set.
                txtMove.setText(boardShowing.getMove());

                boardShowing.halfMove();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CB boardShowing = (CB) findViewById(R.id.boardShowing);
                nMoveNumber = boardShowing.getMoveNumber() - 1;

                boardShowing.setMoveNumber(nMoveNumber);

                // the pgn is set on instancing of CB so set the text view
                // but this line has to come after the move number is set.
                txtMove.setText(boardShowing.getMove());

                boardShowing.halfMoveBackwards();
            }
        });

        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CB boardShowing = (CB) findViewById(R.id.boardShowing);
                nMoveNumber = 0;

                boardShowing.setMoveNumber(nMoveNumber);
                boardShowing.initBoard();
            }
        });

        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CB boardShowing = (CB) findViewById(R.id.boardShowing);
                boardShowing.toTheEnd();
            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CB boardShowing = (CB) findViewById(R.id.boardShowing);
                boardShowing.switchSides();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
