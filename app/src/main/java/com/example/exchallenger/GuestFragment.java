package com.example.exchallenger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.exchallenger.Helpers.MainHelper;
import com.example.exchallenger.Helpers.UserHelper;
import com.example.exchallenger.Listeners.AddListener;
import com.example.exchallenger.Listeners.EditListener;
import com.example.exchallenger.Models.User;
import com.example.exchallenger.Models.event.LoginSuccessEvent;
import com.example.exchallenger.base.BaseFragment;
import com.example.exchallenger.utils.JSONUtilParser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.utilityview.customview.CustomTextviewFonts;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class GuestFragment extends BaseFragment {

    private static final String TAG = GuestFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 69;

    @BindView(R.id.btnSignUp)
    CustomTextviewFonts btnSignUp;
    @BindView(R.id.btnToLogin)
    CustomTextviewFonts btnToLogin;
    @BindView(R.id.btn_login_fb)
    ViewGroup btnLoginFb;
    @BindView(R.id.btn_login_gg)
    ViewGroup btnLoginGg;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private FirebaseAuth firebaseAuth;


    public static GuestFragment newInstance() {
        GuestFragment guestFragment = new GuestFragment();
        return guestFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guest;
    }

    @Override
    protected void setUp() {
        mCallbackManager = CallbackManager.Factory.create();
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            if (response.getError() != null) {
                                Toast.makeText(getContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.v(TAG, "checkLogin" + response.toString());
                            JSONUtilParser jsonUtilParser = new JSONUtilParser();
                            String id = jsonUtilParser.getString("id", object);
                            String firstName = jsonUtilParser.getString("first_name", object);
                            String lastName = jsonUtilParser.getString("last_name", object);
                            String email = jsonUtilParser.getString("email", object);
                            String gender = jsonUtilParser.getString("gender", object);
                            String birthday = jsonUtilParser.getString("birthday", object);
                            JSONObject picture = jsonUtilParser.getJSONObject("picture", object);
                            JSONObject pictureData = jsonUtilParser.getJSONObject("data", picture);
                            String photoUrl = jsonUtilParser.getString("url", pictureData);
                            if (TextUtils.isEmpty(email))
                                email = id + "@facebook.com";
                            AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
                            Log.e("accessToken", currentAccessToken.getToken());
                            User user = new User();
                            user.setName(lastName);
                            user.setEmail(email);
                            user.setPhoto(photoUrl);
                            handleFacebookLoginResult(currentAccessToken, user);

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,email,last_name,picture,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "cancel login");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.toString());
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void handleFacebookLoginResult(AccessToken token, User newUser) {
        Log.d(TAG, "handleFacebookLoginResult:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            MyApplication.firebaseUser = user;
                            login(user.getUid(), newUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void login(String uid, User newUser) {
        MyApplication.getInstance().getUserHelper().getUsersInfo(uid, new UserHelper.GetUserInfo() {
            @Override
            public void onRead(Map<String, Object> user) {
                if (user != null) {
                    Log.d(TAG, "User existed" + user.toString());
                    newUser.setNumChallenger((long) user.get("numChallenger"));
                    newUser.setTotalPoints((long) user.get("totalPoints"));
                    newUser.setUserID(uid);
                    MyApplication.getInstance().getUserHelper().editUserInfo(
                            uid,
                            newUser, new EditListener() {
                                @Override
                                public void onSuccess() {
                                    MyApplication.user = newUser;
                                    EventBus.getDefault().post(new LoginSuccessEvent(newUser));
                                }
                            }
                    );
                } else {
                    MyApplication.getInstance().getUserHelper().addNewUser(
                            uid,
                            newUser, new AddListener() {
                                @Override
                                public void onAdd() {
                                    MyApplication.user = newUser;
                                    EventBus.getDefault().post(new LoginSuccessEvent(newUser));
                                }
                            }
                    );
                }
            }
        });


    }


    @OnClick(R.id.btnSignUp)
    public void onBtnSignUpClicked() {
    }

    @OnClick(R.id.btnToLogin)
    public void onBtnToLoginClicked() {
    }

    @OnClick(R.id.btn_login_fb)
    public void onBtnLoginFbClicked() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_gender",
                "user_birthday"));

    }

    @OnClick(R.id.btn_login_gg)
    public void onBtnLoginGgClicked() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(getContext(), "Google sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            MyApplication.firebaseUser = user;
                            User newUser = new User();
                            newUser.setEmail(account.getEmail());
                            newUser.setName(account.getGivenName());
                            newUser.setPhoto(account.getPhotoUrl().toString());
                            newUser.setUserID(user.getUid());
                            login(user.getUid(), newUser);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


}
