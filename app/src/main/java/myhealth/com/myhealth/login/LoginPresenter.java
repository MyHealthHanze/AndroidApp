package myhealth.com.myhealth.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import myhealth.com.myhealth.R;
import myhealth.com.myhealth.api.API;
import myhealth.com.myhealth.api.APIInterface;
import myhealth.com.myhealth.maingui.MainActivity;
import myhealth.com.myhealth.passwordEdit.PasswordEditActivity;

public class LoginPresenter implements APIInterface {
    private LoginActivity mView;
    private SharedPreferences.Editor mEditor;
    private API mAPI;

    public LoginPresenter(LoginActivity view, API api) {
        mView = view;
        mAPI = api;
    }

    /**
     * Handles the users interaction with the app
     */
    public void onLoginClicked() {
        String mEmail = mView.getEmail();
        if (mEmail.isEmpty() || !mEmail.contains("@") || !mEmail.contains(".")) {
            mView.showEmailError(R.string.email_error);
            return;
        }
        String mPassword = mView.getPassword();
        if (mPassword.isEmpty()) {
            mView.showPasswordError(R.string.password_error);
            return;
        }

        // Set the body for the request in a HashMap
        Map<String, String> params = new HashMap<>();
        params.put("email", mEmail);
        params.put("password", mPassword);

        // Fire off the API request
        mAPI.request(API.USER_LOGIN_POST, API.POST, this, mView, params, false);
    }

    /**
     * Saves the JWT string into the SharedPreferences for persistance
     *
     * @param token
     */
    public void saveJWT(String token) {
        // Save the JWT in SharedPreferences
        SharedPreferences.Editor editor;
        if (mEditor != null) { // using a mocked editor if it is set
            editor = mEditor;
        } else {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences((LoginActivity) mView);
            editor = sharedPref.edit();
        }
        editor.putString("jwt", token);
        editor.apply();
    }

    public void useEditor(SharedPreferences.Editor editor) {
        this.mEditor = editor;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            // Get and save the JWT
            String token = jsonResponse.getString("token");
            saveJWT(token);
            // check if this is the first login
            if (jsonResponse.has("changePassword") && jsonResponse.getBoolean("changePassword")) {
                Intent i = new Intent(mView, PasswordEditActivity.class);
                i.putExtra("old_password", mView.getPassword());
                (mView).startActivity(i);
            } else {
                // Start the welcome screen
                Intent i = new Intent(mView, MainActivity.class);
                i.putExtra("logged_in", true);
                (mView).startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the error response from the VolleyError object and shows a Toast message to the user
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        // Empty if you want the default implementation, see the API class
    }
}
