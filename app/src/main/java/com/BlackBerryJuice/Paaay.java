package com.BlackBerryJuice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.BlackBerryJuice.util.ErrorToast;

public class Paaay extends Activity {

    Double totalprice;
    int inttotalprice;
    private WebView w;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.price));
        }
        setContentView(R.layout.activity_pay);
        Bundle b = getIntent().getExtras();
        totalprice = b.getDouble("price");



        inttotalprice = totalprice.intValue();

        w = (WebView) findViewById(R.id.web);

        w.getSettings().setJavaScriptEnabled(true);

        w.getSettings().setDomStorageEnabled(true);

        w.addJavascriptInterface(new js(), "cc");

        w.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //super.onPageFinished(view, url);
                //w.loadUrl("javascript:window.cc.show(document.getElementsByTagName('html')[0].innerHTML);");
                w.loadUrl("javascript:window.cc.show('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }

            @Override
            public void onReceivedSslError(WebView view,SslErrorHandler handler, SslError error) {
                handler.proceed();

            }
        });

        w.loadUrl(Constant.PayURL +inttotalprice +"0");


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
        if(SharedData.load_reservarion_time(Paaay.this).equals("")) {
            i = new Intent(Paaay.this, Review.class);
            i.putExtra("price", totalprice);
        }
        else
            i= new Intent(Paaay.this,ActivityCart.class);

        startActivity(i);
        overridePendingTransition(R.anim.open_main, R.anim.close_next);
        finish();
    }

    class js{

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void show(String content){

            String c=Html.fromHtml(content).toString();

            Log.e("content in js" , c);

            String[] t=c.split("-");

            if(t[0].equals("ok")){

                ErrorToast.makeToast(Paaay.this, "تراکنش با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                Intent go = new Intent(Paaay.this,User_Buy_Record_Fake.class);
                go.putExtra("rahgir",t[1]);
                go.putExtra("price",inttotalprice);
                startActivity(go);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                finish();

            }else if(t[0].equals("er")){

                ErrorToast.makeToast(Paaay.this, "تراکنش ناموفق بود", Toast.LENGTH_LONG).show();
                Intent go = new Intent(Paaay.this,User_Buy_Record_Fake.class);
                go.putExtra("rahgir","null");
                go.putExtra("price",0);
                startActivity(go);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                finish();
            }
        }

    }//TEST3



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}


