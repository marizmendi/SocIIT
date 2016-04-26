package com.sociit.app.sociit.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sociit.app.sociit.R;

public class NewsFragment extends Fragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                if (savedInstanceState == null) {
                        addRssFragment();
                        Log.e("tag", "dentro1");
                }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                View view = inflater.inflate(R.layout.fragment_news, container, false);
                Log.e("tag", "dentro2");
                return view;
        }

        private void addRssFragment() {
                FragmentManager manager = super.getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                RssFragment fragment = new RssFragment();
                transaction.add(R.id.fragment_container, fragment);
                transaction.commit();
                Log.e("tag", "dentro3");
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                outState.putBoolean("fragment_added", true);
        }

        public interface OnFragmentInteractionListener {
                void onFragmentInteraction(Uri uri);
        }
}