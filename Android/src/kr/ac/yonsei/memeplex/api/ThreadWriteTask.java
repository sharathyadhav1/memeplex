package kr.ac.yonsei.memeplex.api;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ThreadWriteTask extends AsyncTask<String, Void, Integer> implements OnCancelListener {
    private static final int RETURN_CODE_FAIL = 0;

    private static final int PICTURE_WIDTH_TO_RESIZE = 480;
    
    private static final String MEMEPLEX_URL_WRITE_THREAD
                            = "http://memeplex.ohmyenglish.co.kr/thread_save.php";
    
    private Context context;
    private ProgressDialog progressDialog;
    private ThreadWriteListener listener;

    String nickname, content, tagSrlList;
    String latitude, longitude, deviceId;
    
    public ThreadWriteTask(Context context, ThreadWriteListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setThreadInfo(String nickname, String content, String tagSrlList,
            String latitude, String longitude, String deviceId) {
        this.nickname = nickname;
        this.content = content;
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tagSrlList = tagSrlList;
    }
    
    @Override
    public void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, "스레드를 등록 중입니다.", true, true, this);
    }
    
    @Override
    protected Integer doInBackground(String... params) {
        String picFilePath = params[0];
        
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            HttpPost httpost = new HttpPost(MEMEPLEX_URL_WRITE_THREAD);
            MultipartEntity entity = new MultipartEntity();

            // 이미지 첨부
            if (picFilePath != null) {
                // 이미지 읽어옴
                Bitmap originalImage = BitmapFactory.decodeFile(picFilePath);

                int img_width = originalImage.getWidth(); // 변환할 이미지의 가로
                int img_height = originalImage.getHeight();  // 변환할 이미지의 세로
                 
                float di = (float)img_width / (float)PICTURE_WIDTH_TO_RESIZE; // 배수 계산
                int height = (int)((float)img_height / di); // 새로운 이미지의 세로 계산
                 
                // 이미지 리사이즈
                Bitmap resizedImage = Bitmap.createScaledBitmap(originalImage, PICTURE_WIDTH_TO_RESIZE, height, true);
                String resizedImagePath = Environment.getExternalStorageDirectory() + "/data/place_picture.png";
                
                // 비트맵저장
                FileOutputStream fos = new FileOutputStream(resizedImagePath);
                resizedImage.compress(CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                 
                File f = new File(resizedImagePath);
                entity.addPart("picture", new FileBody(f));
            }
            
            // 기타 정보 추가
            entity.addPart("nick_name", new StringBody(nickname));
            entity.addPart("content", new StringBody(content));
            entity.addPart("tag_srl_list", new StringBody(tagSrlList));
            entity.addPart("latitude", new StringBody(latitude));
            entity.addPart("longitude", new StringBody(longitude));
            entity.addPart("device_id", new StringBody(deviceId));

            httpost.setEntity(entity);

            HttpResponse response;
                            
            response = httpclient.execute(httpost);

            Log.d("fu", "response : " + response.getStatusLine());

            if (entity != null) {
                entity.consumeContent();
            }
            
            return response.getStatusLine().getStatusCode();
        } catch (Exception ex) {
            Log.d("fu", "Upload failed : " + ex.getMessage() + ", " + ex.toString());
            return RETURN_CODE_FAIL;
        }
    }

    protected void onPostExecute(Integer retCode) {
        if (isCancelled() == true) {
            listener.onSendCancel();
            return;
        }
        
        if (retCode == 200) {
            listener.onSendFinish();
        } else {
            listener.onSendFail();
        }

        progressDialog.dismiss();
    }
    
    @Override
    public void onCancelled() {
        listener.onSendCancel();
        progressDialog.dismiss();
    }
    
    public void onCancel(DialogInterface dialoginterface) {
        cancel(true);
    }
}
