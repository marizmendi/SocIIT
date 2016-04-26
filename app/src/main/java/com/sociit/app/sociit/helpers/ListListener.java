package com.sociit.app.sociit.helpers;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;


import com.sociit.app.sociit.entities.RssItem;

import java.util.List;

/**
 * Created by Lazaro on 4/25/16.
 */
public class ListListener implements AdapterView.OnItemClickListener {

    List<RssItem> listItems;
    Activity activity;

    public ListListener(List<RssItem> listItems, Activity activity){
        this.listItems = listItems;
        this.activity = activity;
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
        Intent i = new Intent (Intent.ACTION_VIEW);
        i.setData(Uri.parse(listItems.get(pos).getLink()));
        activity.startActivity(i);
    }
}
