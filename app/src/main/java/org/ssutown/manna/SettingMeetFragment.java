package org.ssutown.manna;

import android.view.View;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.exception.KakaoException;

public class SettingMeetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        Button kakao_link = (Button)getView().findViewById(R.id.kakao_link);

        kakao_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invateKakao(v);
            }
        });

        return inflater.inflate(R.layout.settingmeet_fragment, container, false);

    }

    public void invateKakao(View v){
        try{
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getActivity());
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoTalkLinkMessageBuilder.addText("만나 초대하기");
            String url = "https://search.naver.com/search.naver?where=image&sm=tab_jum&ie=utf8&query=%EC%9D%B4%EB%AF%B8%EC%A7%80+url#";
            kakaoTalkLinkMessageBuilder.addImage(url,1080,1920);
            kakaoTalkLinkMessageBuilder.addAppButton("앱실행");
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder,getActivity());
        }catch (KakaoParameterException e){
            e.printStackTrace();
        }
    }
}