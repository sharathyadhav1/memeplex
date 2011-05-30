package kr.ac.yonsei.memeplex.view;

import kr.ac.yonsei.memeplex.TagInfo;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class TagView extends TextView {
    private static final int SELECTED_TAG_BACKGROUND_COLOR = Color.MAGENTA;

    private TagInfo tagInfo;
    
    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public TagView(Context context) {
        super(context);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            tagInfo.toggleSelected();
            refreshView();
        }
        
        return super.onTouchEvent(event);
    }
    
    public void setTagInfo(TagInfo tagInfo) {
        this.tagInfo = tagInfo;
        
        refreshView();
    }

    private void refreshView() {
        setText(tagInfo.getTag());
        setTextColor(tagInfo.getTagTextColor());
        setTextSize(tagInfo.getTagTextSize());
        setSingleLine(true);
        
        if (tagInfo.isSelected()) {
            setBackgroundColor(SELECTED_TAG_BACKGROUND_COLOR);
        } else {
            setBackgroundColor(tagInfo.getTagBackgroundColor());
        }
    }

    public TagView setTagInfo(String tag, int color, float size) {
        this.setText(tag);
        this.setTextColor(color);
        this.setTextSize(size);
        this.setSingleLine(true);
        
        return this;
    }
}