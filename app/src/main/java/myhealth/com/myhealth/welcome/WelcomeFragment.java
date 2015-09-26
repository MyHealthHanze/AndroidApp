package myhealth.com.myhealth.welcome;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import myhealth.com.myhealth.R;

public class WelcomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        EditText mToken = (EditText) view.findViewById(R.id.token);
        // Get the JWT
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
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
        return view;
    }
}
