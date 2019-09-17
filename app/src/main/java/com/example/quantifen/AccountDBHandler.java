package com.example.quantifen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountDBHandler extends SQLiteOpenHelper {

    private long update;

    private static final int        DB_VERSION = 3;
    private static final String     DB_NAME = "accountdb";
    private static final String     TABLE_Users = "accountdetails";
    private static final String     KEY_ID = "rowId";
    private static final String     KEY_GNAME = "gname";
    private static final String     KEY_FNAME = "fname";
    private static final String     KEY_DOB = "birthday";
    private static final String     KEY_HEI = "height";
    private static final String     KEY_WEI = "weight";

    private static final String     KEY_EMAIL = "email";
    private static final String     KEY_PASSWORD = "password";

    private static final String     KEY_PHONE = "phone";

    private static final String     KEY_EMERGENCYNAME = "ename";
    private static final String     KEY_EMERGENCYPHONE = "ephone";
    private static final String     KEY_EMERGENCYEMAIL = "eemail";

    //Default constructor of AccountDBHandler class
    public AccountDBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_GNAME + " TEXT,"
                + KEY_FNAME + " TEXT,"
                + KEY_DOB + " TEXT,"
                + KEY_HEI + " TEXT,"
                + KEY_WEI + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMERGENCYNAME + " TEXT,"
                + KEY_EMERGENCYPHONE + " TEXT,"
                + KEY_EMERGENCYEMAIL + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }

    // Adding new User Details
    void insertUserDetails(String email, String password, String gname, String fname, String birthday, String height, String weight, String phone, String ename, String ephone, String eemail){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_EMAIL, email);
        cValues.put(KEY_PASSWORD, password);
        cValues.put(KEY_GNAME, gname);
        cValues.put(KEY_FNAME, fname);
        cValues.put(KEY_DOB, birthday);
        cValues.put(KEY_HEI, height);
        cValues.put(KEY_WEI, weight);
        cValues.put(KEY_PHONE, phone);
        cValues.put(KEY_EMERGENCYNAME, ename);
        cValues.put(KEY_EMERGENCYPHONE, ephone);
        cValues.put(KEY_EMERGENCYEMAIL, eemail);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Users,null, cValues);
        db.close();
    }

    // Get User Details
    public ArrayList<HashMap<String, String>> GetUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT email, password, gname, fname, birthday, height, weight, phone, ename, ephone, eemail FROM "+ TABLE_Users;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("email",cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            user.put("password",cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
            user.put("gname",cursor.getString(cursor.getColumnIndex(KEY_GNAME)));
            user.put("fname",cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
            user.put("height",cursor.getString(cursor.getColumnIndex(KEY_HEI)));
            user.put("weight",cursor.getString(cursor.getColumnIndex(KEY_WEI)));
            user.put("phone", cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            user.put("ename",cursor.getString(cursor.getColumnIndex(KEY_EMERGENCYNAME)));
            user.put("ephone",cursor.getString(cursor.getColumnIndex(KEY_EMERGENCYPHONE)));
            user.put("eemail",cursor.getString(cursor.getColumnIndex(KEY_EMERGENCYEMAIL)));
            user.put("birthday",cursor.getString(cursor.getColumnIndex(KEY_DOB)));

            userList.add(user);
        }
        return  userList;
    }

    // Get User Details based on userid
    public ArrayList<HashMap<String, String>> GetUserByUserId(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT email, password, gname, fname, birthday, height, weight, phone, ename, ephone, eemail FROM "+ TABLE_Users;
        Cursor cursor = db.query(TABLE_Users, new String[]{KEY_EMAIL, KEY_PASSWORD, KEY_GNAME, KEY_FNAME, KEY_DOB, KEY_HEI, KEY_WEI, KEY_PHONE, KEY_EMERGENCYNAME, KEY_EMERGENCYPHONE, KEY_EMERGENCYEMAIL}, KEY_ID+ "=?",new String[]{String.valueOf(userid)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();;
            user.put("email",cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            user.put("password",cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
            user.put("gname",cursor.getString(cursor.getColumnIndex(KEY_GNAME)));
            user.put("fname",cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
            user.put("height",cursor.getString(cursor.getColumnIndex(KEY_HEI)));
            user.put("weight",cursor.getString(cursor.getColumnIndex(KEY_WEI)));
            user.put("phone", cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            user.put("ename",cursor.getString(cursor.getColumnIndex(KEY_EMERGENCYNAME)));
            user.put("ephone",cursor.getString(cursor.getColumnIndex(KEY_EMERGENCYPHONE)));
            user.put("eemail",cursor.getString(cursor.getColumnIndex(KEY_EMERGENCYEMAIL)));
            user.put("birthday",cursor.getString(cursor.getColumnIndex(KEY_DOB)));

            userList.add(user);
        }
        return  userList;
    }

    public void DeleteUser(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Users, "id = ?",new String[]{String.valueOf(userid)});
        db.close();
    }

    // Update User Details
    public long UpdateUserDetails(int id, String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        String[] whereArgs = {String.valueOf(id)};
        if (key.contentEquals(KEY_HEI)) {
            cVals.put(KEY_HEI, value);
            update = db.update(TABLE_Users, cVals, KEY_ID+" = ?" , null);
            System.out.println(value + "hello\n");
            System.out.println(update+"\n");

        }
        return update;
        /*cVals.put(KEY_EMAIL, email);
        cVals.put(KEY_PASSWORD, password);
        cVals.put(KEY_DOB, birthday);
        cVals.put(KEY_HEI, height);
        cVals.put(KEY_WEI, weight);
        cVals.put(KEY_PHONE, phone);
        cVals.put(KEY_EMERGENCYNAME, ename);
        cVals.put(KEY_EMERGENCYPHONE, ephone);
        cVals.put(KEY_EMERGENCYEMAIL, eemail);*/

    }


    public static void deleteCart(Context context) {
        AccountDBHandler accountdbhandler = new AccountDBHandler(context);
        SQLiteDatabase db = accountdbhandler.getReadableDatabase();

        try {

            db.execSQL("DELETE FROM " + accountdbhandler.TABLE_Users);

        } catch (SQLiteException e) {
            e.printStackTrace();
        }

    }
}
