package kr.ac.yonsei.memeplex.view;

import kr.ac.yonsei.memeplex.TagInfo;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class TagView extends TextView {
    private static final int TAG_BACKGROUND_COLOR_NOT_SELECTED  = Color.WHITE;
    private static final int TAG_BACKGROUND_COLOR_AND           = Color.BLUE;
    private static final int TAG_BACKGROUND_COLOR_OR            = Color.GREEN;
    private static final int TAG_BACKGROUND_COLOR_NOT           = Color.RED;

    private TagInfo tagInfo;
    private TagViewListener listener;
    
    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public TagView(Context context) {
        super(context);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            listener.OnTagTouched(this);
        }
        
        return super.onTouchEvent(event);
    }
    
    public void setTagInfo(TagInfo tagInfo) {
        this.tagInfo = tagInfo;
        
        refreshView();
    }
    
    public TagInfo getTagInfo() {
        return tagInfo;
    }

    public void refreshView() {
        setText(tagInfo.getTag());
        setTextColor(getTagTextColor(tagInfo));
        setTextSize(getTagTextSize(tagInfo));
        setSingleLine(true);
        
        switch (tagInfo.getTagStatus()) {
        case TagInfo.TAG_STATUS_NOT_SELECTED:
            setBackgroundColor(TAG_BACKGROUND_COLOR_NOT_SELECTED);
            break;
        case TagInfo.TAG_STATUS_AND:
            setBackgroundColor(TAG_BACKGROUND_COLOR_AND);
            break;
        case TagInfo.TAG_STATUS_OR:
            setBackgroundColor(TAG_BACKGROUND_COLOR_OR);
            break;
        case TagInfo.TAG_STATUS_NOT:
            setBackgroundColor(TAG_BACKGROUND_COLOR_NOT);
            break;
        }
    }

    public int getTagTextColor(TagInfo tagInfo) {
        return Color.BLACK;
    }

    public float getTagTextSize(TagInfo tagInfo) {
        int scoreDay = tagInfo.getScoreDay();
        
        if (scoreDay > 400)
            return 30.0f;
        else if (scoreDay > 300)
            return 28.0f;
        else if (scoreDay > 200)
            return 26.0f;
        else if (scoreDay > 100)
            return 24.0f;
        else
            return 22.0f;
    }

    public TagView setTagInfo(String tag, int color, float size) {
        this.setText(tag);
        this.setTextColor(color);
        this.setTextSize(size);
        this.setSingleLine(true);
        
        return this;
    }

    public void setTagViewListner(TagViewListener listener) {
        this.listener = listener;
    }
}