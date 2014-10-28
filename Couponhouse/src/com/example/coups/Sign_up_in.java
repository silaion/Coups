package com.example.coups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;

public class Sign_up_in {

    Thread th;
    Context context;
    String name, phoneNum, birth, gender;
    Global global;

    public Sign_up_in(Context context, String name,String gender,String birth,String phoneNum){
        this.context = context;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.phoneNum = phoneNum;
        global = new Global();
    }

    private ResponseHandler<String> responseHandler;
    //스레드 클래스
    //211.183.2.174:9000//login.action 으로
    //id와 pw 파라미터를 post방식으로 전송을 요청하고 전송이 끝나면
    //responseHandler를 호출하도록 설정★★★★
    class ThreadEx extends Thread {

        private String url = "http://192.168.0.21:8081/gcm_jsp";
        //private String url = "http://192.168.42.82:8081/gcm_jsp";
        //private String url = "http://172.17.108.168:8081/gcm_jsp";
        private String comment;

        public ThreadEx(int num){
            switch(num){
                case 1 : comment = "/login.action"; break;
                case 2 : comment = "/insert.action"; break;
                case 3 : comment = "/delete.action"; break;
            }
        }

        @Override
        public void run() {
            //전송을 요청할 주소를 문자열로 생성

            //요청을 처리하기  위한 클라이언트 생성
            HttpClient http = new DefaultHttpClient();
            try {

                //파라미터 설정
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                //파라미터 설정.
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("gender", gender));
                nameValuePairs.add(new BasicNameValuePair("birth", birth));
                nameValuePairs.add(new BasicNameValuePair("phoneNum", phoneNum));

                //포스트 방식으로 요청을 하기 위한 객체 생성(Get방식이면 Post 대신 get)
                System.out.println(url+comment);
                HttpPost httpPost = new HttpPost(url+comment);
                //파라미터 생성
                UrlEncodedFormEntity entityRequest =
                        new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                //파라미터를 httpPost에 부착
                httpPost.setEntity(entityRequest);

                //httpPost에게 요청하고 전송을 받으면 responseHandler를 호출.//responseHandler는 loginProcessing 메서드에 등록되어있음.
                http.execute(httpPost, responseHandler);
                //전공이 끝나면 responseHanler가 호출되고 그안에서 받은 데이터에 대한 파싱이 진행된다.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //메시지를 받아서 RESULT 키의 값을 확인해서 UI를 갱신하는 핸들러    //loginProcess에 메세지를 여기 handler에게 보내는 메서드가 있다. ★★(밑에보면된다 아복잡해)
    private final Handler handler = new Handler() {
        @Override
        //메시지를 받으면(Handler객체의 sendMessage()나
        //sendEmptyMessage() 호출)호출 되는 메서드.
        public void handleMessage(Message msg) {
            //화면에서 대화상자 제거

            //Result 키의 데이터를 문자열로 변환
            String result = msg.getData().getString("RESULT");

            //result의 값이 로그인에 성공하면 배경색을 변경하고 토스트를 출력
            if (result.equals("로그인성공")) {
                System.out.println("로그인 성공");
                Intent intent = new Intent(context,Tabview.class);
                context.startActivity(intent);
            }
            else if(result.equals("삽입성공")){
                Log.d("InsertAction", "삽입 성공!!");
            }
            else if(result.equals("삭제성공")){
                System.out.println("삭제 성공");
                Intent intent = new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
            else {
                Log.d("로그인 과정 실패 ", result);
            }
        }

    };


    //InputStream을 매개변수로 받아서 데이터를 줄단위로 전부 읽은 후
    //그 데이터를 파싱하고 파싱한 결과를 리턴하는 메서드
    //result라는 키의 문자열을 읽어내서 리턴
    private String parsingData(InputStream input) {
        //결과를 리턴할 변수
        System.out.println("parsingData 메서드 시작");
        String result = null;
        //데이터를 받아오는 동안 사용할 임시 변수
        StringBuilder sBuffer = new StringBuilder();

        //InputStream 객체를 가지고 문자단위로 읽을 수 있는 InputStreamReader 생성
        InputStreamReader isr = new InputStreamReader(input);

        //위의 객체를 가지고 줄단위로 문자를 읽어 낼 수 있는 BufferedReader 생성
        BufferedReader br = new BufferedReader(isr);


        try {
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                sBuffer.append(line);
            }
            br.close();
        } catch (Exception e) {
        }

        //저장한 데이터를 문자열로 변환
        String xml = sBuffer.toString();


        //xml 문자열 xml 파싱
        try {
            if (xml != null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                //문자열을 InputStream으로 변환
                InputStream is = new ByteArrayInputStream(xml.getBytes());
                //InputStream의 내용을 xml문서로    변환
                Document doc = documentBuilder.parse(is);

                //루트 태그 찾아오기
                Element element = doc.getDocumentElement();

                //result 태그의 데이터 가져오기
                NodeList items = element.getElementsByTagName("result");

                //result태그의 첫번째 값 찾아오기
                result = items.item(0).getFirstChild().getNodeValue();
            }
        } catch (Exception e) {
        }

        return result;
    }


    //버튼의 이벤트 처리에서 호출하는 메서드
    public void loginProcess() {
        //데이터를 전부 다운로드 받고 나면 호출되는 핸들러 생성
        responseHandler = new ResponseHandler<String>() {   //일종의 핸들러 등록인가...??

            @Override
            //데이터를 다운로드 받고 나면 호출되는 메서드. ★ 데이터를 다운받고나서 파싱을 실시한다.
            public String handleResponse(HttpResponse arg0)
                    throws ClientProtocolException, IOException {

                //parsingData 메서드의 결과를 저장할 변수
                String result = null;
                //받아온 데이터의 InputStream을 생성하기 위한 객체
                HttpEntity entity = arg0.getEntity();

                //parsingData를 호출하고 결과를 result에 저장
                result = parsingData(entity.getContent());

                System.out.println(result);

                //일반 핸들러에게 메시지를 전송하기 위해서 메시지 객체 생성
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (result.equals("1")) {
                    bundle.putString("RESULT", "로그인성공");
                } else {
                    bundle.putString("RESULT", "로그인실패");
                }
                message.setData(bundle);
                //핸들러에게 메시지를 전송함.
                handler.sendMessage(message);
                global.start = true;
                return result;
            }
        };
        //다이얼로그 생성해서 출력

        //스레드를 생성해서 시작
        th = new ThreadEx(1);
        th.start();
    }

    public void insertProcess() {
        //데이터를 전부 다운로드 받고 나면 호출되는 핸들러 생성
        responseHandler = new ResponseHandler<String>() {
            @Override
            //데이터를 다운로드 받고 나면 호출되는 메서드. ★ 데이터를 다운받고나서 파싱을 실시한다.
            public String handleResponse(HttpResponse arg0)
                    throws ClientProtocolException, IOException {

                //parsingData 메서드의 결과를 저장할 변수
                String result = null;
                //받아온 데이터의 InputStream을 생성하기 위한 객체
                HttpEntity entity = arg0.getEntity();

                //parsingData를 호출하고 결과를 result에 저장
                result = parsingData(entity.getContent());    //

                System.out.println(result);

                //일반 핸들러에게 메시지를 전송하기 위해서 메시지 객체 생성
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (result.equals("1")) {
                    bundle.putString("RESULT", "삽입성공");
                } else {
                    bundle.putString("RESULT", "삽입실패");
                }
                message.setData(bundle);
                //핸들러에게 메시지를 전송함.
                handler.sendMessage(message);
                return result;
            }
        };
        //다이얼로그 생성해서 출력

        //스레드를 생성해서 시작
        th = new ThreadEx(2);
        th.start();
    }

    public void deleteProcess() {
        //데이터를 전부 다운로드 받고 나면 호출되는 핸들러 생성
        responseHandler = new ResponseHandler<String>() {
            @Override
            //데이터를 다운로드 받고 나면 호출되는 메서드. ★ 데이터를 다운받고나서 파싱을 실시한다.
            public String handleResponse(HttpResponse arg0)
                    throws ClientProtocolException, IOException {

                //parsingData 메서드의 결과를 저장할 변수
                String result = null;
                //받아온 데이터의 InputStream을 생성하기 위한 객체
                HttpEntity entity = arg0.getEntity();

                //parsingData를 호출하고 결과를 result에 저장
                result = parsingData(entity.getContent());    //

                System.out.println(result);

                //일반 핸들러에게 메시지를 전송하기 위해서 메시지 객체 생성
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (result.equals("1")) {
                    bundle.putString("RESULT", "삭제성공");
                } else {
                    bundle.putString("RESULT", "삭제실패");
                }
                message.setData(bundle);
                //핸들러에게 메시지를 전송함.
                handler.sendMessage(message);
                return result;
            }
        };
        //다이얼로그 생성해서 출력

        //스레드를 생성해서 시작
        th = new ThreadEx(3);
        th.start();
    }
}
