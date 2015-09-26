package myhealth.com.myhealth.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import myhealth.com.myhealth.R;

public class WelcomeActivity extends AppCompatActivity {

    private EditText mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mToken = (EditText) findViewById(R.id.token);

        // Get the JWT
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String token = sharedPref.getString("jwt", null);

        // Display the token on screen
        if (token != null) {
            mToken.setText("You are logged in with this token:\n\n" + token + "\n\nThis token is saved in SharedPreferences under the key \"jwt\"");
        } else {
            mToken.setText("Something went wrong, token is not saved");
        }
    }

    /**
     * Disables the apps history if the user logs in or changes its password for the first time
     */
    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        // Get the "logged_in" extra from the intent, sets false as the default value
        boolean loggedIn = i.getExtras().getBoolean("logged_in", false);
        // Finish the app (closes app) when the user presses back and comes from login
        if (loggedIn) {
            return;
        } else { // Otherwise act as normal, call the super method
            super.onBackPressed();
            return;
        }
    }

}
