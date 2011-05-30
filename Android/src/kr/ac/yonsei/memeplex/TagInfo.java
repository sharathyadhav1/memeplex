package kr.ac.yonsei.memeplex;

import java.io.Serializable;

import android.graphics.Color;

public class TagInfo implements Serializable {
    private static final long serialVersionUID = -7826734242526211013L;
    
    String tag;
    int srl;
    int scoreDay;
    
    boolean selected;
    
    public TagInfo(String tag, int srl, int scoreDay) {
        this.tag = tag;
        this.srl = srl;
        this.scoreDay = scoreDay;
    }
    
    public String getTag() {
        return tag;
    }
    
    public int getSrl() {
        return srl;
    }

    public int getScoreDay() {
        return scoreDay;
    }
    
    public int getTagBackgroundColor() {
        return Color.BLACK;
    }
    
    public int getTagTextColor() {
        if (scoreDay > 400)
            return Color.RED;
        else if (scoreDay > 300)
            return Color.YELLOW;
        else if (scoreDay > 200)
            return Color.BLUE;
        else if (scoreDay > 100)
            return Color.GREEN;
        else
            return Color.WHITE;
    }

    public float getTagTextSize() {
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

    public void toggleSelected() {
        selected = !selected;
    }
    
    public boolean isSelected() {
        return selected;
    }
}
