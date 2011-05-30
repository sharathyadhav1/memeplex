package kr.ac.yonsei.memeplex;

public class ThreadReply {
    private String nickname;
    private String content;
    private int passed;

    public ThreadReply(String nickname, String content, int passed) {
        this.nickname = nickname;
        this.content = content;
        this.passed = passed;
    }
    
    public String getAuthor() {
        return nickname;
    }

    public String getMessage() {
        return content;
    }
    
    public int getPassed() {
        return passed;
    }
}