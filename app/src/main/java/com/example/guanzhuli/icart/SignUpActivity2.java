package com.example.guanzhuli.icart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.guanzhuli.icart.data.Client;
import com.example.guanzhuli.icart.data.MoradaData;
import com.example.guanzhuli.icart.data.SPManipulation;


// url
// http://rjtmobile.com/ansari/shopingcart/androidapp/shop_reg.php?%20name=aamir&email=aa@gmail.com&mobile=555454545465&password=7011
public class SignUpActivity2 extends AppCompatActivity {
    private static final String REGISTER_URL = "http://rjtmobile.com/ansari/shopingcart/androidapp/shop_reg.php?%20";
    private TextView mTextAddress, mTextpostCode, mTextCity, mTextPwd, mTextRePwd, mTextSignIn;
    private Button mButtonSignUp;
    private RequestQueue mRequestQueue;
    private SPManipulation mSPManipulation;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        mSPManipulation = SPManipulation.getInstance(this);
        mRequestQueue =  Volley.newRequestQueue(SignUpActivity2.this);
        mTextAddress = (TextView) findViewById(R.id.sign_up_Address);
        mTextpostCode = (TextView) findViewById(R.id.sign_up_postCode);
        mTextCity = (TextView) findViewById(R.id.sign_up_City);
        mButtonSignUp = (Button) findViewById(R.id.button_signIn);

        client = (Client) getIntent().getSerializableExtra("client");

        Log.d("cliente", client.toString());

        ImageButton back1 = (ImageButton) findViewById(R.id.backToMain);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.getMorada().setMorada_1(mTextAddress.getText().toString());
                client.getMorada().setCod_postal(mTextCity.getText().toString());
                client.getMorada().setLocalidade(mTextCity.getText().toString());

                Intent intent = new Intent();
                intent.putExtra("client", client);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mTextAddress.getText().toString();
                // add method to check illegal character
                final String mobile = mTextpostCode.getText().toString();
                // add method to check all are numbers
                final String email = mTextCity.getText().toString();
                // add method to check email format
                final String pwd = mTextPwd.getText().toString();
                String pwd2 = mTextRePwd.getText().toString();
                if (!pwdMatch(pwd, pwd2)) {
                    Toast.makeText(SignUpActivity2.this, "Password does not match!", Toast.LENGTH_LONG).show();
                    return;
                }
                // add method to check pwd and pwd2 match
                // http://rjtmobile.com/ansari/shopingcart/androidapp/shop_reg.php?%20name=aamir&email=aa@gmail.com&mobile=555454545465&password=7011
                String url = REGISTER_URL + "name=" + username
                        + "&email=" + email + "&mobile=" + mobile
                        + "&password=" + pwd;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (s.contains("successfully")) {
                                    mSPManipulation.clearSharedPreference(SignUpActivity2.this);
                                    mSPManipulation.saveName(username);
                                    mSPManipulation.saveMobile(mobile);
                                    mSPManipulation.saveEmail(email);
                                    mSPManipulation.savePwd(pwd);
                                    Intent i = new Intent(SignUpActivity2.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SignUpActivity2.this, "network error!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextAddress.setText( client.getMorada().getMorada_1());
        mTextpostCode.setText( client.getMorada().getCod_postal());
        mTextCity.setText(  client.getMorada().getLocalidade());
    }

    private boolean pwdMatch(String pwd, String pwd2) {
        return pwd.equals(pwd2);
    }

    private boolean checkUsername(String username) {
        return true;
    }

    private boolean checkMobile(String mobile) {
        return true;
    }

    private boolean checkEmail(String email) {
        return true;
    }
}
