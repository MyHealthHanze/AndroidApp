package myhealth.com.myhealth.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import myhealth.com.myhealth.R;

public class LoginPresenter {
    private LoginView mView;
    private LoginService mService;
    private SharedPreferences.Editor mEditor;

    public LoginPresenter(LoginView view) {
        mView = view;
        mService = new LoginService((LoginActivity) mView, this);
    }

    public void setService(LoginService service) {
        this.mService = service;
    }

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
        mService.login(mEmail, mPassword);
    }

    /**
     * Saves the JWT string into the SharedPreferences for persistance
     *
     * @param token
     */
    public void saveJWT(String token) {
        // Save the JWT in SharedPreferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences((LoginActivity) mView);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (mEditor != null) {
            editor = mEditor;
        }
        editor.putString("jwt", token);
        editor.apply();
    }

    /**
     * Parses the error response from the VolleyError object and shows a Toast message to the user
     *
     * @param error
     */
    public void handleErrorResponse(VolleyError error) {
        try {
            // Get response body from the VolleyError object
            String responseBody = new String(error.networkResponse.data, "utf-8");
            // Get the JSON object
            JSONObject jsonObject = new JSONObject(responseBody);
            // Get the string from the "error" object
            String errorString = jsonObject.getString("error");
            // Create a Toast message
            Toast.makeText((LoginActivity) mView, errorString, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            // Handle a malformed json response
            Log.e("JSON Exception", e.toString());
        } catch (UnsupportedEncodingException e) {
            // Handle an unsupported encoding
            Log.e("UnsupportedEncodingE", e.toString());
        }
    }

    public void useEditor(SharedPreferences.Editor editor) {
        this.mEditor = editor;
    }
}
