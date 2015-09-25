package myhealth.com.myhealth.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import myhealth.com.myhealth.R;

public class LoginPresenter {
    private LoginView mView;
    private LoginService mService;
    private SharedPreferences.Editor mEditor;

    public LoginPresenter(LoginView view) {
        mView = view;
        mService = new LoginService((LoginActivity) mView, this);
    }

    public void setService(LoginService service){
        this.mService = service;
    }

    public void onLoginClicked() {
        String mEmail = mView.getEmail();
        if (mEmail.isEmpty() || !mEmail.contains("@")|| !mEmail.contains(".")){
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

    public void saveJWT(String token) {
        // Save the JWT in SharedPreferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences((LoginActivity) mView);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (mEditor != null){
            editor = mEditor;
        }
        editor.putString("jwt", token);
        editor.apply();
    }


    public void useEditor(SharedPreferences.Editor editor) {
        this.mEditor = editor;
    }
}
