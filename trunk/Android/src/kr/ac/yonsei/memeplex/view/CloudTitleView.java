package kr.ac.yonsei.memeplex.view;

import kr.ac.yonsei.memeplex.TagInfo;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CloudTitleView extends TextView {
    private String title;
    
    public CloudTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public CloudTitleView(Context context) {
        super(context);
    }
    
    public void setTitle(String title) {
        setText(title);
        setTextSize(33.0f);
        setTextColor(Color.BLUE);
    }
}