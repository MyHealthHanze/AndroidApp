package myhealth.com.myhealth.welcome;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

}
