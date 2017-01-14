package bldg5.jj.pgnhelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.w3c.dom.Text;

import bldg5.jj.pgnhelper.common.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity {
    private int nMoveNumber;
    private CB boardShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(R.layout.content_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        Button btnFirst = (Button) findViewById(R.id.btnFirst);
        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        Button btnNext = (Button) findViewById(R.id.btnNext);
        Button btnLast = (Button) findViewById(R.id.btnLast);
        Button btnSwitch = (Button) findViewById(R.id.btnSwitch);
        boardShowing = (CB) findViewById(R.id.boardShowing);
        final TextView txtMove = (TextView) findViewById(R.id.txtCurrentMove);
        // basic info on zeroth move, who's black, who's white, etc.
        String strGameInfo = boardShowing.getInfo();

        // if it's long, make the font smaller
        // if it's really long trim it.
        // under 170, 20 size
        // between 170 and 300, 15 size
        // higher than 300, 12 size
        if (strGameInfo.length() > 170) {
            if (strGameInfo.length() > 300) {
                strGameInfo = strGameInfo.substring(0, 500);
                txtMove.setTextSize(12.0f);
            } else {
                // strGameInfo = strGameInfo.substring(0, 170);
                txtMove.setTextSize(15.0f);
            }
        } else {
            txtMove.setTextSize(20.0f);
        }

        // set the text
        txtMove.setText(strGameInfo);
        // set up the navigation drawer.
        setupDrawer();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next(txtMove);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev(txtMove);
            }
        });

        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first();
            }
        });

        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last();
            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardShowing.switchSides();
            }
        });

        final Context context = this.getApplicationContext();

        // set the swipe listener
        boardShowing.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() { last(); }

            public void onSwipeRight() { prev(txtMove); }

            public void onSwipeLeft() { next(txtMove); }

            public void onSwipeBottom() { first(); }
        });
    }

    private void next(TextView txtMove) {
        nMoveNumber = boardShowing.getMoveNumber() + 1;
        boardShowing.setMoveNumber(nMoveNumber);

        // the pgn is set on instancing of CB so set the text view
        // but it has to be after the move number is set.
        txtMove.setText(boardShowing.getMove());
        boardShowing.halfMove();
    }

    private void prev(TextView txtMove) {
        nMoveNumber = boardShowing.getMoveNumber() - 1;
        boardShowing.setMoveNumber(nMoveNumber);

        // the pgn is set on instancing of CB so set the text view
        // but it has to be after the move number is set.
        txtMove.setText(boardShowing.getMove());
        boardShowing.halfMoveBackwards();
    }

    private void first() {
        nMoveNumber = 0;
        boardShowing.setMoveNumber(nMoveNumber);
        boardShowing.initBoard();
    }

    private void last() {
        // if the game ends on a white move, this will be higher than
        // the max # of UI moves by one.
        nMoveNumber = 2 * boardShowing.getNumMoves();
        boardShowing.toTheEnd();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        final TextView txtMove = (TextView) findViewById(R.id.txtCurrentMove);

        outState.putInt("nMoveNumber", nMoveNumber);
        outState.putString("txtMove", txtMove.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        final TextView txtMove = (TextView) findViewById(R.id.txtCurrentMove);
        nMoveNumber = savedState.getInt("nMoveNumber");

        String move = savedState.getString("txtMove");
        txtMove.setText(move);

        if (nMoveNumber > 0) {
            boardShowing.setMoveNumber(nMoveNumber);
            boardShowing.toMoveNumber(nMoveNumber);
        }
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

    private void setupDrawer() {
        // https://github.com/mikepenz/MaterialDrawer
        // navigation drawer
        new DrawerBuilder().withActivity(this).build();
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem searchItem = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_search);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                // .withToolbar(toolbar)
                .addDrawerItems(
                        homeItem,
                        searchItem,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 0) {
                            // home
                        }

                        if (position == 1) {
                            // search
                        }
                        return true;
                    }
                })
                .build();

    }
}
