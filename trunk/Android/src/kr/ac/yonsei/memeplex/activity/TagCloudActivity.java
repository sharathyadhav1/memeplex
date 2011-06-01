package kr.ac.yonsei.memeplex.activity;

import java.util.ArrayList;

import kr.ac.yonsei.memeplex.Memeplex;
import kr.ac.yonsei.memeplex.R;
import kr.ac.yonsei.memeplex.TagInfo;
import kr.ac.yonsei.memeplex.api.DataLoaderListener;
import kr.ac.yonsei.memeplex.api.DataLoaderTask;
import kr.ac.yonsei.memeplex.view.CloudTitleView;
import kr.ac.yonsei.memeplex.view.TagCloudLayout;
import kr.ac.yonsei.memeplex.view.TagView;
import kr.ac.yonsei.memeplex.view.TagViewListener;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TagCloudActivity extends Activity implements DataLoaderListener, TagViewListener {
    private static final int SELECTION_MODE_NOT_DECIDED = 0;
    private static final int SELECTION_MODE_AND         = 1;
    private static final int SELECTION_MODE_OR          = 2;
    
    private static final int MENU_SET_EXTERNAL_CLOUD    = 0;
    private static final int MENU_SET_NEW_THREAD        = 1;

    private TagCloudLayout tagCloudLayout;
    private ArrayList<TagInfo> tagList;
    private int selectionMode = SELECTION_MODE_AND;
    
    private ProgressDialog progressGetLocation;
    
    private LocationManager mManager;
    private LocationListener mListener;
    private Location mLocation;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagcloud);
    
        tagList = new ArrayList<TagInfo>();

        tagCloudLayout = (TagCloudLayout) findViewById(R.id.tagcloudlayout);
        
        final Button btnDefaultTagCloud = (Button) findViewById(R.id.ButtonDefaultTagCloud);
        final Button btnExternalTagCloud = (Button) findViewById(R.id.ButtonExternalTagCloud);
        Button btnGetThread = (Button) findViewById(R.id.ButtonGetThreadList);
        
        btnDefaultTagCloud.setSelected(true);
        btnDefaultTagCloud.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnDefaultTagCloud.setSelected(true);
                btnExternalTagCloud.setSelected(false);
                
                getDefaultTagCloud();
            }
        });
        
        btnExternalTagCloud.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnDefaultTagCloud.setSelected(false);
                btnExternalTagCloud.setSelected(true);
                
                getExternalTagCloud();
            }
        });
        
        btnGetThread.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<TagInfo> tags = new ArrayList<TagInfo>();
                
                for (TagInfo tagInfo : tagList) {
                    if (tagInfo.getTagStatus() != TagInfo.TAG_STATUS_NOT_SELECTED) {
                        tags.add(tagInfo);
                    }
                }
                
                if (tags.size() == 0) {
                    Toast.makeText(TagCloudActivity.this, "태그를 선택해주십시요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showThreadListWithTag(tags);
            }
        });
        
        
        mManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mListener = new MyLocationListener();
        
        mLocation = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        if (mLocation == null) {
            // 위치 정보 없으면 가져올 때까지 대기
            progressGetLocation = ProgressDialog.show(this, null, "위치 정보를 읽어오는 중입니다.", true, true);
            mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener);
        } else {
            Memeplex memeplex = (Memeplex) getApplication();
            memeplex.setLocation(mLocation);
            
            // 위치 정보 있으면 바로 태그 클라우드 불러오기
            getDefaultTagCloud();
        }
    }

    // 내부 클래스로 구현한 LocationListener
    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {
            if (mLocation == null) {
                getDefaultTagCloud();
            }
            
            mLocation = loc;
            
            Memeplex memeplex = (Memeplex) getApplication();
            memeplex.setLocation(mLocation);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void getDefaultTagCloud() {
        String tagUrl = "http://memeplex.ohmyenglish.co.kr/tag_list.php";
        
        showTagCloud(tagUrl);
    }
    
    private void getExternalTagCloud() {
        if (hasExternalCloudUrl()) {
            showTagCloud(getExternalCloudUrl());
        } else {
            Toast.makeText(TagCloudActivity.this,
                    "외부 태그 클라우드가 설정되지 않았습니다. 메뉴키를 눌러 외부 태그 클라우드 주소를 설정해주세요."
                    , Toast.LENGTH_LONG).show();
        }
    }

    private void showTagCloud(String tagUrl) {
        tagList.clear();
        tagCloudLayout.removeAllViews();
        
        if (mLocation != null) {
            tagUrl += "?latitude=" + mLocation.getLatitude();
            tagUrl += "&longitude=" + mLocation.getLongitude();
            
            Toast.makeText(TagCloudActivity.this, "현재 위치 정보를 기반으로 태그 클라우드를 불러옵니다.",
                    Toast.LENGTH_SHORT).show();
        }
        
        DataLoaderTask task = new DataLoaderTask(this, this);
        task.execute(tagUrl);
    }

    private boolean hasExternalCloudUrl() {
        String url = getExternalCloudUrl();
        
        return (url != null && url.length() > 0);
    }
    
    private String getExternalCloudUrl() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString("external_cloud_url", null);
    }
    
    private void showThreadListWithTag(ArrayList<TagInfo> selectedTags) {
        Intent intent = new Intent(this, ThreadListActivity.class);
        intent.putExtra("tags", selectedTags);
        startActivity(intent);        
    }

    public void onDataLoadingFinish(Document doc, int id) {
        if (doc == null) {
            Toast t = Toast.makeText(this, "데이터를 가져올 수 없습니다.", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        
        int srl = 0, score = 0;
        
        NodeList clouds = doc.getElementsByTagName("CLOUD");
        for (int i = 0; i < clouds.getLength(); ++i) {
            // 클라우드 타이틀 추가
            NamedNodeMap attrs = clouds.item(i).getAttributes();
            addCloudTitle(attrs.getNamedItem("name").getNodeValue());
            
            NodeList tags = clouds.item(i).getChildNodes();

            for (int j = 0; j < tags.getLength(); ++j) {
                if (tags.item(j).hasAttributes()) {
                    NamedNodeMap subattrs = tags.item(j).getAttributes();
    
                    String name = subattrs.getNamedItem("name").getNodeValue();
    
                    if (subattrs.getNamedItem("tag_srl") != null)
                        srl = Integer.parseInt(subattrs.getNamedItem("tag_srl").getNodeValue());
                    if (subattrs.getNamedItem("score_day") != null)
                        score = Integer.parseInt(subattrs.getNamedItem("score_day").getNodeValue());
    
                    addTagToCloud(new TagInfo(name, srl, score));
                }
            }
        }
    }

    private void addTagToCloud(TagInfo tagInfo) {
        TagView t = new TagView(this);
        t.setTagInfo(tagInfo);
        t.setTagViewListner(this);
        tagCloudLayout.addTagView(t);
        
        tagList.add(tagInfo);
    }
    
    private void addCloudTitle(String title) {
        CloudTitleView titleView = new CloudTitleView(this);
        titleView.setTitle(title);
        tagCloudLayout.addTagView(titleView);
    }

    public void onDataLoadingCancel() {
    }

    public void OnTagTouched(TagView tagView) {
        TagInfo touchedTagInfo = tagView.getTagInfo();
        int selectedCount = 0;
        int status = TagInfo.TAG_STATUS_AND;
        
        for (TagInfo ti : tagList) {
            if (ti != touchedTagInfo && ti.isSelected()) {
                selectedCount++;
                status = ti.getTagStatus();
            }
        }
        
        if (selectedCount == 0) {
            touchedTagInfo.toggleSelect();
        } else if (touchedTagInfo.isSelected()) {
            touchedTagInfo.setTagStatus(TagInfo.TAG_STATUS_NOT);
        } else if (touchedTagInfo.getTagStatus() == TagInfo.TAG_STATUS_NOT) {
            touchedTagInfo.setTagStatus(TagInfo.TAG_STATUS_NOT_SELECTED);
        } else {
            touchedTagInfo.setTagStatus(status);
        }
        
        tagView.refreshView();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SET_EXTERNAL_CLOUD, 0, "외부 태그 클라우드 설정");
        menu.add(1, MENU_SET_NEW_THREAD, 1, "새 태그에 글쓰기");
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == MENU_SET_EXTERNAL_CLOUD) {
            showSetExternalCloudUrlDialog();
        } else if (item.getItemId() == MENU_SET_NEW_THREAD) {
            Intent intent = new Intent(this, ThreadWriteActivity.class);
            startActivity(intent);
        }
        
        return super.onMenuItemSelected(featureId, item);
    }

    private void showSetExternalCloudUrlDialog() {
        AlertDialog.Builder builder;

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_input_external_cloud,
                                       (ViewGroup)findViewById(R.id.layout_root));

        builder = new AlertDialog.Builder(this);
        builder.setTitle("외부 클라우드 URL");
        
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText editUrl = (EditText) layout.findViewById(R.id.EditTextExternalCloudUrl);
                saveExternalCloudUrl(editUrl.getText().toString());
                
                getDefaultTagCloud();
            }
        });
        
        builder.setNegativeButton("취소", null);
        
        builder.setView(layout);
        builder.show();
    }

    private void saveExternalCloudUrl(String url) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
                
        editor.putString("external_cloud_url", url);

        editor.commit();
    }
}
