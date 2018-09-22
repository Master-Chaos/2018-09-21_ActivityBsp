package at.ac.fhstp.activitybsp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity"; //DEFINE STRING TAG
    private TextView mTxtOutput = null;
    private Button mBtnTwo = null; //METhode 1; m für member
    private int mCounter = 0;
    private  static int sCnt = 0; //s für Static
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100; // must be > 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtOutput = (TextView) findViewById(R.id.txtOutput); //liefert einen Typ view zurück
        mBtnTwo = (Button) findViewById(R.id.btnTwo); //METhode 1
        mBtnTwo.setOnClickListener(new View.OnClickListener() { //METhode 1
            @Override
            public void onClick(View view) {
                Log.i(TAG, "button tow!!!");
            }
        });

        Log.i(TAG, "onCreate()"); //OUTPUT MESSAGE IN LOGCAT BY START OF APP
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    public void btnonClick(View view) //Alt + enter includiert bib
    {
        ++mCounter;
        ++sCnt;

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            mTxtOutput.setText("permission not granted now ...");
            // --> >= API 23 and no permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            // TODO: access contacts
            mTxtOutput.setText(getContacts());
        }

        //mTxtOutput.setText("counter" + mCounter + "stat Cnt " + sCnt); //Print out Counter in Phone
        mTxtOutput.setText(getContacts());
        Log.i(TAG, "btnonClick()" + mCounter  + "stat Cnt " + sCnt); //PRINT OUT IN
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is granted ;-)", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Permission denied :-(", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getContacts()
    {
        String info = "";
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            return "Query failed (no contacts)";
        }
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            info += id + " " + name + "\n";
        }
        return info;
    }

}
