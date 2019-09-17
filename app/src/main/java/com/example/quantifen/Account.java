package com.example.quantifen;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Account extends AppCompatActivity {

    private Button          logoutButton;
    private Button          clearButton;

    private TextView        fullNameText;
    private TextView        birthdayText;
    private TextView        heightText;
    private TextView        weightText;
    private TextView        phoneText;
    private TextView        emergencyNameText;
    private TextView        emergencyPhoneText;
    private TextView        emergencyEmailText;
    private TextView        emailText;
    private TextView        passwordText;

    private TabLayout       quantifenTabs;
    private TabLayout.Tab   accountTab;
    AccountDBHandler accountdbhandler = new AccountDBHandler(Account.this);


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        fullNameText = findViewById(R.id.full_name);
        birthdayText = findViewById(R.id.date_of_birth);
        heightText = findViewById(R.id.height);
        weightText = findViewById(R.id.weight);
        phoneText = findViewById(R.id.phone);
        emergencyNameText = findViewById(R.id.emergency_contact_fullname);
        emergencyPhoneText = findViewById((R.id.emergencyphone));
        emergencyEmailText = findViewById(R.id.emergencyemail);

        emailText = findViewById(R.id.email_address);
        passwordText = findViewById(R.id.password);

        final ArrayList<HashMap<String, String>> userList = accountdbhandler.GetUsers();

        if (!userList.isEmpty()){

            String email = userList.get(User.getId()).get("email");
            String password = userList.get(User.getId()).get("password");
            String name = userList.get(User.getId()).get("gname") + " " + userList.get(User.getId()).get("fname");
            String birthday = userList.get(User.getId()).get("birthday");
            String height = userList.get(User.getId()).get("height");
            String weight = userList.get(User.getId()).get("weight");
            String phone = userList.get(User.getId()).get("phone");
            String ename = userList.get(User.getId()).get("ename");
            String ephone = userList.get(User.getId()).get("ephone");
            String eemail = userList.get(User.getId()).get("eemail");

            emailText.setText(email);
            passwordText.setText(password);
            fullNameText.setText(name);
            birthdayText.setText(birthday);
            heightText.setText(height + " cm");
            weightText.setText(weight + " kg");
            phoneText.setText(phone);
            emergencyNameText.setText(ename);
            emergencyPhoneText.setText(ephone);
            emergencyEmailText.setText(eemail);
        }

        //Update user details here
        emailText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Account.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.user_dialog_input_email, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Account.this);
                alertDialogBuilder.setView(mView);

                String oldEmail = userList.get(User.getId()).get("email");
                System.out.println(oldEmail);

                userList.get(User.getId()).replace("email", oldEmail, "listeven@hotmail.fr");
                accountdbhandler.UpdateUserDetails(User.getId(), "height", "111"); //For testing
                //System.out.println(update+"\n");

                emailText.setText(userList.get(User.getId()).get("email"));

                final EditText userInputDialogEmailText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                String newemail = userInputDialogEmailText.getText().toString().trim();

                                Intent refresh = new Intent(Account.this, Account.class);
                                startActivity(refresh);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilder.create();
                alertDialogAndroid.show();

            }
        });


        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try {
                    logout();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        });

        clearButton = findViewById(R.id.clear_data);
        clearButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try {
                    userList.clear();
                    accountdbhandler.deleteCart(Account.this);
                    Intent intent = new Intent(Account.this, Account.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        quantifenTabs = findViewById(R.id.tabs);
        accountTab = quantifenTabs.getTabAt(3);
        accountTab.select();
        quantifenTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()){
                    case 0:
                        Intent intent1 = new Intent(Account.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Account.this, CameraMenu.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(Account.this, LearnMenu.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        break;
                    default:
                        Intent intentd = new Intent(Account.this, Account.class);
                        startActivity(intentd);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){

            }

            public void onTabReselected(TabLayout.Tab tab){
                switch(quantifenTabs.getSelectedTabPosition()) {
                    case 0:
                        Intent intent1 = new Intent(Account.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Account.this, LearnMenu.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(Account.this, Account.class);
                        startActivity(intent3);
                        break;
                    default:
                        Intent intentd = new Intent(Account.this, Account.class);
                        startActivity(intentd);
                }
            }

        });
    }

    public void logout(){
        Intent intent = new Intent(Account.this, LogJoinActivity.class);
        startActivity(intent);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
