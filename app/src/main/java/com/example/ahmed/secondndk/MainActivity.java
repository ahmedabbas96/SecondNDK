package com.example.ahmed.secondndk;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static String PACKAGE_NAME;
    private OkHttpClient client;

    /** WebSocketListner to connect to echo server*/
    private final class EchoWebSocketListner extends WebSocketListener{

        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send(PACKAGE_NAME);
            webSocket.close(NORMAL_CLOSURE_STATUS,"");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("WebSocket "+ text);

        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS,null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error: "+t.getMessage());

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // socket button
        PACKAGE_NAME = getApplication().getPackageName();
        client = new OkHttpClient();
        Button SocketButton = findViewById(R.id.socket_button);
        SocketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        // dynamic button
        Button DynamicButton = findViewById(R.id.dynamic_button);
        DynamicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String EncryptionKey = "";
                String CipherText = "";

                //Run the service from the first app
                Intent intent = new Intent();
                intent.setAction("StartGetProbService");
                startService(intent);

                //get data SharedPrefernces
                try {
                    Context context = createPackageContext("com.example.ahmed.firstappgetprop",0);
                    @SuppressLint("WrongConstant")
                    SharedPreferences preferences = context.getSharedPreferences("CipherPreference",Context.CONTEXT_IGNORE_SECURITY);
                    EncryptionKey = preferences.getString("key","no key");
                    CipherText = preferences.getString("cipher","no cipher");

                    byte[] CipherByte = String.valueOf(CipherText).getBytes();
                    String DecryptDate = DecryptAES.decrypt(CipherByte,EncryptionKey);

                    Log.d("decrypted data", "onClick: "+DecryptDate);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    /**
     *  Start the connection between server and application
     */
    private void start(){

        Request request = new Request.Builder().url("ws://demos.kaazing.com/echo").build();
        EchoWebSocketListner listner = new EchoWebSocketListner();
        WebSocket webSocket = client.newWebSocket(request,listner);

        client.dispatcher().executorService().shutdown();
    }

    /**
     * output method to make toast of the output
     */

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, txt, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(/*String JString*/);
}
