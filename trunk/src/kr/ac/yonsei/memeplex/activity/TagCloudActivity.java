package kr.ac.yonsei.memeplex.activity;

import kr.ac.yonsei.memeplex.R;
import kr.ac.yonsei.memeplex.view.TagCloudLayout;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TagCloudActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagcloud);
    
        //TagCloudLayout l = new TagCloudLayout(this);
        
        TagCloudLayout l = (TagCloudLayout) findViewById(R.id.tagcloudlayout);
        
        TagView t = new TagView(this);
        l.addTagView(t.setTagInfo("연세대", Color.YELLOW, 30));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("등록금", Color.WHITE, 23));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("군대", Color.WHITE, 23));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("예비군", Color.WHITE, 25));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("축구", Color.WHITE, 25));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("고시원", Color.WHITE, 18));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("미팅", Color.GREEN, 29));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("소주", Color.WHITE, 20));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("심심", Color.WHITE, 24));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("여자", Color.RED, 32));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("외로움", Color.WHITE, 18));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("숙제", Color.WHITE, 22));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("학사경고", Color.WHITE, 24));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("지진", Color.WHITE, 24));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("소녀시대", Color.RED, 32));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("카이스트", Color.WHITE, 24));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("아이유", Color.RED, 33));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("인증", Color.GREEN, 30));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("일본", Color.WHITE, 24));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("박지성", Color.WHITE, 24));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("데이트", Color.YELLOW, 28));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("맛집", Color.WHITE, 24));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("방사능", Color.GREEN, 29));
        
        t = new TagView(this);
        l.addTagView(t.setTagInfo("빅뱅", Color.WHITE, 22));
    }

    public class TagView extends TextView {
        public TagView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        
        public TagView(Context context) {
            super(context);
        }

        public TagView setTagInfo(String tag, int color, float size) {
            this.setText(tag);
            this.setTextColor(color);
            this.setTextSize(size);
            this.setSingleLine(true);
            
            return this;
        }
    }
}
