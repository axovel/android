package com.axovel.schoolbus.veiw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.axovel.schoolbus.R;

public class Login extends AppCompatActivity {

    private int backButtonCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Parent.class);
                startActivity(i);
                Login.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Handling Back Press to Close App
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(
                    this,
                    R.string.app_close_confirmation,
                    Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
