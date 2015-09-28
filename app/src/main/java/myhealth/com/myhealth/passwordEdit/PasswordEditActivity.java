package myhealth.com.myhealth.passwordEdit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import myhealth.com.myhealth.R;
import myhealth.com.myhealth.api.API;

public class PasswordEditActivity extends AppCompatActivity implements PasswordEditView {

    private String password;
    private EditText mPassword1;
    private EditText mPassword2;
    private Button mChangeButton;
    private PasswordEditPresenter mPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_edit);

        // Get the input fields
        mPassword1 = (EditText) findViewById(R.id.editPassword1);
        mPassword2 = (EditText) findViewById(R.id.editPassword2);
        Intent i = getIntent();
        password = i.getExtras().getString("old_password");

        // Create the new presenter
        mPasswordPresenter = new PasswordEditPresenter(this, new API());

        // Setup the button and its ClickListener
        mChangeButton = (Button) findViewById(R.id.change_button);

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordPresenter.onChangeClicked();
            }
        });
    }

    @Override
    public String getOldPassword() {
        return password;
    }

    @Override
    public String getNewPassword1() {
        return mPassword1.getText().toString();
    }

    @Override
    public String getNewPassword2() {
        return mPassword2.getText().toString();
    }

    @Override
    public void showPassword1Error(int resId) {
        mPassword1.setError(getString(resId));
    }

    @Override
    public void showPassword2Error(int resId) {
        mPassword1.setError(getString(resId));
    }
}
