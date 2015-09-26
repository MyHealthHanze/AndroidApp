package myhealth.com.myhealth.welcome;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return view;
    }
}
