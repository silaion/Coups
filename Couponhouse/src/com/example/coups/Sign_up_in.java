package com.example.coups;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class Sign_up_in {
	
	Thread th;
	Context context;
	String name, phoneNum, birth, gender;
	
	public Sign_up_in(Context context, String name,String gender,String birth,String phoneNum){
		this.context = context;
		this.name = name;
		this.gender = gender;
		this.birth = birth;
		this.phoneNum = phoneNum;
	}

	private ResponseHandler<String> responseHandler;
	 //������ Ŭ����
    //211.183.2.174:9000//login.action ����
    //id�� pw �Ķ���͸� post������� ������ ��û�ϰ� ������ ������
    //responseHandler�� ȣ���ϵ��� �����ڡڡڡ�
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
            //������ ��û�� �ּҸ� ���ڿ��� ����

            //��û�� ó���ϱ�  ���� Ŭ���̾�Ʈ ����
            HttpClient http = new DefaultHttpClient();
            try {

                //�Ķ���� ����
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                //�Ķ���� ����.
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("gender", gender));
                nameValuePairs.add(new BasicNameValuePair("birth", birth));
                nameValuePairs.add(new BasicNameValuePair("phoneNum", phoneNum));

                //����Ʈ ������� ��û�� �ϱ� ���� ��ü ����(Get����̸� Post ��� get)
                System.out.println(url+comment);
                HttpPost httpPost = new HttpPost(url+comment);
                //�Ķ���� ����
                UrlEncodedFormEntity entityRequest =
                        new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                //�Ķ���͸� httpPost�� ����
                httpPost.setEntity(entityRequest);

                //httpPost���� ��û�ϰ� ������ ������ responseHandler�� ȣ��.//responseHandler�� loginProcessing �޼��忡 ��ϵǾ�����.
                http.execute(httpPost, responseHandler);
                //������ ������ responseHanler�� ȣ��ǰ� �׾ȿ��� ���� �����Ϳ� ���� �Ľ��� ����ȴ�.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //�޽����� �޾Ƽ� RESULT Ű�� ���� Ȯ���ؼ� UI�� �����ϴ� �ڵ鷯    //loginProcess�� �޼����� ���� handler���� ������ �޼��尡 �ִ�. �ڡ�(�ؿ�����ȴ� �ƺ�����)
    private final Handler handler = new Handler() {
        @Override
        //�޽����� ������(Handler��ü�� sendMessage()��
        //sendEmptyMessage() ȣ��)ȣ�� �Ǵ� �޼���.
        public void handleMessage(Message msg) {
            //ȭ�鿡�� ��ȭ���� ����

            //Result Ű�� �����͸� ���ڿ��� ��ȯ
            String result = msg.getData().getString("RESULT");

            //result�� ���� �α��ο� �����ϸ� ������ �����ϰ� �佺Ʈ�� ���
            if (result.equals("�α��μ���")) {
            	System.out.println("�α��� ����");
            	Intent intent = new Intent(context,Tabview.class);
            	context.startActivity(intent);
            }
            else if(result.equals("���Լ���")){
            	System.out.println("���� ����");
            }
            else if(result.equals("��������")){
            	System.out.println("���� ����");
            	Intent intent = new Intent(context,MainActivity.class);
            	context.startActivity(intent);
            }
            else {
            	System.out.println("����");
            	Toast.makeText(context, "�ش� ���μ����� �������� ���߽��ϴ�.", Toast.LENGTH_LONG);
            }
        }

    };


    //InputStream�� �Ű������� �޾Ƽ� �����͸� �ٴ����� ���� ���� ��
    //�� �����͸� �Ľ��ϰ� �Ľ��� ����� �����ϴ� �޼���
    //result��� Ű�� ���ڿ��� �о�� ����
    private String parsingData(InputStream input) {
        //����� ������ ����
        System.out.println("parsingData �޼��� ����");
        String result = null;
        //�����͸� �޾ƿ��� ���� ����� �ӽ� ����
        StringBuilder sBuffer = new StringBuilder();

        //InputStream ��ü�� ������ ���ڴ����� ���� �� �ִ� InputStreamReader ����
        InputStreamReader isr = new InputStreamReader(input);

        //���� ��ü�� ������ �ٴ����� ���ڸ� �о� �� �� �ִ� BufferedReader ����
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

        //������ �����͸� ���ڿ��� ��ȯ
        String xml = sBuffer.toString();


        //xml ���ڿ� xml �Ľ�
        try {
            if (xml != null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                //���ڿ��� InputStream���� ��ȯ
                InputStream is = new ByteArrayInputStream(xml.getBytes());
                //InputStream�� ������ xml������ ��ȯ
                Document doc = documentBuilder.parse(is);

                //��Ʈ �±� ã�ƿ���
                Element element = doc.getDocumentElement();

                //result �±��� ������ ��������
                NodeList items = element.getElementsByTagName("result");

                //result�±��� ù��° �� ã�ƿ���
                result = items.item(0).getFirstChild().getNodeValue();
            }
        } catch (Exception e) {
        }

        return result;
    }


    //��ư�� �̺�Ʈ ó������ ȣ���ϴ� �޼���
    public void loginProcess() {
        //�����͸� ���� �ٿ�ε� �ް� ���� ȣ��Ǵ� �ڵ鷯 ����
        responseHandler = new ResponseHandler<String>() {   //������ �ڵ鷯 ����ΰ�...??

            @Override
            //�����͸� �ٿ�ε� �ް� ���� ȣ��Ǵ� �޼���. �� �����͸� �ٿ�ް��� �Ľ��� �ǽ��Ѵ�.
            public String handleResponse(HttpResponse arg0)
                    throws ClientProtocolException, IOException {

                //parsingData �޼����� ����� ������ ����
                String result = null;
                //�޾ƿ� �������� InputStream�� �����ϱ� ���� ��ü
                HttpEntity entity = arg0.getEntity();

                //parsingData�� ȣ���ϰ� ����� result�� ����
                result = parsingData(entity.getContent());

                System.out.println(result);

                //�Ϲ� �ڵ鷯���� �޽����� �����ϱ� ���ؼ� �޽��� ��ü ����
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (result.equals("1")) {
                    bundle.putString("RESULT", "�α��μ���");
                } else {
                    bundle.putString("RESULT", "�α��ν���");
                }
                message.setData(bundle);
                //�ڵ鷯���� �޽����� ������.
                handler.sendMessage(message);
                return result;
            }
        };
        //���̾�α� �����ؼ� ���

        //�����带 �����ؼ� ����
        th = new ThreadEx(1);
        th.start();
    }

    public void insertProcess() {
        //�����͸� ���� �ٿ�ε� �ް� ���� ȣ��Ǵ� �ڵ鷯 ����
        responseHandler = new ResponseHandler<String>() {
            @Override
            //�����͸� �ٿ�ε� �ް� ���� ȣ��Ǵ� �޼���. �� �����͸� �ٿ�ް��� �Ľ��� �ǽ��Ѵ�.
            public String handleResponse(HttpResponse arg0)
                    throws ClientProtocolException, IOException {

                //parsingData �޼����� ����� ������ ����
                String result = null;
                //�޾ƿ� �������� InputStream�� �����ϱ� ���� ��ü
                HttpEntity entity = arg0.getEntity();

                //parsingData�� ȣ���ϰ� ����� result�� ����
                result = parsingData(entity.getContent());    //

                System.out.println(result);

                //�Ϲ� �ڵ鷯���� �޽����� �����ϱ� ���ؼ� �޽��� ��ü ����
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (result.equals("1")) {
                    bundle.putString("RESULT", "���Լ���");
                } else {
                    bundle.putString("RESULT", "���Խ���");
                }
                message.setData(bundle);
                //�ڵ鷯���� �޽����� ������.
                handler.sendMessage(message);
                return result;
            }
        };
        //���̾�α� �����ؼ� ���

        //�����带 �����ؼ� ����
        th = new ThreadEx(2);
        th.start();
    }

    public void deleteProcess() {
        //�����͸� ���� �ٿ�ε� �ް� ���� ȣ��Ǵ� �ڵ鷯 ����
        responseHandler = new ResponseHandler<String>() {
            @Override
            //�����͸� �ٿ�ε� �ް� ���� ȣ��Ǵ� �޼���. �� �����͸� �ٿ�ް��� �Ľ��� �ǽ��Ѵ�.
            public String handleResponse(HttpResponse arg0)
                    throws ClientProtocolException, IOException {

                //parsingData �޼����� ����� ������ ����
                String result = null;
                //�޾ƿ� �������� InputStream�� �����ϱ� ���� ��ü
                HttpEntity entity = arg0.getEntity();

                //parsingData�� ȣ���ϰ� ����� result�� ����
                result = parsingData(entity.getContent());    //

                System.out.println(result);

                //�Ϲ� �ڵ鷯���� �޽����� �����ϱ� ���ؼ� �޽��� ��ü ����
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                if (result.equals("1")) {
                    bundle.putString("RESULT", "��������");
                } else {
                    bundle.putString("RESULT", "��������");
                }
                message.setData(bundle);
                //�ڵ鷯���� �޽����� ������.
                handler.sendMessage(message);
                return result;
            }
        };
        //���̾�α� �����ؼ� ���

        //�����带 �����ؼ� ����
        th = new ThreadEx(3);
        th.start();
    }
}
