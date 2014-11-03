package com.example.coups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

public class InfoclickActivity extends FragmentActivity {
    private UiLifecycleHelper uiHelper;
    private Button facebook, twit, kakao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.infoclick);

        facebook = (Button)findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(InfoclickActivity.this)
                        .setLink("https://developers.facebook.com/android")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            }
        });

        twit = (Button)findViewById(R.id.twitter);
        twit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it = new Intent(getBaseContext(), TwitterLoad.class);
                it.putExtra("url", "https://play.google.com/store/apps/details?id=Coups");
                it.putExtra("msg", "Coupon House");
                startActivity(it);
            }
        });

        kakao = (Button)findViewById(R.id.kakao);
        kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = getBaseContext();
                Kakaolink kakaolink = new Kakaolink(getBaseContext());
                kakaolink.sendKakaoTalkLink();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}