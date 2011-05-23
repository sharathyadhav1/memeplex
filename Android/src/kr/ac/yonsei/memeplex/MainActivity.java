package kr.ac.yonsei.memeplex;

import kr.ac.yonsei.memeplex.activity.TagCloudActivity;
import kr.ac.yonsei.memeplex.activity.ThreadListActivity;
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