package org.ssutown.manna;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.kakaolink.*;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.kakaolink.internal.KakaoTalkLinkProtocol;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.exception.KakaoException;
import com.kakao.message.*;
import com.kakao.util.helper.log.Logger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;


public class SettingMeetFragment extends Fragment {

    String meeting_id = null;
    String meeting_name = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.settingmeet_fragment, container, false);

        Button kakao_link = (Button)view.findViewById(R.id.kakao_link);

        meeting_id = ((MeetingActivity)getActivity()).meeting_id;
        meeting_name = ((MeetingActivity)getActivity()).meeting_name;

        Log.d(TAG, "onCreateView: " + meeting_id + " , " + meeting_name);
        kakao_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDefaultFeedTemplate();
            }
        });

        return view;

    }

    private void sendDefaultFeedTemplate() {

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("일정 공유 MANNA",
                        "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                        LinkObject.newBuilder().setWebUrl("https://dev.kakao.com")
                                .setMobileWebUrl("https://dev.kakao.com").build())
                        .setDescrption("MANNA로 초대되었습니다")
                        .build())
                .addButton(new ButtonObject("MANNA으로 이동하기", LinkObject.newBuilder()
                        .setWebUrl("'https://dev.kakao.com")
                        .setMobileWebUrl("'https://dev.kakao.com")
                        .setAndroidExecutionParams("meeting_info="+meeting_id+":"+meeting_name)
                        .build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(getActivity(), params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());

            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Toast.makeText(getActivity(),"초대성공이요",Toast.LENGTH_SHORT).show();
               // Intent i = getActivity().getIntent();
               // Uri uri=i.getData();
               // Toast.makeText(getActivity(),uri.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}