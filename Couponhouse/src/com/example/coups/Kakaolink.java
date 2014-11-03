package com.example.coups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import com.kakao.*;

/**
 * Created by DaeSeung on 2014-08-18.
 */
public class Kakaolink {

    private final String imageSrc = "http://dn.api1.kage.kakao.co.kr/14/dn/btqaWmFftyx/tBbQPH764Maw2R6IBhXd6K/o.jpg";
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private Context c;
    private Intent i;

    public Kakaolink(Context c){
        this.c = c;
    }
    public void sendKakaoTalkLink() {
        try {
            kakaoLink = KakaoLink.getKakaoLink(c);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoTalkLinkMessageBuilder.addText("Coups");
            kakaoTalkLinkMessageBuilder.addImage(imageSrc, 300, 200);

            // 앱이 설치되어 있는 경우 kakao<app_key>://kakaolink?execparamkey1=1111 로 이동. 앱이 설치되어 있지 않은 경우 market://details?id=com.kakao.sample.kakaolink&referrer=kakaotalklink 또는 https://itunes.apple.com/app/id12345로 이동
            kakaoTalkLinkMessageBuilder.addAppLink("Coups의 링크",
                    new AppActionBuilder()
                            .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder()
                                    .setExecuteParam("execparamkey1=1111").setMarketParam("referrer=Coups").build())
                            .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE)
                                    .setExecuteParam("execparamkey1=1111").build()).build());

            // 웹싸이트에 등록된 kakao<app_key>://kakaolink로 이동
            kakaoTalkLinkMessageBuilder.addAppButton("앱으로 이동");

            //kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
            Intent intent = new Intent(c, InfoclickActivity.class);
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), c);
        } catch (KakaoParameterException e) {
            alert(e.getMessage());
        }
    }

    private void alert(String message) {
        new AlertDialog.Builder(c)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }
}
