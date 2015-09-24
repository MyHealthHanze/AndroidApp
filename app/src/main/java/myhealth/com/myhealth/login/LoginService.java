package myhealth.com.myhealth.login;

import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import myhealth.com.myhealth.R;
import myhealth.com.myhealth.passwordEdit.PasswordEditActivity;
import myhealth.com.myhealth.welcome.WelcomeActivity;

public class LoginService {

    private LoginActivity mView;
    private LoginPresenter mPresenter;

    public LoginService(LoginActivity view, LoginPresenter presenter) {
        mView = view;
        mPresenter = presenter;
    }

    public void login(final String email, final String password) {
        String url = "https://myhealth-hanze.herokuapp.com/api/v1/user/login";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        // Get and save the JWT
                        String token = jsonResponse.getString("token");
                        mPresenter.saveJWT(token);
                        // check if this is the first login
                        if (jsonResponse.has("changePassword") && jsonResponse.getBoolean("changePassword")){
                            Intent i = new Intent(mView, PasswordEditActivity.class);
                            i.putExtra("old_password", mView.getPassword());
                            mView.startActivity(i);
                        }
                        else {
                            // Start the welcome screen
                            mView.startActivity(new Intent(mView, WelcomeActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // This means the credentials are incorrect or something went wrong, so display an message
                    Toast.makeText(mView, mView.getString(R.string.login_unsuccessful), Toast.LENGTH_SHORT).show();
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // The request body
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        Volley.newRequestQueue(mView).add(postRequest);
    }
}
