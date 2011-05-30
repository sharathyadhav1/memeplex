package kr.ac.yonsei.memeplex;

public class ThreadArticle
{
    private String nickname;
    private String content;
    private String picturePath;

    private int reply;
    private int docSrl;
    private int passed;
    
    public ThreadArticle(String nickname, String content, int docSrl, int reply,
            int passed, String picturePath) {
        this.nickname = nickname;
        this.content = content;
        this.docSrl = docSrl;
        this.reply = reply;
        this.passed = passed;
        this.picturePath = picturePath;
    }

    public String getNickname() {
        return nickname;
    }

    public String getContent() {
        return content;
    }
    
    public int getReply() {
        return reply;
    }
    
    public int getDocSrl() {
        return docSrl;
    }

    public int getPassed() {
        return passed;
    }
    
    public boolean hasImage() {
        return (picturePath.length() > 0);
    }

    public String getPicturePath() {
        return picturePath;
    }
}