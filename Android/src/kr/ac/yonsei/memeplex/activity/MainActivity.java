package kr.ac.yonsei.memeplex.activity;

import kr.ac.yonsei.memeplex.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 이미지의 화질저하를 막기 위해
        getWindow().setFormat(PixelFormat.RGBA_8888);
        
        setContentView(R.layout.logo);
    
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
    
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg)
        {
            startActivity(new Intent(getApplicationContext(),
                    TagCloudActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            
            overridePendingTransition(R.anim.fade, R.anim.hold);
            
            finish();
        }
    };
}