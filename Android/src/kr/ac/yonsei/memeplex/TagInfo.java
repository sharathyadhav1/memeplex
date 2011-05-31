package kr.ac.yonsei.memeplex;

import java.io.Serializable;

public class TagInfo implements Serializable {
    private static final long serialVersionUID = -7826734242526211013L;
    
    public static final int TAG_STATUS_NOT_SELECTED     = 0;
    public static final int TAG_STATUS_AND              = 1;
    public static final int TAG_STATUS_OR               = 2;
    public static final int TAG_STATUS_NOT              = 3;
    
    String tag;
    int srl;
    int scoreDay;
    
    int status;
    
    public TagInfo(String tag, int srl, int scoreDay) {
        this.tag = tag;
        this.srl = srl;
        this.scoreDay = scoreDay;
        
        this.status = TAG_STATUS_NOT_SELECTED;
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
    
    public int getTagStatus() {
        return status;
    }

    public boolean isSelected() {
        return (status == TAG_STATUS_AND || status == TAG_STATUS_OR);
    }
    
    public void toggleSelect() {
        if (status == TAG_STATUS_NOT_SELECTED)
            status = TAG_STATUS_AND;
        else if (status == TAG_STATUS_AND)
            status = TAG_STATUS_OR;
        else if (status == TAG_STATUS_OR)
            status = TAG_STATUS_NOT;
        else if (status == TAG_STATUS_NOT)
            status = TAG_STATUS_NOT_SELECTED;
    }

    public void setTagStatus(int status) {
        this.status = status;        
    }
}
