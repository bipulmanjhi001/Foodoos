package foodoo.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.widget.NestedScrollView;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import foodoo.com.api.URLs;
import foodoo.com.model.CommonUtils;
import foodoo.com.model.ConnectionDetector;
import foodoo.com.model.VolleySingleton;
import foodoo.com.ui.login.Login;

public class Register extends AppCompatActivity {

    EditText registerfirstname, registeraddress,registerphoneno,registeremailaddress,registerpassword,registerconfirmpassword;
    private String fname = null;
    private String mob_number = null;
    private String email=null;
    private String password=null;
    private String confirmpassword = null;
    Button registerbutton;
    NestedScrollView coordinatorlayout;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    Dialog myDialog2;
    String otp="",token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        coordinatorlayout=(NestedScrollView)findViewById(R.id.registercoordinatorlayout);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        registerfirstname=(EditText)findViewById(R.id.registerfirstname);
        registeraddress=(EditText)findViewById(R.id.registeraddress);
        registerphoneno=(EditText)findViewById(R.id.registerphoneno);
        registeremailaddress=(EditText)findViewById(R.id.registeremailaddress);
        registerpassword=(EditText)findViewById(R.id.registerpassword);
        registerconfirmpassword=(EditText)findViewById(R.id.registerconfirmpassword);
        registerbutton=(Button)findViewById(R.id.registerbutton);

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        myDialog2=new Dialog(Register.this);
    }

    private void validateData() {
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(registerfirstname.getText().toString()))
        {
            registerfirstname.setError("Required field!");
            focusView = registerfirstname;
            cancel = true;
        }
        else if(TextUtils.isEmpty(registerphoneno.getText().toString()))
        {
            registerphoneno.setError("Required field!");
            focusView = registerphoneno;
            cancel = true;
        }
        else if(TextUtils.isEmpty(registeremailaddress.getText().toString()))
        {
            registeremailaddress.setError("Required field!");
            focusView = registeremailaddress;
            cancel = true;
        }
        else if(TextUtils.isEmpty(registerpassword.getText().toString()))
        {
            registerpassword.setError("Required field!");
            focusView = registerpassword;
            cancel = true;
        }
        else if(TextUtils.isEmpty(registerconfirmpassword.getText().toString()))
        {
            registerconfirmpassword.setError("Required field!");
            focusView = registerconfirmpassword;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        }
        else
        {
            getTextValues();
        }
    }

    private void getTextValues() {

        fname = registerfirstname.getText().toString();
        mob_number = registerphoneno.getText().toString();
        email = registeremailaddress.getText().toString();
        password = registerpassword.getText().toString();
        confirmpassword=registerconfirmpassword.getText().toString();

        if(CommonUtils.mobileNumberPatternMatcher(mob_number) && password.equals(confirmpassword)){
            if (isInternetPresent) {
                RegisterNow();
            } else {
                Snackbar snackbar = Snackbar.make(coordinatorlayout, "Enable Internet!",
                         Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                             startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        });

                TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action );
                snackbarActionTextView.setTextSize(14);
                snackbarActionTextView.setTextColor(Color.RED);
                snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setMaxLines(1);
                textView.setTextSize(14);
                textView.setSingleLine(true);
                textView.setTypeface(null, Typeface.BOLD);
                snackbar.show();
            }
        }
        else{
            Toast.makeText(this, "Please Enter a correct Mobile number or check password!", Toast.LENGTH_SHORT).show();
        }
    }

    public void RegisterNow(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                JSONObject object=obj.getJSONObject("user");
                                token=object.getString("token");
                                otp= object.getString("otp");
                                ShowPopup();
                                showNotification(Register.this);

                            }else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
               {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", fname);
                params.put("password", confirmpassword);
                params.put("mobile", mob_number);
                params.put("email", email);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void ShowPopup() {
        TextView txtclose;
        final EditText etDigit1,etDigit2,etDigit3,etDigit4,etDigit5,etDigit6;
        AppCompatButton btnContinue;

        myDialog2.setContentView(R.layout.forgot_password2);
        myDialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCancelable(false);

        txtclose =(TextView) myDialog2.findViewById(R.id.txtclose);
        etDigit1=(EditText)myDialog2.findViewById(R.id.etDigit1);
        etDigit2=(EditText)myDialog2.findViewById(R.id.etDigit2);
        etDigit3=(EditText)myDialog2.findViewById(R.id.etDigit3);
        etDigit4=(EditText)myDialog2.findViewById(R.id.etDigit4);
        etDigit5=(EditText)myDialog2.findViewById(R.id.etDigit5);
        etDigit6=(EditText)myDialog2.findViewById(R.id.etDigit6);

        btnContinue=(AppCompatButton)myDialog2.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=etDigit1.getText().toString()+etDigit2.getText().toString()+etDigit3.getText().toString()+etDigit4.getText().toString()+etDigit5.getText().toString()+etDigit6.getText().toString();
                if(otp.length() == 6) {
                    OTP();
                }
                myDialog2.dismiss();
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.dismiss();
            }
        });

        myDialog2.show();
    }

    public void OTP(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
          {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("otp", otp);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Your OTP is :"+otp)
                .setContentTitle("Welcome to Foodoo");

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
