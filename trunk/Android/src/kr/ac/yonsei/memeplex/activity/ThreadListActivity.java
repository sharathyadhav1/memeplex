package kr.ac.yonsei.memeplex.activity;

import java.util.ArrayList;
import java.util.List;

import kr.ac.yonsei.memeplex.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ThreadListActivity extends ListActivity {
    private ArrayList<ThreadArticle> list;
    private CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_list);
        
        list = new ArrayList<ThreadArticle>();
        
        adapter = new CustomAdapter(this, R.layout.thread_list_row, list);
        setListAdapter(adapter);
        
        Button btnBackToTagList = (Button) findViewById(R.id.ButtonBackToTagList);
        btnBackToTagList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        
        Button btnWriteOnThisTag = (Button) findViewById(R.id.ButtonWriteOnThisTag);
        btnWriteOnThisTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                write();
            }
        });
        
        /* 
         * 시연용 코드
         */
        
        list.add(new ThreadArticle("베이비", "저도 노래를 불러보았습니다", "", "12분 전", "", "2", "노래",
                false, true, false));
        list.add(new ThreadArticle("자웅동체", "나는 숙제한다. 고로 존재한다.", "", "13분 전", "", "0", "숙제 과제",
                false, false, false));
        list.add(new ThreadArticle("연세인", "저도 A+를 받아보고 싶습니다. 어떻게 해야하나요?", "", "18분 전", "", "0", "연세 성적",
                false, false, false));
        list.add(new ThreadArticle("고장닉", "이게 누구인지 아는 사람?", "", "20분 전", "", "1", "자유",
                true, false, false));
        list.add(new ThreadArticle("암컷", "나 여자임. 목소리랑 사진 인증! 형들아 관심 좀..", "", "25분 전", "", "8", "여자 인증",
                true, true, false));
        
        adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        Intent intent = new Intent(getApplicationContext(), ThreadViewActivity.class);
        
        intent.putExtra("author", list.get(position).getAuthor());
        intent.putExtra("message", list.get(position).getMessage());
        intent.putExtra("passed", list.get(position).getPassed());
        intent.putExtra("msgid", list.get(position).getMsgid());
        intent.putExtra("tags", list.get(position).getTags());
        
        startActivity(intent);
    }
    
    private void write() {
        Intent intent = new Intent(this, ThreadWriteActivity.class);
        startActivity(intent);
    }

    public class ThreadArticle
    {
        private String author;
        private String message;
        private String date;
        private String passed;
        private String msgid;
        private String reply;
        private String tags;
        
        private boolean hasImage;
        private boolean hasVoiceRecord;
        private boolean hasPlaceInfo;
        
        public ThreadArticle(String author, String message, String date, String passed,
                String msgid, String reply, String tags, boolean hasImage, boolean hasVoiceRecord,
                boolean hasPlaceInfo)
        {
            this.author = author;
            this.message = message;
            this.date = date;
            this.passed = passed;
            this.msgid = msgid;
            this.reply = reply;
            this.tags = tags;
            
            this.hasImage = hasImage;
            this.hasVoiceRecord = hasVoiceRecord;
            this.hasPlaceInfo = hasPlaceInfo;
        }

        public String getMsgid() {
            return msgid;
        }

        public String getReply() {
            return reply;
        }

        public String getAuthor() {
            return author;
        }

        public String getMessage() {
            return message;
        }

        public String getDate() {
            return date;
        }
        
        public String getPassed() {
            return passed;
        }
        
        public String getTags() {
            return tags;
        }
        
        public boolean hasImage() {
            return hasImage;
        }
        
        public boolean hasVoiceRecord() {
            return hasVoiceRecord;
        }
        
        public boolean hasPlaceInfo() {
            return hasPlaceInfo;
        }
    }
    
    private class CustomAdapter extends ArrayAdapter<ThreadArticle>
    {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            
            if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                
                v = vi.inflate(R.layout.thread_list_row, null);
            }

            TextView tvmessage = (TextView)v.findViewById(R.id.TextViewMessage);
            TextView tvwriteinfo = (TextView)v.findViewById(R.id.TextViewWriteInfo);
            TextView tvreplies = (TextView)v.findViewById(R.id.TextViewReply);
            
            ThreadArticle threadArticle = list.get(position);
            
            if(threadArticle != null) {
                tvmessage.setText(threadArticle.getMessage());
                tvmessage.setTextColor(0xFFFFFFFF);
                
                String writeInfo = threadArticle.getAuthor();
                
                if (threadArticle.getPassed().length() > 0) {
                    writeInfo += ", " + threadArticle.getPassed();    // 지난 시간
                }
                
                tvwriteinfo.setText(writeInfo);
                
                if(Integer.parseInt(threadArticle.getReply()) > 0) {
                    tvreplies.setVisibility(View.VISIBLE);
                    tvreplies.setText("(" + threadArticle.getReply() + ")");
                }
                else {
                    tvreplies.setVisibility(View.INVISIBLE);
                }
            }
            
            ImageView imgHasImage = (ImageView)v.findViewById(R.id.ImageHasImage);
            ImageView imgHasVoiceRecord = (ImageView)v.findViewById(R.id.ImageHasVoiceRecord);
            
            if (threadArticle.hasImage()) {
                imgHasImage.setVisibility(View.VISIBLE);
            } else {
                imgHasImage.setVisibility(View.GONE);
            }

            if (threadArticle.hasVoiceRecord()) {
                imgHasVoiceRecord.setVisibility(View.VISIBLE);
            } else {
                imgHasVoiceRecord.setVisibility(View.GONE);
            }
            
            return v;
        }

        public CustomAdapter(Context context, int textViewResourceId,
                List<ThreadArticle> objects) {
            super(context, textViewResourceId, objects);
        }
    }
}
