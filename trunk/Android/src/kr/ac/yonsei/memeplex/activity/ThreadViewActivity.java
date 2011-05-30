package kr.ac.yonsei.memeplex.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.ac.yonsei.memeplex.R;
import kr.ac.yonsei.memeplex.ThreadReply;
import kr.ac.yonsei.memeplex.api.DataLoaderListener;
import kr.ac.yonsei.memeplex.api.DataLoaderTask;
import kr.ac.yonsei.memeplex.util.TimeUtility;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadViewActivity extends ListActivity implements DataLoaderListener {
    private static final int REQUEST_WRITE_COMMENT = 0;
    
    private ArrayList<ThreadReply> list;
    private CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_list);
        
        // 리스트 및 어댑터 세팅
        list = new ArrayList<ThreadReply>();
        
        adapter = new CustomAdapter(this, R.layout.reply_list_row, list);
        setListAdapter(adapter);

        getIntent().putExtra("tags", "TODO:tags");

        // 스레드 첨부된 이미지가 있으면
        final String picturePath = getIntent().getStringExtra("picturePath");

        if (picturePath.length() > 0) {
            new Thread(new Runnable() {
                public void run() {
                    final Drawable d = loadImageFromUrl(picturePath);
                    
                    final ImageView imgAttatched = (ImageView) findViewById(R.id.ImageViewAttached);
                    Log.d("sharewhere", "!!" + imgAttatched);

                    imgAttatched.post(new Runnable() {
                        public void run() {
                            Log.d("sharewhere", "##");
                            
                            if (d != null) {
                                imgAttatched.setImageDrawable(d);
                            }
                        }
                    });
                }
            }).start();
        }
        
        Button btnWriteComment = (Button) findViewById(R.id.ButtonWriteComment);
        btnWriteComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ThreadViewActivity.this, CommentWriteActivity.class);
                intent.putExtra("docSrl", getIntent().getIntExtra("docSrl", 0));
                startActivityForResult(intent, REQUEST_WRITE_COMMENT);
            }
        });
        
        Button btnRefreshComment = (Button) findViewById(R.id.ButtonRefreshComment);
        btnRefreshComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshThreadComment();
            }
        });
        
        refreshThreadComment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE_COMMENT) {
            refreshThreadComment();
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshThreadComment() {
        int srl = getIntent().getIntExtra("docSrl", 0);
        String apiUrl = "http://memeplex.ohmyenglish.co.kr/comment_list.php?document_srl=" + srl;

        DataLoaderTask task = new DataLoaderTask(this, this);
        task.execute(apiUrl);
    }

    public static Drawable loadImageFromUrl(String url) {
        InputStream is;
        
        Log.d("sharewhere", "loadimage : " + url);
        
        try {
            is = (InputStream) (new URL(url)).getContent();

            Drawable drawable = Drawable.createFromStream(is, "src");
            
            Log.d("sharewhere", "i've got image!");
            
            return drawable;
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    private class CustomAdapter extends ArrayAdapter<ThreadReply>
    {
        // 원본글 View와 댓글 View구분을 위한 상수값
        private static final int VIEW_ID_SOURCE = 1;
        private static final int VIEW_ID_REPLY = 2;
        
        private LayoutInflater vi;
        
        public CustomAdapter(Context context, int textViewResourceId,
                List<ThreadReply> objects) {
            super(context, textViewResourceId, objects);
            vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        @Override
        public int getCount() {
            return super.getCount() + 1;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            
            if (position > 0) { // 댓글
                if (v == null || v.getId() != VIEW_ID_REPLY) {
                    v = vi.inflate(R.layout.reply_list_row, null);
                    v.setId(VIEW_ID_REPLY);
                }
                
                TextView tvmessage = (TextView)v.findViewById(R.id.TextViewItem);
                TextView tvreplyno = (TextView)v.findViewById(R.id.TextViewReplyNum);
                
                ThreadReply gba = list.get(position - 1);
                    
                if(gba != null)
                {
                    tvreplyno.setVisibility(View.VISIBLE);
                    tvmessage.setGravity(Gravity.LEFT);
                    tvmessage.setText(gba.getMessage() + "\n" + gba.getAuthor() + ", "
                            + TimeUtility.getTimePassedString(gba.getPassed()));
                    tvreplyno.setText(String.valueOf(position) + ")");
                }
            }
            else {  // 원본글, 작성자 등
                if (v == null || v.getId() != VIEW_ID_SOURCE) {
                    v = vi.inflate(R.layout.reply_list_source, null);
                    v.setId(VIEW_ID_SOURCE);

                    // 본문 정보 뷰에 채우기
                    TextView tvmessage = (TextView) v
                            .findViewById(R.id.TextViewMessage);
                    TextView tvwriteinfo = (TextView) v
                            .findViewById(R.id.TextViewWriteInfo);

                    tvmessage.setText(getIntent().getStringExtra("content"));

                    String writeInfo = getIntent().getStringExtra("nickname");

                    int passed = getIntent().getIntExtra("passed", 0);
                    if (passed > 0) {
                        writeInfo += ", " + TimeUtility.getTimePassedString(passed);
                    }
                    
                    if (getIntent().getStringExtra("thread_tags") != null) {
                        writeInfo += "\n" + getIntent().getStringExtra("thread_tags");
                    }

                    tvwriteinfo.setText(writeInfo);
                }
            }

            return v;
        }
    }

    public void onDataLoadingFinish(Document doc, int id) {
        if (doc == null) {
            Toast t = Toast.makeText(this, "데이터를 가져올 수 없습니다.", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        
        adapter.clear();
        
        NodeList tags = doc.getElementsByTagName("THREAD");
        for (int i = 0; i < tags.getLength(); ++i) {
            NamedNodeMap attrs = tags.item(i).getAttributes();
            
            String nickname = attrs.getNamedItem("nick_name").getNodeValue();
            String content = attrs.getNamedItem("content").getNodeValue();
            int passed = Integer.parseInt(attrs.getNamedItem("timestamp").getNodeValue());

            list.add(new ThreadReply(nickname, content, passed));
        }
        
        adapter.notifyDataSetChanged();
    }

    public void onDataLoadingCancel() {
        // TODO Auto-generated method stub
        
    }
}
