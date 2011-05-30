package kr.ac.yonsei.memeplex.api;

public interface ThreadWriteListener {
    public void onSendFinish();
    public void onSendFail();
    public void onSendCancel();
}
