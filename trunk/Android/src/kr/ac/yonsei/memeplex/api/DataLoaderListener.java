package kr.ac.yonsei.memeplex.api;

import org.w3c.dom.Document;

public interface DataLoaderListener {
	public void onDataLoadingFinish(Document doc, int id);
	public void onDataLoadingCancel();
}
