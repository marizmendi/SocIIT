package com.sociit.app.sociit.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sociit.app.sociit.R;
import com.sociit.app.sociit.entities.Building;
import com.sociit.app.sociit.fragments.BuildingFragment.OnListFragmentInteractionListener;
import com.sociit.app.sociit.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBuildingRecyclerViewAdapter extends RecyclerView.Adapter<MyBuildingRecyclerViewAdapter.ViewHolder> {

    private final List<Building> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyBuildingRecyclerViewAdapter(List<Building> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_building, parent, false);
        return new ViewHolder(view);
    }

    public int getBuildingImageId(Building building){
        int id;
        switch (building.getName()){
            case "MTCC":
                id = R.drawable.mtcc;
                break;
            case "Stuart Building":
                id = R.drawable.stuart;
                break;
            case "Hermann Hall":
                id = R.drawable.hermann;
                break;
            case "S. R. Crown Hall":
                id = R.drawable.srcrownhall;
                break;
            case "Paul V Galvin Library":
                id = R.drawable.paulvgalvin;
                break;
            case "Keating Sports Center":
                id = R.drawable.keating;
                break;
            case "VanderCook College of Music":
                id = R.drawable.vandercook;
                break;
            case "IIT Tower":
                id = R.drawable.tower;
                break;
            case "Life Sciences Building":
                id = R.drawable.lifescience;
                break;
            case "Perlstein Hall":
                id = R.drawable.perlstein;
                break;
            case "Wishnick Hall":
                id = R.drawable.wishnick;
                break;
            default:
                id = R.drawable.iitlogo;
                break;
        }
        return id;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getActivityList().size()+"");
        holder.mImageView.setImageResource(getBuildingImageId(mValues.get(position)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public Building mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.buildingImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
