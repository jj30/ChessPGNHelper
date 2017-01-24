package bldg5.jj.pgnbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends Activity {

    @BindView(R2.id.btnOK) Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // Button btnOK = (Button) findViewById(R.id.btnOK);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navToSearch = new Intent(HomeActivity.this.getApplicationContext(), StartSearch.class);
                HomeActivity.this.startActivity(navToSearch);
            }
        });
    }
}
