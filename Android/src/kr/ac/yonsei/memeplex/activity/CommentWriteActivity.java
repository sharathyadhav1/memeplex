package kr.ac.yonsei.memeplex.activity;

import java.net.URLEncoder;

import kr.ac.yonsei.memeplex.Memeplex;
import kr.ac.yonsei.memeplex.R;
import kr.ac.yonsei.memeplex.api.DataLoaderListener;
import kr.ac.yonsei.memeplex.api.DataLoaderTask;

import org.w3c.dom.Document;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommentWriteActivity extends Activity implements DataLoaderListener {
    private int srl;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_write);
        
        srl = getIntent().getIntExtra("docSrl", 0);
        
        final EditText editNickname = (EditText) findViewById(R.id.EditTextNickname);
        final EditText editComment = (EditText) findViewById(R.id.EditTextComment);
        Button btnWrite = (Button) findViewById(R.id.ButtonWriteComment);
        
        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nickname = editNickname.getText().toString();
                String comment = editComment.getText().toString();
                
                if (nickname.length() ==0 || comment.length() == 0) {
                    Toast.makeText(CommentWriteActivity.this, "모든 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                writeComment(nickname, comment);
            }
        });
    }

    private void writeComment(String nickname, String comment) {
        Memeplex memeplex = (Memeplex) getApplication();
        
        String apiUrl = "http://memeplex.ohmyenglish.co.kr/comment_save.php?document_srl=" + srl;
        apiUrl += "&nick_name=" + URLEncoder.encode(nickname);
        apiUrl += "&content=" + URLEncoder.encode(comment);
        apiUrl += "&latitude=" + memeplex.getLatitude();
        apiUrl += "&longitude=" + memeplex.getLongitude();
        apiUrl += "&device_id=0";

        DataLoaderTask task = new DataLoaderTask(this, this);
        task.execute(apiUrl);
    }

    public void onDataLoadingFinish(Document doc, int id) {
        Toast.makeText(CommentWriteActivity.this, "글쓰기가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onDataLoadingCancel() {
        finish();
    }
}
