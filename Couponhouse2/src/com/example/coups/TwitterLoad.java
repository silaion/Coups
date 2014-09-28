package com.example.coups;

import twitter4j.HttpResponseCode;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DaeSeung on 2014-08-13.
 */
public class TwitterLoad extends Activity{

    private static final String SHARED_PREF_NAME = "Coups";

    private static final String TWITTER_CONSUMER_KEY = "qamJFsxW8LXXPLjRyoz9h5Jy7";
    private static final String TWITTER_CONSUMER_SECRET = "fiMhCeaWpdqSzNqp4BylHU1OH2VhKtF5OlQyNnJ20LYTI8QAvq";
    private static final String SHARED_PREF_KEY_TWITTER_CONSUMER_KEY = "TWITTER_CONSUMER_KEY";
    private static final String SHARED_PREF_KEY_TWITTER_CONSUMER_SECRET = "TWITTER_CONSUMER_SECRET";
    private static Uri TWITTER_CALLBACK_URL = Uri.parse("http://coups.co.kr");
    private static String msg = "Coupon House";
    private static String url = "http://172.30.96.200:8081/gcm_jsp/";
    private Uri uri;
    AsyncTask<?, ?, ?> twiterTask;
    AsyncTask<?, ?, ?> updateTask;
    AsyncTask<?, ?, ?> newIntentTask;
    String strError;
    String errorMsg = "";
    private Twitter mTwitter;
    private AccessToken accessToken;
    private RequestToken requestToken;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        twiterTask = new twiterTask().execute();
    }

    @Override
    protected void onNewIntent(Intent intent){
        Log.d("myLog", "onNewIntent");
        // WEB 에서 인증을 마치거나, 인증하지 않고 앱으로 돌아온 경우 이쪽으로 떨어지게 됩니다.
        super.onNewIntent(intent);

        uri = intent.getData();
        newIntentTask = new newIntentTask().execute();
        //TWITTER_Submit();
    }

    public void TWITTER_Submit(){

        Twitter_GetInstance();

        accessToken = Twitter_LoadAccessToken(0);
        Log.d("myLog", "accessToken :" + accessToken);

        if (null != accessToken){
            // SharedPreferences 에 저장된 access token 이 있으면
            // Twitter 객체에 access token 을 설정해 주고 바로 message 를 전송합니다.
            mTwitter.setOAuthAccessToken(accessToken);
            //TWITTER_UpdateStatus();
            updateTask = new updateTask().execute();
        } else{
            // SharedPreferences 에 저장된 accesstoken 이 없으면 인증 절차를 진행하게 됩니다.
            try{
                // request token 을 얻어옵니다.
                requestToken = mTwitter.getOAuthRequestToken(TWITTER_CALLBACK_URL.toString());
                System.out.println(TWITTER_CALLBACK_URL.toString());
            } catch (TwitterException te){
                te.printStackTrace();
            }
            // WEB OAuth 인증을 진행하기 위해 Browser 를 호출합니다.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthorizationURL())));
        }
    }

    private void Twitter_GetInstance(){
        if (null == mTwitter){

            mTwitter = new TwitterFactory().getInstance();
            // twitter instance 에 앱의 key 를 등록 해 줍니다.
            mTwitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
        }
    }

    private void Twitter_StoreAccessToken(long a_lUseID, AccessToken a_insAccessToken){

        String strToken = null;
        String strTokenSecret = null;

        strToken = a_insAccessToken.getToken();
        strTokenSecret = a_insAccessToken.getTokenSecret();

        StoreData(SHARED_PREF_KEY_TWITTER_CONSUMER_KEY, strToken);
        StoreData(SHARED_PREF_KEY_TWITTER_CONSUMER_SECRET, strTokenSecret);
    }

    private AccessToken Twitter_LoadAccessToken(long a_lUseID){
    	Log.d("myLog", "LoadAccessToken");

        AccessToken insAccessToken = null;
        String strToken = null;
        String strTokenSecret = null;


        //strToken = LoadData(SHARED_PREF_KEY_TWITTER_CONSUMER_KEY);
        //strTokenSecret = LoadData(SHARED_PREF_KEY_TWITTER_CONSUMER_SECRET);
        strToken = SHARED_PREF_KEY_TWITTER_CONSUMER_KEY;
        strTokenSecret = SHARED_PREF_KEY_TWITTER_CONSUMER_SECRET;
        System.out.println(strToken);
        System.out.println(strTokenSecret);


        if (null != strToken 
        		&& null != strTokenSecret 
        		&& !"".equals(strToken)
                && !"".equals(strTokenSecret)){

            try {
				insAccessToken = mTwitter.getOAuthAccessToken(strToken, strTokenSecret);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        } else{
            Log.v("myLog", "Twitter_LoadAccessToken() ## There's no saved strToken, strTokenSecret data");
        }
        
        return insAccessToken;
    }

    private boolean StoreData(String a_strKey, String a_strData){

        SharedPreferences insSharedPref = null;
        SharedPreferences.Editor insSharedPrefEditor = null;

        insSharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        insSharedPrefEditor = insSharedPref.edit();
        insSharedPrefEditor.putString(a_strKey, a_strData);

        return insSharedPrefEditor.commit();
    }

    private String LoadData(String a_strKey){

        SharedPreferences insSharedPref = null;
        insSharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return insSharedPref.getString(a_strKey, "");
    }

    private void TWITTER_UpdateStatus(){

        String strContent = null;
        String strTwitterID = null;
        strContent = msg + " " + url;
        strTwitterID = "RECEIVER_ID";
        try{
            // 특정 ID 에게 message 를 보내봅니다.
            mTwitter.updateStatus(strContent);
            //finish();
        } catch (TwitterException te){
            if (HttpResponseCode.FORBIDDEN == te.getStatusCode()){
                strError = null;
                strError = te.getErrorMessage();
                if (true == strError.contains("duplicate"))
                    errorMsg = "이미 트위터에 등록되었습니다.";
                else if (true == strError.contains("140"))
                    errorMsg = "등록이 실패하였습니다.! 140자가 넘었습니다.!";
                else{
                	
                }
                finish();
            } else if (HttpResponseCode.UNAUTHORIZED == te.getStatusCode()){
                errorMsg = "인증 오류입니다!";
                finish();
            }
            te.printStackTrace();
        }
    }

    private class twiterTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            TWITTER_Submit();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

    }

    private class updateTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override

        protected Void doInBackground(String... params) {
            TWITTER_UpdateStatus();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if (errorMsg.equals("")) {
                Toast.makeText(TwitterLoad.this, "트위터 공유가 완료되었습니다", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(TwitterLoad.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private class newIntentTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... params) {
        	Log.d("myLog", "in newIntentTask");
            if (null != uri && TWITTER_CALLBACK_URL.getScheme().equals(uri.getScheme())){
            // 내가 보낸 URI Scheme 과 동일하면
                String strOAuthVerifier = uri.getQueryParameter("oauth_verifier");    // oauth_verifier 로 넘어옵니다.
                // twitter 인증 page에서 인증하지 않고 앱으로 이동 링크를 클릭해서 바로 돌아오면 strOAuthVerifier == null 이므로 예외처리
                if (null == strOAuthVerifier) {
                    return null;
                }
                try {
                	System.out.println(requestToken);
                    // request token 과 oauth verifier 로 access token 을 얻어옵니다.
                    accessToken = mTwitter.getOAuthAccessToken(requestToken, strOAuthVerifier);
                    // 얻어온 access token 은 Shared Preference 에 고이 모셔 둡니다.
                    Twitter_StoreAccessToken(mTwitter.verifyCredentials().getId(), accessToken);
                    // access token 이 생겼으니 message 를 전송합니다.
                } catch (TwitterException te){
                    Log.e("myLog", te.getMessage());
                }
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(Void result) {
            updateTask = new updateTask().execute();
        }
    }
}
