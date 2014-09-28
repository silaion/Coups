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
        // WEB ���� ������ ��ġ�ų�, �������� �ʰ� ������ ���ƿ� ��� �������� �������� �˴ϴ�.
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
            // SharedPreferences �� ����� access token �� ������
            // Twitter ��ü�� access token �� ������ �ְ� �ٷ� message �� �����մϴ�.
            mTwitter.setOAuthAccessToken(accessToken);
            //TWITTER_UpdateStatus();
            updateTask = new updateTask().execute();
        } else{
            // SharedPreferences �� ����� accesstoken �� ������ ���� ������ �����ϰ� �˴ϴ�.
            try{
                // request token �� ���ɴϴ�.
                requestToken = mTwitter.getOAuthRequestToken(TWITTER_CALLBACK_URL.toString());
                System.out.println(TWITTER_CALLBACK_URL.toString());
            } catch (TwitterException te){
                te.printStackTrace();
            }
            // WEB OAuth ������ �����ϱ� ���� Browser �� ȣ���մϴ�.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthorizationURL())));
        }
    }

    private void Twitter_GetInstance(){
        if (null == mTwitter){

            mTwitter = new TwitterFactory().getInstance();
            // twitter instance �� ���� key �� ��� �� �ݴϴ�.
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
            // Ư�� ID ���� message �� �������ϴ�.
            mTwitter.updateStatus(strContent);
            //finish();
        } catch (TwitterException te){
            if (HttpResponseCode.FORBIDDEN == te.getStatusCode()){
                strError = null;
                strError = te.getErrorMessage();
                if (true == strError.contains("duplicate"))
                    errorMsg = "�̹� Ʈ���Ϳ� ��ϵǾ����ϴ�.";
                else if (true == strError.contains("140"))
                    errorMsg = "����� �����Ͽ����ϴ�.! 140�ڰ� �Ѿ����ϴ�.!";
                else{
                	
                }
                finish();
            } else if (HttpResponseCode.UNAUTHORIZED == te.getStatusCode()){
                errorMsg = "���� �����Դϴ�!";
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
                Toast.makeText(TwitterLoad.this, "Ʈ���� ������ �Ϸ�Ǿ����ϴ�", Toast.LENGTH_SHORT).show();
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
            // ���� ���� URI Scheme �� �����ϸ�
                String strOAuthVerifier = uri.getQueryParameter("oauth_verifier");    // oauth_verifier �� �Ѿ�ɴϴ�.
                // twitter ���� page���� �������� �ʰ� ������ �̵� ��ũ�� Ŭ���ؼ� �ٷ� ���ƿ��� strOAuthVerifier == null �̹Ƿ� ����ó��
                if (null == strOAuthVerifier) {
                    return null;
                }
                try {
                	System.out.println(requestToken);
                    // request token �� oauth verifier �� access token �� ���ɴϴ�.
                    accessToken = mTwitter.getOAuthAccessToken(requestToken, strOAuthVerifier);
                    // ���� access token �� Shared Preference �� ���� ��� �Ӵϴ�.
                    Twitter_StoreAccessToken(mTwitter.verifyCredentials().getId(), accessToken);
                    // access token �� �������� message �� �����մϴ�.
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
