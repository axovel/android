package com.axovel.ecommerce.sweetschoice.view.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.ecommerce.sweetschoice.ECommerceApplication;
import com.axovel.ecommerce.sweetschoice.R;

/*  @author Umesh Chauhan
 *   Axovel Private Limited
 */
public class Activity_Login extends Activity {

    private EditText txtUserName;
    private EditText txtPassword;
    private TextView txtError;
    private int backButtonCount=0;
    Activity actLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Animation Right to Left
        overridePendingTransition(R.animator.slide_in_right,
                R.animator.slide_out_left);
        setContentView(R.layout.activity_login);
        actLogin = this;
        // Getting Views
        RelativeLayout rlLoginBox = (RelativeLayout) findViewById(R.id.rlLoginBox);
        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);
        Button btnGuestLogin = (Button) findViewById(R.id.btnGuestLogin);
        txtUserName = (EditText) findViewById(R.id.edtxtUsername);
        txtPassword = (EditText) findViewById(R.id.edtxtPassword);
        txtError = (TextView) findViewById(R.id.txtErr);
        txtError.setVisibility(View.GONE);
        // Apply Animation To Login Box
        TranslateAnimation t = new TranslateAnimation(0,0,-100,0);
        t.setDuration(400);
        t.setRepeatCount(2);
        t.setInterpolator(new AccelerateInterpolator());
        rlLoginBox.startAnimation(t);
        // OnClick Listener
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class SignUp extends DialogFragment {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        // Use the Builder class for convenient dialog construction
                        final View view = getActivity().getLayoutInflater().inflate(
                                R.layout.layout_signup, null);
                        // Create the AlertDialog object and return it
                        final AlertDialog registerDialog = new AlertDialog.Builder(getActivity()).setView(view)
                                .create();
                        registerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        final Button btnSave = (Button) view
                                .findViewById(R.id.btnSignUpSave);
                        final EditText firstName = (EditText) view
                                .findViewById(R.id.edtxtFirstName);
                        final EditText lastName = (EditText) view
                                .findViewById(R.id.edtxtLastName);
                        final EditText email = (EditText) view
                                .findViewById(R.id.edtxtEmail);
                        final EditText pass = (EditText) view
                                .findViewById(R.id.edtxtPassword);
                        final TextView txtErr = (TextView) view
                                .findViewById(R.id.txtErrSignUp);
                        txtErr.setVisibility(View.GONE);
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(firstName.getText()!=null && firstName.getText().length()>0 && !firstName.getText().equals(R.string.first_name)){
                                    if(lastName.getText()!=null && lastName.getText().length()>0 && !lastName.getText().equals(R.string.last_name)){
                                        if(ECommerceApplication.mGeneral.isValidEmail(email.getText())){
                                            if(!pass.getText().equals(R.string.password) && pass.getText().length()>=8){

                                            }else{
                                                txtErr.setText(R.string.err_password);
                                                txtErr.setVisibility(View.VISIBLE);
                                            }
                                        }else{
                                            txtErr.setText(R.string.err_email);
                                            txtErr.setVisibility(View.VISIBLE);
                                        }
                                    }else{
                                        txtErr.setText(R.string.err_lastname);
                                        txtErr.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    txtErr.setText(R.string.err_firstname);
                                    txtErr.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        return registerDialog;
                    }
                }
                DialogFragment dialog = new SignUp();
                dialog.show(getFragmentManager(), "SignUp");
            }
        });
        btnGuestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(actLogin, Activity_Ecomm_Base.class);
                startActivity(i);
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
