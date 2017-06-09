package org.ssutown.manna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;


public class LoginActivity extends AppCompatActivity {

    private static final int KAKAO_LOGIN_SUCCESS = 200;
    private Handler handler;
    private ProgressDialog progressDialog;
    SessionCallback callback;
    private static final String TAG = "KakaoLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaologin);

        TextView textView = (TextView)findViewById(R.id.splash);
        final LoginButton loginButton = (LoginButton)findViewById(R.id.com_kakao_login);
        final Button exitButton = (Button)findViewById(R.id.exitButton);

        SessionCallback sessionCallback;

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Kakao Callback
                Log.e(TAG, "MainActivity Before new SessionCallback");
                callback = new SessionCallback();
                Log.e(TAG, "MainActivity After new SessionCallback");
                Session.getCurrentSession().addCallback(callback);
                Toast.makeText(getApplicationContext(),"check",Toast.LENGTH_SHORT).show();
                //여기서 캐쉬에 기록이 있는지 없는지 검사한다
                if(!Session.getCurrentSession().checkAndImplicitOpen()){
                    loginButton.setVisibility(View.VISIBLE);
                    exitButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"login",Toast.LENGTH_SHORT).show();
                }
            }
        }, 2500);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /**카카오톡 로그아웃 요청**/
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출합니다.
        //테스트 하시기 편하라고 매번 로그아웃 요청을 수행하도록 코드를 넣었습니다 ^^
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        setResult(100, null);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    public void onBackPressed() {
        setResult(100, null);
        finish();
    }


    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e(TAG, "MainActivity onSessionClosed");
                }

                @Override
                public void onNotSignedUp() {
                    Log.e(TAG, "MainActivity onNotSignedUp");
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    Log.i("UserProfile", userProfile.toString() + "id : " + userProfile.getId());
                    Log.e(TAG, "MainActivity OnSuccess");
                    Toast.makeText(getApplicationContext(),String.valueOf(userProfile.getId()),Toast.LENGTH_SHORT).show();

                    SharedPreferences kakao_id = getSharedPreferences("KAKAO_ID",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = kakao_id.edit();
                    editor.putLong("KAKAO_ID",userProfile.getId());
                    editor.commit();

                    SharedPreferences login_State = getSharedPreferences("login_State",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = login_State.edit();
                    editor.putBoolean("login_State",true);
                    editor.commit();

                    finish();
                }

            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("myLog","onSessionOpenFailed"+"onSessionOpenFaild");
            if(exception != null){
                Logger.e(exception);
            }
        }

    }
}
