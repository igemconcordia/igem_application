package com.example.quantifen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AboutYouActivity extends AppCompatActivity {

    private Button      createAccountButton;
    private EditText    givenNameText;
    private EditText    familyNameText;
    private EditText    birthDateText;
    private EditText    heightText;
    private EditText    weightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        givenNameText = findViewById(R.id.given_names);
        familyNameText = findViewById(R.id.family_name);
        birthDateText = findViewById(R.id.date_of_birth);
        heightText = findViewById(R.id.height);
        weightText = findViewById(R.id.weight);

        createAccountButton = findViewById(R.id.btn_account_create);
        createAccountButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent intent = new Intent(AboutYouActivity.this, DevicePairingActivity.class);
                startActivity(intent);
            }

        });

    }

    public boolean validate(){

        boolean valid = true;

        String firstName = givenNameText.getText().toString();
        String lastName = familyNameText.getText().toString();
        String birthday = birthDateText.getText().toString();
        String height = heightText.getText().toString();
        String weight = weightText.getText().toString();

        if(firstName.isEmpty()){
            givenNameText.setError("*Required Field");
            valid = false;
        }else{
            givenNameText.setError(null);
        }

        if(lastName.isEmpty()){
            familyNameText.setError("Required Field");
            valid = false;
        }else{
            familyNameText.setError(null);
        }

        if(birthday.isEmpty()){
            birthDateText.setError("Required Field");
            valid = false;
        }else{
            birthDateText.setError(null);
        }

        if(height.isEmpty()){
            heightText.setError("Required Field");
            valid = false;
        }else{
            heightText.setError(null);
        }

        if(weight.isEmpty()){
            weightText.setError("Required Field");
            valid = false;
        }else{
            weightText.setError(null);
        }

        return valid;

    }

}
