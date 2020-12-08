package com.hosinc.shopperapp.shopper.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hosinc.shopperapp.shopper.app.adapters.ShopperContract;
import com.hosinc.shopperapp.shopper.app.biz.profile.ActivityForgotPassword;


public class BsUsLoginRegister extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private TextView signIn, signUp, signIn_signUp_txt, forgot_password;
    private Button signIn_signUp_btn;
    private EditText etEmail, etPassword;
    private String email, password;
    private RelativeLayout rootLayout;
    private ProgressBar progressBar;
    private Boolean isLogin = true;
    private CallbackManager mCallbackManager;
    private static final String TAG = "FACEBOOK LOG";
    GoogleSignInClient mGoogleSignInClient;
    LoginButton loginButton;
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.us_bs_login_register);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
//        signIn_signUp_txt = findViewById(R.id.signin_signup_txt);
        forgot_password = findViewById(R.id.txt_forgot_password_login);
        signIn_signUp_btn = findViewById(R.id.sign_in_sign_up_btn);
        etEmail = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);
        rootLayout = findViewById(R.id.root_login);
        ImageView btFBSignIn = findViewById(R.id.fb_sign_in);
        progressBar = findViewById(R.id.progress_bar_login);
        ImageView btnGooglelogin = findViewById(R.id.btSignInGoogleCustom);

        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent i = new Intent(BsUsLoginRegister.this, UsGetStarted.class);
            startActivity(i);
        }

        signIn.setTextColor(Color.parseColor("#d4e9fc"));
        signIn.setBackgroundColor(Color.parseColor("#248ff1"));

        signIn_signUp_btn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (isLogin) {
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                Log.d(TAG, "onClick: " + email + "\n" + password);
                if (TextUtils.isEmpty(email)) {
                    Snackbar snackbar = Snackbar
                            .make(rootLayout, "Oops,Please Enter Email address", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Snackbar snackbar = Snackbar
                            .make(rootLayout, "Oops,Please Enter password", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(BsUsLoginRegister.this, task -> {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    etPassword.setError(getString(R.string.minimum_password));
                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(rootLayout, "Oops,Please make sure your passwords are correct", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } else {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    if (user.getEmail() != null) {
                                        final String email = user.getEmail();
                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful() && task1.getResult() != null) {
                                                        String token = task1.getResult().getToken();
                                                        DocumentReference userProfile = db.
                                                                collection(ShopperContract.USER_DETAILS).document(email);
                                                        userProfile.update("regToken", token);
                                                    }
                                                });
                                    }
                                    Intent i = new Intent(BsUsLoginRegister.this, UsGetStarted.class);
                                    startActivity(i);
                                    Snackbar snackbar = Snackbar
                                            .make(rootLayout, "Hooray,Welcome", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                                finish();
                            }
                        });
            } else {
                SignUpMethod();
            }

        });


        signIn.setOnClickListener(view -> {
            isLogin = true;
            signIn.setTextColor(Color.parseColor("#d4e9fc"));
            signIn.setBackgroundColor(Color.parseColor("#248ff1"));
            signUp.setTextColor(Color.parseColor("#d4e9fc"));
            signUp.setBackgroundResource(R.drawable.bordershape);
//            signIn_signUp_txt.setText(R.string.login_swipe);
            signIn_signUp_btn.setText(R.string.login_button_text);
            forgot_password.setVisibility(View.VISIBLE);
        });

        signUp.setOnClickListener(view -> {
            isLogin = false;
            signUp.setBackgroundColor(Color.parseColor("#248ff1"));
            signIn.setBackgroundResource(R.drawable.bordershape);
//            signIn_signUp_txt.setText(R.string.register_swipe);
            signIn_signUp_btn.setText(R.string.register_button_text);
            forgot_password.setVisibility(View.INVISIBLE);
        });

        forgot_password.setOnClickListener(v -> {
            Intent i = new Intent(BsUsLoginRegister.this, ActivityForgotPassword.class);
            startActivity(i);
        });
    }

    private void SignUpMethod() {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Snackbar snackbar = Snackbar.make(rootLayout, "Oops,Please Enter Email address", Snackbar.LENGTH_LONG);
            snackbar.show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Snackbar snackbar = Snackbar.make(rootLayout, "Oops,Please Enter password",
                    Snackbar.LENGTH_LONG);
            snackbar.show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.length() < 6) {
            Snackbar snackbar = Snackbar.make(rootLayout, "Oops,Password too short,enter 6 characters atleast",
                    Snackbar.LENGTH_LONG);
            snackbar.show();
            progressBar.setVisibility(View.GONE);
            return;
        }

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        btnGooglelogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, 101);
//            }
//        });
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
////        signInButton = findViewById(R.id.btSignInGoogle);
//
//        authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // user auth state is changed - user is not null
//                    startActivity(new Intent(BsUsLoginRegister.this, HomeNavigation.class));
//                }
//            }
//        };
//
////        signInButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
////                startActivityForResult(signInIntent, 101);
////            }
////        });
//
//        private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//
//            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//            auth.signInWithCredential(credential)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//
//                                boolean newUser = task.getResult().getAdditionalUserInfo().isNewUser();
//                                if (newUser) {
//                                    startActivity(new Intent(BsUsLoginRegister.this, UsGetStarted.class));
//                                } else {
//                                    FirebaseUser user = auth.getCurrentUser();
//                                    assert user != null;
//                                    if (user.getDisplayName() != null) {
//                                        Toast.makeText(getApplicationContext(), "Welcome back " + user.getDisplayName(), Toast.LENGTH_LONG).show();
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "Welcome back " + user.getEmail(), Toast.LENGTH_LONG).show();
//                                    }
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Intent intent = new Intent(BsUsLoginRegister.this, HomeNavigation.class);
//
//                                    intent.putExtra("user", user);
//                                    startActivity(intent);
//                                }
//
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_LONG).show();
//                            }
//
//                            // ...
//                        }
//                    });
//        }

        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Snackbar snackbar = Snackbar.make(rootLayout,
                                    "Oops,User with this  Email address,already exists", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(rootLayout,
                                    "Sign Up Failed", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(rootLayout, "User Created With This Email Address", Snackbar.LENGTH_LONG);
                        snackbar.show();

                        Intent intent = new Intent(BsUsLoginRegister.this, HomeNavigation.class);
                        startActivity(intent);
                    }
                });
        // Initialize Facebook Login btn_free_trial
        mCallbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.buttonFacebookLogin);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        View btFBSignIn = null;
        btFBSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Sign In Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            boolean newUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (newUser) {
                                startActivity(new Intent(BsUsLoginRegister.this, UsGetStarted.class));
                            } else {
                                FirebaseUser user = auth.getCurrentUser();
                                assert user != null;
                                if (user.getDisplayName() != null) {
                                    Toast.makeText(getApplicationContext(), "Welcome back " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Welcome back " + user.getEmail(), Toast.LENGTH_LONG).show();
                                }
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(BsUsLoginRegister.this, HomeNavigation.class);

                                intent.putExtra("user", user);
                                startActivity(intent);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean newUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (newUser) {
                                startActivity(new Intent(BsUsLoginRegister.this, UsGetStarted.class));
                            } else {
                                FirebaseUser user = auth.getCurrentUser();
                                assert user != null;
                                if (user.getDisplayName() != null) {
                                    Toast.makeText(getApplicationContext(), "Welcome back " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Welcome back " + user.getEmail(), Toast.LENGTH_LONG).show();
                                }
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(BsUsLoginRegister.this, HomeNavigation.class);

                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(BsUsLoginRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // user auth state is changed - user is not null
                startActivity(new Intent(BsUsLoginRegister.this, HomeNavigation.class));
                finish();
            }
        }


    };
}


