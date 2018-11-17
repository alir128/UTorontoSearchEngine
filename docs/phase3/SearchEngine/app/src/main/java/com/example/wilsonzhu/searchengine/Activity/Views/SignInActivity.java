package com.example.wilsonzhu.searchengine.Activity.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.wilsonzhu.searchengine.Activity.Models.RequestQueueService;
import com.example.wilsonzhu.searchengine.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = SignInActivity.class.getCanonicalName();
    private static final String OAUTH_SERVER = "1025339532344-uf2j29c90kh4l7dsrim4l029j2ttfgc3.apps.googleusercontent.com";

    private static int REQUEST_CODE_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton mGoogleSignInButton;
    private Button mSignOutButton;
    private TextView mEmailDisplayName;
    private TextView mEmail;
    private ImageView mEmailImagePill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setUp();
    }

    private void setUp() {
        mSignOutButton = findViewById(R.id.signOutButton);
        mSignOutButton.setOnClickListener(this);
        mEmailImagePill = findViewById(R.id.email_image_pill);
        mEmailDisplayName = findViewById(R.id.email_display_name);
        mEmail = findViewById(R.id.email);

        mGoogleSignInButton = findViewById(R.id.googleSignInButton);
        mGoogleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mGoogleSignInButton.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(OAUTH_SERVER)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    @Override
    protected void onStart() {

        super.onStart();
        if (isAlreadyLoggedIn()) {
            updateUI(GoogleSignIn.getLastSignedInAccount(this));
        }
    }

    private boolean isAlreadyLoggedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;

    }

    @Override
    public void onClick(View v) {
        if (v == mGoogleSignInButton) {
            signIn();
        } else if (v == mSignOutButton) {
            signOut();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        recreate();
                    }
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            return;
        }
        mGoogleSignInButton.setVisibility(View.GONE);
        mEmailDisplayName.setText(account.getDisplayName());
        mEmail.setText(account.getEmail());
        mSignOutButton.setVisibility(View.VISIBLE);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            validateGoogleToken(account, idToken);

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void handleGoogleTokenValidationSuccess(final GoogleSignInAccount account) {
        String postUrl = "localhost:8080/loginmethod=post";
        String tag = "POST GOOGLE TOKEN TO BACKEND";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updateUI(account);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        RequestQueueService.getRequestQueueInstance(this).addToQueue(stringRequest, tag);
        updateUI(account);
    }

    private void validateGoogleToken(final GoogleSignInAccount account, String idToken) {
        String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + idToken;
        final String requestTAG = "GoogleAPI";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("aud").contains(OAUTH_SERVER)) {
                                handleGoogleTokenValidationSuccess(account);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        RequestQueueService.getRequestQueueInstance(this).addToQueue(jsonRequest, requestTAG);
    }
}
