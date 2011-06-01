package kr.ac.yonsei.memeplex.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import kr.ac.yonsei.memeplex.R;
import kr.ac.yonsei.memeplex.TagInfo;
import kr.ac.yonsei.memeplex.ThreadArticle;
import kr.ac.yonsei.memeplex.api.DataLoaderListener;
import kr.ac.yonsei.memeplex.api.DataLoaderTask;
import kr.ac.yonsei.memeplex.util.TimeUtility;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadListActivity extends ListActivity implements DataLoaderListener {
    private static final int REQUEST_WRITE_THREAD = 0;

    private ArrayList<ThreadArticle> list;
    private ArrayList<TagInfo> tagList;
    private CustomAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_list);
        
        tagList = (ArrayList<TagInfo>) getIntent().getSerializableExtra("tags");
        
        list = new ArrayList<ThreadArticle>();
        adapter = new CustomAdapter(this, R.layout.thread_list_row, list);

        setListAdapter(adapter);
        
        TextView tvTagList = (TextView) findViewById(R.id.TextViewTagList);
        tvTagList.setText(getTagTextList());
        
        Button btnBackToTagList = (Button) findViewById(R.id.ButtonBackToTagList);
        btnBackToTagList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        
        Button btnRefreshThreadList = (Button) findViewById(R.id.ButtonRefreshThreadList);
        btnRefreshThreadList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshThreadList();
            }
        });
        
        Button btnWriteOnThisTag = (Button) findViewById(R.id.ButtonWriteOnThisTag);
        btnWriteOnThisTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                write();
            }
        });
        
        refreshThreadList();
    }
    
    private void refreshThreadList() {
        StringBuilder apiUrl = new StringBuilder("http://memeplex.ohmyenglish.co.kr/thread_list.php");
        
        apiUrl.append("?and=");
        apiUrl.append(getTagListWithStatus(tagList, TagInfo.TAG_STATUS_AND));
        apiUrl.append("&or=");
        apiUrl.append(getTagListWithStatus(tagList, TagInfo.TAG_STATUS_OR));
        apiUrl.append("&not=");
        apiUrl.append(getTagListWithStatus(tagList, TagInfo.TAG_STATUS_NOT));

        DataLoaderTask task = new DataLoaderTask(this, this);
        task.execute(apiUrl.toString());
    }

    private String getTagListWithStatus(ArrayList<TagInfo> tagList, int tagStatus) {
        StringBuilder sb = new StringBuilder();
        
        for (TagInfo ti : tagList) {
            if (ti.getTagStatus() == tagStatus) {
                sb.append(ti.getTag());
                sb.append(",");
            }
        }
        
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        return URLEncoder.encode(sb.toString());
    }

    private String getSrlListFromTags() {
        StringBuilder sb = new StringBuilder();
        
        for (TagInfo tag : tagList) {
            sb.append(tag.getSrl());
            sb.append(',');
        }
        
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        
        return sb.toString();
    }
    
    private String getTagTextList() {
        StringBuilder sb = new StringBuilder();
        
        for (TagInfo tag : tagList) {
            sb.append(tag.getTag());
            sb.append(',');
        }
        
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        
        return sb.toString();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Intent intent = new Intent(getApplicationContext(), ThreadViewActivity.class);
        
        intent.putExtra("nickname", list.get(position).getNickname());
        intent.putExtra("content", list.get(position).getContent());
        intent.putExtra("picturePath", list.get(position).getPicturePath());
        intent.putExtra("passed", list.get(position).getPassed());
        intent.putExtra("docSrl", list.get(position).getDocSrl());
        intent.putExtra("thread_tags", getTagTextList());
        
        startActivity(intent);
    }
    
    private void write() {
        Intent intent = new Intent(this, ThreadWriteActivity.class);
        intent.putExtra("tags", getTagTextList());
        startActivityForResult(intent, REQUEST_WRITE_THREAD);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE_THREAD) {
            refreshThreadList();
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class CustomAdapter extends ArrayAdapter<ThreadArticle>
    {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            
            if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                
                v = vi.inflate(R.layout.thread_list_row, null);
            }

            TextView tvmessage = (TextView)v.findViewById(R.id.TextViewMessage);
            TextView tvwriteinfo = (TextView)v.findViewById(R.id.TextViewWriteInfo);
            TextView tvreplies = (TextView)v.findViewById(R.id.TextViewReply);
            
            ThreadArticle threadArticle = list.get(position);
            
            if(threadArticle != null) {
                tvmessage.setText(threadArticle.getContent());
                
                String writeInfo = threadArticle.getNickname();
                
                if (threadArticle.getPassed() > 0) {
                    writeInfo += ", " + TimeUtility.getTimePassedString(threadArticle.getPassed());    // 지난 시간
                }
                
                tvwriteinfo.setText(writeInfo);
                
                if(threadArticle.getReply() > 0) {
                    tvreplies.setVisibility(View.VISIBLE);
                    tvreplies.setText("(" + threadArticle.getReply() + ")");
                }
                else {
                    tvreplies.setVisibility(View.INVISIBLE);
                }
            }
            
            ImageView imgHasImage = (ImageView)v.findViewById(R.id.ImageHasImage);
            
            if (threadArticle.hasImage()) {
                imgHasImage.setVisibility(View.VISIBLE);
            } else {
                imgHasImage.setVisibility(View.GONE);
            }
            
            tvwriteinfo.requestLayout();

            return v;
        }

        public CustomAdapter(Context context, int textViewResourceId,
                List<ThreadArticle> objects) {
            super(context, textViewResourceId, objects);
        }
    }

    public void onDataLoadingFinish(Document doc, int id) {
        if (doc == null) {
            Toast t = Toast.makeText(this, "데이터를 가져올 수 없습니다.", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        
        adapter.clear();
        
        NodeList tags = doc.getElementsByTagName("THREAD");
        for (int i = 0; i < tags.getLength(); ++i) {
            NamedNodeMap attrs = tags.item(i).getAttributes();
            
            String nickname = attrs.getNamedItem("nick_name").getNodeValue();
            String content = attrs.getNamedItem("content").getNodeValue();
            String picturePath = attrs.getNamedItem("picture_path").getNodeValue();
            int docSrl = Integer.parseInt(attrs.getNamedItem("document_srl").getNodeValue());
            int reply = Integer.parseInt(attrs.getNamedItem("comment_count").getNodeValue());
            int passed = Integer.parseInt(attrs.getNamedItem("timestamp").getNodeValue());

            list.add(new ThreadArticle(nickname, content, docSrl, reply, passed, picturePath));
        }
        
        adapter.notifyDataSetChanged();
    }

    public void onDataLoadingCancel() {
        finish();
    }
}
