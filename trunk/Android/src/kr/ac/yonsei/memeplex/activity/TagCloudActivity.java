package kr.ac.yonsei.memeplex.activity;

import java.util.ArrayList;

import kr.ac.yonsei.memeplex.R;
import kr.ac.yonsei.memeplex.TagInfo;
import kr.ac.yonsei.memeplex.api.DataLoaderListener;
import kr.ac.yonsei.memeplex.api.DataLoaderTask;
import kr.ac.yonsei.memeplex.view.TagCloudLayout;
import kr.ac.yonsei.memeplex.view.TagView;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TagCloudActivity extends Activity implements DataLoaderListener {
    ArrayList<TagInfo> tagList;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagcloud);
    
        tagList = new ArrayList<TagInfo>();

        Button btnGetThread = (Button) findViewById(R.id.ButtonGetThreadList);
        btnGetThread.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<TagInfo> tags = new ArrayList<TagInfo>();
                
                for (TagInfo tagInfo : tagList) {
                    if (tagInfo.isSelected()) {
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
        
        getTagList();
    }

    private void getTagList() {
        DataLoaderTask task = new DataLoaderTask(this, this);
        task.execute("http://memeplex.ohmyenglish.co.kr/tag_list.php");        
    }

    private void showThreadListWithTag(ArrayList<TagInfo> tags) {
        Intent intent = new Intent(this, ThreadListActivity.class);
        intent.putExtra("tags", tags);
        startActivity(intent);        
    }

    public void onDataLoadingFinish(Document doc, int id) {
        if (doc == null) {
            Toast t = Toast.makeText(this, "데이터를 가져올 수 없습니다.", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        
        NodeList tags = doc.getElementsByTagName("THREAD");
        for (int i = 0; i < tags.getLength(); ++i) {
            NamedNodeMap attrs = tags.item(i).getAttributes();
            
            String name = attrs.getNamedItem("name").getNodeValue();
            int srl = Integer.parseInt(attrs.getNamedItem("tag_srl").getNodeValue());
            int score = Integer.parseInt(attrs.getNamedItem("score_day").getNodeValue());
            
            addTagToCloud(new TagInfo(name, srl, score));
        }
    }

    private void addTagToCloud(TagInfo tagInfo) {
        TagCloudLayout l = (TagCloudLayout) findViewById(R.id.tagcloudlayout);
        
        TagView t = new TagView(this);
        t.setTagInfo(tagInfo);
        l.addTagView(t);
        
        tagList.add(tagInfo);
    }

    public void onDataLoadingCancel() {
    }
}
