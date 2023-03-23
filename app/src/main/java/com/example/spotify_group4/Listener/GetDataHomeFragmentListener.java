package com.example.spotify_group4.Listener;

import com.example.spotify_group4.Model.HomeContent;
import com.example.spotify_group4.Model.PlayList;

import java.util.List;

public interface GetDataHomeFragmentListener {
     void onGetListPlayListComplete(List<PlayList> playLists);
     void onGetListPlayListFail();
     void onGetHomeContentComplete( List<HomeContent> homeContents);
     void onGetHomeContentFail();
}
