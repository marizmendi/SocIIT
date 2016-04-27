package com.sociit.app.sociit.fragments;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sociit.app.sociit.R;
import com.sociit.app.sociit.entities.Activity;
import com.sociit.app.sociit.entities.Building;
import com.sociit.app.sociit.fragments.ActivityFragment.OnListFragmentInteractionListener;
import com.sociit.app.sociit.fragments.dummy.DummyContent.DummyItem;

import java.util.List;
/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyActivityRecyclerViewAdapter extends RecyclerView.Adapter<MyActivityRecyclerViewAdapter.ViewHolder> {
    private final List<Activity> mValues;
    private final OnListFragmentInteractionListener mListener;
    public MyActivityRecyclerViewAdapter(List<Activity> activities, OnListFragmentInteractionListener listener) {
        mValues = activities;
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_activity, parent, false);
        return new ViewHolder(view);
    }
    public int getBuildingImageId(Building building) {
        int id;
        switch (building.getName()) {
            case "MTCC":
                id = R.drawable.mtcc;
                break;
            case "Stuart Building":
                id = R.drawable.stuart;
                break;
            case "Hermann Hall":
                id = R.drawable.hermann;
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
        holder.mActivityName.setText(mValues.get(position).getName());
        holder.mParticipantsNumber.setText(mValues.get(position).getNumberUsers() + "");
        holder.mBuilding.setText(mValues.get(position).getBuilding().getName());
        holder.mDate.setText(mValues.get(position).getDate().toString());
        holder.mImageView.setImageResource(getBuildingImageId(mValues.get(position).getBuilding()));
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
        public final TextView mActivityName;
        public final TextView mParticipantsNumber;
        public final TextView mBuilding;
        public final TextView mDate;
        public final ImageView mImageView;
        public Activity mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mActivityName = (TextView) view.findViewById(R.id.activityName);
            mParticipantsNumber = (TextView) view.findViewById(R.id.participantsNumber);
            mBuilding = (TextView) view.findViewById(R.id.building);
            mDate = (TextView) view.findViewById(R.id.date);
            mImageView = (ImageView) view.findViewById(R.id.buildingImage);
        }
    }
}
