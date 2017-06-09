package org.ssutown.manna.KakaoLogin;


import android.app.Activity;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class KakaoSDKAdapter extends KakaoAdapter {

    /**
     * 로그인을 위해 Session을 생성하기 위해 필요한 옵션을 얻기위한 abstract class.
     * 기본 설정은 KakaoAdapter에 정의되어있으며, 설정 변경이 필요한 경우 상속해서 사용할 수 있다.
     */
    @Override
    public ISessionConfig getSessionConfig() {
           return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
    }

    //Application이 가지고있는 정보를 얻기위한 interface.
    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {

            @Override
            public Context getApplicationContext() {
                return GlobalApplication.getGlobalApplicationContext();
            }
        };
    }
}

