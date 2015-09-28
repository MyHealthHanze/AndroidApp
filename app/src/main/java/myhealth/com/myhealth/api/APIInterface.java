package myhealth.com.myhealth.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public interface APIInterface {

    void onResponse(String response);

    void onErrorResponse(VolleyError errorResponse);

}
