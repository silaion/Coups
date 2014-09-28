package com.example.coups;

import android.app.AlertDialog;
import android.content.Context;
import com.kakao.*;

/**
 * Created by DaeSeung on 2014-08-18.
 */
public class Kakaolink {

    private final String imageSrc = "http://dn.api1.kage.kakao.co.kr/14/dn/btqaWmFftyx/tBbQPH764Maw2R6IBhXd6K/o.jpg";
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private Context ct;

    public Kakaolink(Context c){
       ct = c;
    }
    public void sendKakaoTalkLink() {
        try {
                kakaoLink = KakaoLink.getKakaoLink(ct);
                //kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
                kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                kakaoTalkLinkMessageBuilder.addText("Coups");
                kakaoTalkLinkMessageBuilder.addImage(imageSrc, 300, 200);

            // ���� ��ġ�Ǿ� �ִ� ��� kakao<app_key>://kakaolink?execparamkey1=1111 �� �̵�. ���� ��ġ�Ǿ� ���� ���� ��� market://details?id=com.kakao.sample.kakaolink&referrer=kakaotalklink �Ǵ� https://itunes.apple.com/app/id12345�� �̵�
                kakaoTalkLinkMessageBuilder.addAppLink("Coups�� ��ũ",
                    new AppActionBuilder()
                            .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder()
                                    .setExecuteParam("execparamkey1=1111").setMarketParam("referrer=Coups").build())
                            .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE)
                                    .setExecuteParam("execparamkey1=1111").build()).build());

            // ������Ʈ�� ��ϵ� kakao<app_key>://kakaolink�� �̵�
                kakaoTalkLinkMessageBuilder.addAppButton("������ �̵�");

            //kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), this);
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), ct);
        } catch (KakaoParameterException e) {
            alert(e.getMessage());
        }
    }

    private void alert(String message) {
        new AlertDialog.Builder(ct)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }
}
