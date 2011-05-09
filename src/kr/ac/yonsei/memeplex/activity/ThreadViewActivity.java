package kr.ac.yonsei.memeplex.activity;

import java.util.ArrayList;
import java.util.List;

import kr.ac.yonsei.memeplex.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThreadViewActivity extends ListActivity {
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
        
        /*
         * 시연용 코드
         */
        
        getIntent().putExtra("author", "암컷");
        getIntent().putExtra("message", "나 뻥 안치고, 진짜 여자라니까.. 사진이랑 목소리로 확실히 인증한다!!");
        getIntent().putExtra("passed", "20분 전");
        getIntent().putExtra("tags", "여자 학교 숙제 연애");
        getIntent().putExtra("thread_tags", "부산, 해운대구, 중동, 여자, 인증");
        getIntent().putExtra("msgid", "12");
        
        list.add(new ThreadReply("의처증", "헐 진짜네... 덜덜덜", "2분 전"));
        list.add(new ThreadReply("피리", "이쁘네 러블리~~ 저 번호 좀 주세요 혼내줄랑게", "1분 전"));
        
        adapter.notifyDataSetChanged();
    }

    private class ThreadReply {
        private String author;
        private String message;
        private String passed;

        public ThreadReply(String author, String message, String passed) {
            this.author = author;
            this.message = message;
            this.passed = passed;
        }
        
        public String getAuthor() {
            return author;
        }

        public String getMessage() {
            return message;
        }
        
        public String getTimePassedString() {
            return passed;
        }
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
                            + gba.getTimePassedString());
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

                    tvmessage.setText(getIntent().getStringExtra("message"));

                    String writeInfo = getIntent().getStringExtra("author");

                    String passed = getIntent().getStringExtra("passed");
                    if (passed.length() > 0) {
                        writeInfo += ", " + passed;
                    }
                    
                    writeInfo += "\n" + getIntent().getStringExtra("thread_tags");

                    tvwriteinfo.setText(writeInfo);

                    String imageId = getIntent().getStringExtra("imageId");
                }
            }

            return v;
        }
    }

    private String getTimePassedString(int seconds) {
        if (seconds >= 60 * 60 * 24 * 30)
            return seconds / (60 * 60 * 24 * 30) + "개월 전";
        else if (seconds >= 60 * 60 * 24)
            return seconds / (60 * 60 * 24) + "일 전";
        else if (seconds >= 60 * 60)
            return seconds / (60 * 60) + "시간 전";
        else if (seconds >= 60)
            return seconds / 60 + "분 전";
        return "방금 전";
    }
}
