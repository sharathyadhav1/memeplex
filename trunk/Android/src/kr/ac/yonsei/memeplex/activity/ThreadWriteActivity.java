package kr.ac.yonsei.memeplex.activity;

import java.io.File;

import kr.ac.yonsei.memeplex.R;
import kr.ac.yonsei.memeplex.api.ThreadWriteListener;
import kr.ac.yonsei.memeplex.api.ThreadWriteTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadWriteActivity extends Activity implements ThreadWriteListener {
    private static final int REQUEST_CODE_ATTACH_PICTURE = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    
    private String pictureFilePath = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_write);
        
        final EditText editNickname = (EditText) findViewById(R.id.EditTextNickname);
        final EditText editContent = (EditText) findViewById(R.id.EditTextContent);
        final EditText editTags = (EditText) findViewById(R.id.EditTextTags);
        final TextView editTouchToAttach = (TextView) findViewById(R.id.EditTextTouchToAttach);
        ImageView imageviewPic = (ImageView) findViewById(R.id.ImageViewPicture);
        Button btnWrite = (Button) findViewById(R.id.ButtonWriteThread);
        
        editTags.setText(getIntent().getStringExtra("tags"));
        editTouchToAttach.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showOptionToSelectPicture();
            }
        });
        
        imageviewPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showOptionToSelectPicture();
            }
        });
        
        
        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nickname = editNickname.getText().toString();
                String comment = editContent.getText().toString();
                String tags = editTags.getText().toString();
                
                if (nickname.length() == 0 || comment.length() == 0 || tags.length() == 0) {
                    Toast.makeText(ThreadWriteActivity.this, "모든 내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                writeContent(nickname, comment, tags, pictureFilePath);
            }
        });
    }

    private void choicePicture() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, REQUEST_CODE_ATTACH_PICTURE);        
    }
    
    private void takePictureUsingCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        
        String cameraPath = Environment.getExternalStorageDirectory() + "/data/picture_from_camera.png";
        Uri imageUri = Uri.fromFile(new File(cameraPath));
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_CODE_ATTACH_PICTURE && resultCode == RESULT_OK) {
            // 사진을 선택했으면
            Uri uri = data.getData();
            
            if (uri != null) {
                Cursor cursor = managedQuery(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                cursor.moveToFirst();
                pictureFilePath = cursor.getString(0);
                cursor.close();
                
                showPicture(pictureFilePath);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            // 카메라로 사진 찍었으면
            pictureFilePath = Environment.getExternalStorageDirectory() + "/data/picture_from_camera.png";
            showPicture(pictureFilePath);
        }
    }
    
    private void showOptionToSelectPicture() {
        final String items[] = { "카메라로 찍기", "사진 선택하기" };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("사진 첨부 방법");
        
        ab.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (whichButton == 0) {
                    takePictureUsingCamera();
                } else if (whichButton == 1) {
                    choicePicture();                    
                }
            }
        });
        
        ab.show();
    }

    private void showPicture(String path) {
        ImageView imageviewPic = (ImageView) findViewById(R.id.ImageViewPicture);
        TextView editTouchToAttach = (TextView) findViewById(R.id.EditTextTouchToAttach);
        
        Bitmap pic = BitmapFactory.decodeFile(path);
        imageviewPic.setImageBitmap(pic);
        
        editTouchToAttach.setVisibility(View.GONE);        
    }
    
    private void writeContent(String nickname, String content, String tags, String picPath) {
        ThreadWriteTask task = new ThreadWriteTask(this, this);
        task.setThreadInfo(nickname, content, tags, "0", "0", "0");
        task.execute(pictureFilePath);
    }

    public void onSendFinish() {
        Toast.makeText(ThreadWriteActivity.this, "글쓰기가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        finish();        
    }

    public void onSendFail() {
        Toast.makeText(ThreadWriteActivity.this, "글쓰기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
    }

    public void onSendCancel() {
        Toast.makeText(ThreadWriteActivity.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();        
    }
}
