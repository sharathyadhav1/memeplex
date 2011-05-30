package kr.ac.yonsei.memeplex.api;

import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class DataLoaderTask extends AsyncTask<String, Object, Document> implements OnCancelListener {
	private Context context;
	private ProgressDialog progressDialog;
	private DataLoaderListener listener;
	private int id;
	
	public DataLoaderTask(Context context, DataLoaderListener listener) {
		this(context, listener, -1);
	}
	
	public DataLoaderTask(Context context, DataLoaderListener listener, int id) {
		this.context = context;
		this.listener = listener;
		this.id = id;
	}
	
	@Override
	public void onPreExecute() {
		progressDialog = ProgressDialog.show(context, null, "데이터를 읽어오는 중입니다.", true, true, this);
	}
	
	@Override
	protected Document doInBackground(String ... params) {
		String uri = params[0];
		
		Log.d("sharewhere", "ShareWhere API : " + uri);
		
		PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        
        int VERSION = packageInfo.versionCode;
        
        if (uri.indexOf('?') == -1)
            uri += "?appver=" + VERSION;
        else
            uri += "&appver=" + VERSION;
        
        Log.d("shwhere", "DL: " + uri);
        
		Document doc = BasicDom.parseUri(uri, false);
		
		return doc;
	}
	
	@Override
	public void onPostExecute(Document result) {
		listener.onDataLoadingFinish(result, id);
		progressDialog.dismiss();
	}
	
	@Override
	public void onCancelled() {
		progressDialog.dismiss();
		listener.onDataLoadingCancel();
	}
	
	public void onCancel(DialogInterface dialoginterface) {
		cancel(true);
	}
}
