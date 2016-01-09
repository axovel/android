package com.axovel.ecommerce.sweetschoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.axovel.ecommerce.sweetschoice.view.Activity.Activity_Login;

/*  @author Umesh Chauhan
 *   Axovel Private Limited
 */
public class EcommStart extends Activity {

    final String VER_ECOMM_FRAMEWORK = "1.0.0";
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecomm_start);
        // Ecomm Framework Check
        // App Version Check

        // Test
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent i = new Intent(EcommStart.this, Activity_Login.class);
                startActivity(i);
                EcommStart.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}
