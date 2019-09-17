package com.example.quantifen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int EMAIL_VALID = 100;
    private static final int PASSWORD_VALID = 101;

    private EditText        emailText;
    private EditText        passwordText;
    private Button          loginButton;
    private TextView        signupLink;

    AccountDBHandler accountdbhandler = new AccountDBHandler(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);

        loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink = findViewById(R.id.link_signup);
        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.ThemeOverlay_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        int emailVerify = 0;
        int passwordVerify = 0;

        final ArrayList<HashMap<String, String>> userList = accountdbhandler.GetUsers();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (!userList.isEmpty()){
            for (int i = 0; i <userList.size(); i++) {
                if (userList.get(i).get("email").equals(email)) {
                    emailText.setError(null);
                    emailVerify = EMAIL_VALID;
                    if (userList.get(i).get("password").equals(password)){
                        passwordText.setError(null);
                        passwordVerify = PASSWORD_VALID;
                        User.setId(i);
                    }
                }
            }
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else if (emailVerify != EMAIL_VALID){
            emailText.setError("Email or Password is incorrect");
            passwordText.setError("Email or Password is incorrect");
            valid = false;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else if (passwordVerify != PASSWORD_VALID){
            emailText.setError("Email or Password is incorrect");
            passwordText.setError("Email or Password is incorrect");
            valid = false;
        }


        return valid;
    }
}
