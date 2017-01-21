package bldg5.jj.pgnhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import bldg5.jj.pgnhelper.adapters.ApiEndpoint;
import bldg5.jj.pgnhelper.common.Utils;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnOK = (Button) findViewById(R.id.btnOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navToSearch = new Intent(HomeActivity.this.getApplicationContext(), StartSearch.class);
                HomeActivity.this.startActivity(navToSearch);
            }
        });
    }
}
