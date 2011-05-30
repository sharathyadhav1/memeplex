package kr.ac.yonsei.memeplex.activity;

import kr.ac.yonsei.memeplex.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // 테스트용 Activity 띄우기
        Intent intent = new Intent(this, TagCloudActivity.class);
        startActivity(intent);
    }
}