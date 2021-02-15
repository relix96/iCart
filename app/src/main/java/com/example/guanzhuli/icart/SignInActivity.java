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

import com.example.guanzhuli.icart.data.SPManipulation;
import com.example.guanzhuli.icart.service.ClientService;
import com.example.guanzhuli.icart.utils.API;
import com.example.guanzhuli.icart.utils.AppController;

import com.facebook.*;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.ErrorCode;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;

// http://rjtmobile.com/ansari/shopingcart/androidapp/shop_login.php?mobile=123456&password=ansari
public class SignInActivity extends AppCompatActivity {

    ClientService clientService;
    String cookie;

    private static final String LOGIN_URL = "http://192.168.80.1:8080/user/login/cliente/";
    private LoginButton mLoginButton;
    private CallbackManager callbackManager;
    private Button mButtonSignIn;
    private Button mFbButtonSignIn;
    private TextView mTextUsername, mTextPassword, mTextSignUp;
    private AppController mController;
    private RequestQueue mRequestQueue;
    private SPManipulation mSPManipulation;
    private RestTemplate restTemplate = new RestTemplate();

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

                try {
                    login();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

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

    public String login() throws JSONException, IOException {
        ClientService clientService;

        final String mail = mTextUsername.getText().toString();
        final String password = mTextPassword.getText().toString();
        String url = LOGIN_URL + mail;
        final RequestBody pwd = RequestBody.create(MediaType.parse("text/plain"),password);

        final String[] hashcode = {null};
        clientService = API.getClientService();
        Call<ResponseBody> call = clientService.loginClient(pwd,mail);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        hashcode[0] = response.body().string();
                        Toast.makeText(SignInActivity.this, hashcode[0], Toast.LENGTH_LONG).show();
                        mSPManipulation.saveName(mail);
                        mSPManipulation.saveEmail(mail);
                        mSPManipulation.savePwd(password);
                        mSPManipulation.saveMobile(mail);
                        Intent i = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(SignInActivity.this,response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });



        return hashcode[0];
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
