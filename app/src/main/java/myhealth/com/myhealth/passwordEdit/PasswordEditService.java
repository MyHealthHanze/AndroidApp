package myhealth.com.myhealth.passwordEdit;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
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
import myhealth.com.myhealth.login.LoginActivity;
import myhealth.com.myhealth.welcome.WelcomeActivity;

public class PasswordEditService {
    private PasswordEditActivity mView;
    private PasswordEditPresenter mPresenter;

    public PasswordEditService(PasswordEditActivity view, PasswordEditPresenter presenter) {
        mView = view;
        mPresenter = presenter;
    }

    public void changePassword(final String oldPassword, final String newPassword, final Context context) {
        String url = "https://myhealth-hanze.herokuapp.com/api/v1/user/password";

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // todo nog wat doen met de response
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // This means the credentials are incorrect or something went wrong, so display an message
                        Log.d("error", error.toString());
                        // todo nog wat doen met de error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // The request body
                params.put("old_password", oldPassword);
                params.put("new_password", newPassword);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                SharedPreferences prefs = context.getSharedPreferences("com.myhealth.app", Context.MODE_PRIVATE);
                String token = prefs.getString("jwt", "");
                Log.d("token", token);
                params.put("authorization", "Bearer "+ token);

                return params;
            }
        };

        Volley.newRequestQueue(mView).add(putRequest);
    }
}
