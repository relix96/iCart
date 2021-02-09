package com.example.guanzhuli.icart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.guanzhuli.icart.data.SPManipulation;
import com.example.guanzhuli.icart.utils.AppController;
import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

// http://rjtmobile.com/ansari/shopingcart/androidapp/shop_login.php?mobile=123456&password=ansari
public class SignInActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://192.168.80.1:8080/user/login/cliente/";
    private LoginButton mLoginButton;
    private CallbackManager callbackManager;
    private Button mButtonSignIn;
    private Button mFbButtonSignIn;
    private TextView mTextUsername, mTextPassword, mTextSignUp;
    private AppController mController;
    private RequestQueue mRequestQueue;
    private SPManipulation mSPManipulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_signin);
        mSPManipulation = SPManipulation.getInstance(this);
        mController = AppController.getInstance();
        mRequestQueue = mController.getRequestQueue();
        mTextUsername = (TextView) findViewById(R.id.Sign_in_username);
        mTextPassword = (TextView) findViewById(R.id.sign_in_password);
        mButtonSignIn = (Button) findViewById(R.id.button_sign_in);
        if (mSPManipulation.hasUserLoggedIn()) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
            return;
        }
        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mTextUsername.getText().toString();
                final String pwd = mTextPassword.getText().toString();
                String url = LOGIN_URL + email;


                JSONObject data = new JSONObject();
                try {
                    data.put("mail",email);
                    data.put("password", pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String[] requestBody = {data.toString()};


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONArray array = new JSONArray();
                                    JSONObject obj = array.getJSONObject(0);
                                    String msg = s;
                                    if (msg.contains("200")) {
                                        // new SPManipulation().save(SignInActivity.this, s);
                                        String username = obj.getString("UserName");
                                        String email = obj.getString("UserEmail");
                                        mSPManipulation.saveName(username);
                                        mSPManipulation.saveEmail(email);
                                        mSPManipulation.savePwd(pwd);
                                        mSPManipulation.saveMobile(email);
                                        Intent i = new Intent(SignInActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    return;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SignInActivity.this,
                                "Username and password does not match!", Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            requestBody[0] = postData(email,pwd);
                            return requestBody[0] == null ? null : requestBody[0].getBytes("utf-8");
                        } catch (UnsupportedEncodingException | JSONException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody[0], "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };


                mRequestQueue.add(stringRequest);
            }
        });
        mTextSignUp = (TextView) findViewById(R.id.to_sign_up);
        mTextSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        mLoginButton = new LoginButton(this);
        mLoginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("fblogin", "success");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    String email = object.getString("email");
                                    Log.e("fblogin", email);
                                    String id = object.getString("id");
                                    Log.e("fblogin", id);
                                    String name = object.getString("first_name").toLowerCase();
                                    Log.e("fblogin", name);
                                    mSPManipulation.saveName(name);
                                    mSPManipulation.saveEmail(email);
                                    mSPManipulation.savePwd(" ");
                                    mSPManipulation.saveMobile(id);
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("fblogin", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("fblogin", "error");

            }
        });
        mFbButtonSignIn = (Button)findViewById(R.id.button_fb_sign_in);
        mFbButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.performClick();
            }
        });

    }

    public String postData(String email, String pwd) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("mail",email);
        data.put("password", pwd);
        return data.toString();

    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
