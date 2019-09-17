package com.example.quantifen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
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

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final int EMAIL_EXISTS = 100;

    private EditText                emailText;
    private EditText                passwordText;
    private EditText                confirmpasswordText;
    private Button                  signupButton;
    private TextView                loginLink;

    AccountDBHandler accountdbhandler = new AccountDBHandler(SignupActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        confirmpasswordText = findViewById(R.id.input_confirmpassword);

        signupButton = findViewById(R.id.btn_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink = findViewById(R.id.link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.ThemeOverlay_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmpassword = confirmpasswordText.getText().toString().trim();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);


        Intent intent = new Intent(SignupActivity.this, AboutYouActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        int errCode = 0;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString().trim();
        String confirmpassword = confirmpasswordText.getText().toString().trim();

        ArrayList<HashMap<String, String>> userList = accountdbhandler.GetUsers();

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).get("email").equals(email)) {
                errCode = EMAIL_EXISTS;
            }
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Valid email address required");
            valid = false;
        }else if(errCode == EMAIL_EXISTS){
            emailText.setError("Account with this email already exists");
            valid = false;

        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            passwordText.setError("Password must be between 4 and 16 alphanumeric characters long");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if(!password.contentEquals(confirmpassword)){
            confirmpasswordText.setError("Password must match");
            valid = false;
        }

        return valid;
    }

}
