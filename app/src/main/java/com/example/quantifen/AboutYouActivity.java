package com.example.quantifen;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AboutYouActivity extends AppCompatActivity {

    private Button      createAccountButton;

    private EditText    givenNameText;
    private EditText    familyNameText;
    private EditText    birthDateText;
    private EditText    heightText;
    private EditText    weightText;
    private EditText    phoneText;
    private EditText    emergencyNameText;
    private EditText    emergencyPhoneText;
    private EditText    emergencyEmailText;

    final Calendar birthdayCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_you);

        givenNameText = findViewById(R.id.given_names);
        familyNameText = findViewById(R.id.family_name);
        birthDateText = findViewById(R.id.date_of_birth);
        heightText = findViewById(R.id.height);
        weightText = findViewById(R.id.weight);
        phoneText = findViewById(R.id.phone);
        emergencyNameText = findViewById(R.id.emergency_contact_full_name);
        emergencyPhoneText = findViewById(R.id.emergency_contact_phone);
        emergencyEmailText = findViewById(R.id.emergency_contact_email);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                birthdayCalendar.set(Calendar.YEAR, year);
                birthdayCalendar.set(Calendar.MONTH, monthOfYear);
                birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AboutYouActivity.this, R.style.DatePickerDialogTheme, date, birthdayCalendar
                        .get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH),
                        birthdayCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        emergencyPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        createAccountButton = findViewById(R.id.btn_account_create);
        createAccountButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if (!validate()) {
                    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

                    createAccountButton.setEnabled(true);
                    return;
                }

                createAccountButton.setEnabled(false);

                String gname = givenNameText.getText().toString().trim();
                String fname = familyNameText.getText().toString().trim();
                String birthday = birthDateText.getText().toString().trim();
                String height = heightText.getText().toString().trim();
                String weight = weightText.getText().toString().trim();
                String phone = phoneText.getText().toString().trim();
                String ename = emergencyNameText.getText().toString().trim();
                String ephone = emergencyPhoneText.getText().toString().trim();
                String eemail = emergencyEmailText.getText().toString().trim();

                Intent intentAccount = getIntent();

                String email = intentAccount.getStringExtra("email");
                String password = intentAccount.getStringExtra("password");

                AccountDBHandler accountdbhandler = new AccountDBHandler(AboutYouActivity.this);
                accountdbhandler.insertUserDetails(email, password, gname, fname, birthday, height, weight, phone, ename, ephone, eemail);

                Intent intent = new Intent(AboutYouActivity.this, BluetoothActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Details Inserted Successfully", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void updateLabel() {
        String dateFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        birthDateText.setText(sdf.format(birthdayCalendar.getTime()));
    }

    public boolean validate(){

        boolean valid = true;

        String firstName = givenNameText.getText().toString().trim();
        String lastName = familyNameText.getText().toString().trim();
        String birthday = birthDateText.getText().toString().trim();
        String height = heightText.getText().toString().trim();
        String weight = weightText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        String ename = emergencyNameText.getText().toString().trim();
        String ephone = emergencyPhoneText.getText().toString().trim();
        String eemail = emergencyEmailText.getText().toString().trim();

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

        if (phone.isEmpty() || !Patterns.PHONE.matcher(ephone).matches()) {
            phoneText.setError("Required a valid phone number");
            valid = false;
        }else {
            phoneText.setError(null);
        }

        if(ename.isEmpty()){
            emergencyNameText.setError("Required Field");
            valid = false;
        }else {
            emergencyNameText.setError(null);
        }

        if(ephone.isEmpty() || !Patterns.PHONE.matcher(ephone).matches()){
            emergencyPhoneText.setError("Requires a valid phone number");
            valid = false;
        }else{
            emergencyPhoneText.setError(null);
        }

        if(eemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(eemail).matches()){
            emergencyEmailText.setError("Required a valid email address");
            valid = false;
        }else{
            emergencyEmailText.setError(null);
        }

        return valid;

    }

}
