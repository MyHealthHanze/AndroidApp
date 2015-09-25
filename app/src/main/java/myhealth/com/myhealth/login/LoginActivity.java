package myhealth.com.myhealth.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import myhealth.com.myhealth.R;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the input fields
        mEmail = (EditText) findViewById(R.id.email_input_field);
        mPassword = (EditText) findViewById(R.id.password_input_field);

        // Create the new presenter
        mLoginPresenter = new LoginPresenter(this);

        // Setup the button and its ClickListener
        mLoginButton = (Button) findViewById(R.id.login);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.onLoginClicked();
            }
        });
    }

    @Override
    public String getEmail() {
        return mEmail.getText().toString();
    }

    @Override
    public void showEmailError(int resId) {
        mEmail.setError(getString(resId));
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString();
    }

    @Override
    public void showPasswordError(int resId) {
        mPassword.setError(getString(resId));
    }
}
