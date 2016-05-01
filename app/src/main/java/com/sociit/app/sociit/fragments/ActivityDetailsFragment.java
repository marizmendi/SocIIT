package com.sociit.app.sociit.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sociit.app.sociit.MyApplication;
import com.sociit.app.sociit.R;
import com.sociit.app.sociit.activities.MainActivity;
import com.sociit.app.sociit.entities.Activity;
import com.sociit.app.sociit.entities.User;
import com.sociit.app.sociit.helpers.SqlHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActivityDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivityDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SqlHelper db;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Activity activity;
    private OnFragmentInteractionListener mListener;
    boolean userExists;

    public ActivityDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityDetailsFragment newInstance(String param1, String param2) {
        ActivityDetailsFragment fragment = new ActivityDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_details, container, false);

        setupDetails(v);
        return v;
    }

    public void setupDetails(View v) {
        db = new SqlHelper(getActivity().getApplicationContext());
        TextView activityIdName = (TextView) v.findViewById(R.id.activityIdName);
        TextView activityIdCreator = (TextView) v.findViewById(R.id.activityIdCreator);
        TextView activityIdDescription = (TextView) v.findViewById(R.id.activityIdDescription);
        TextView activityIdDateAndTime = (TextView) v.findViewById(R.id.activityIdDateAndTime);
        TextView activityIdPlace = (TextView) v.findViewById(R.id.activityIdPlace);
        TextView activityIdPeople = (TextView) v.findViewById(R.id.activityIdPeople);
        Button joinButton = (Button) v.findViewById(R.id.joinButton);
        final Button leaveButton = (Button) v.findViewById(R.id.leaveButton);
        leaveButton.setVisibility(View.INVISIBLE);

        final int activityId = this.getArguments().getInt("activityId");
        activity = db.getActivityById(activityId);
        final String activityCreator = activity.getCreator() == null ? "NULL" : activity.getCreator().getName();
        activityIdName.setText(activity.getName());
        activityIdCreator.setText(activityCreator);
        activityIdDescription.setText(activity.getDescription());
        activityIdDateAndTime.setText(activity.getDate().toString());
        activityIdPlace.setText(activity.getBuilding().getName());
        activityIdPeople.setText("" + activity.getNumberUsers());

        User user = ((MyApplication) getActivity().getApplication()).getSession().getUser();
        for (int i = 0; i < db.getActivityUsers(activity).size(); i++) {
            //Checks if the user is in that activity already and if so, displays the leave button
            if (activity.getUserList().get(i).getName().equals(user.getName())) {
                userExists = true;
                leaveButton.setVisibility(View.VISIBLE);
            }
        }

        //If the user is the creator the button does not show
        if (activity.getCreatorId() == user.getId()) {
            leaveButton.setVisibility(View.INVISIBLE);
        }

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = ((MyApplication) getActivity().getApplication()).getSession().getUser();
                for (int i = 0; i < db.getActivityUsers(activity).size(); i++) {
                    if (db.getActivityUsers(activity).get(i).getName().equals(user.getName())) {
                        userExists = true;
                        if (db.getActivityUsers(activity).get(i).getId() != 1) {
                            leaveButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (!userExists) {
                    db.linkUserActivity(user, activity);
                    leaveButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "You have successfully joined the event", Toast.LENGTH_SHORT).show();
                    String message = "I've joined the activity " + activity.getName() + " at " + activity.getBuilding().getName();
                    ((MainActivity) getActivity()).twit(message);
                    ((MainActivity) getActivity()).notification(((MyApplication) getActivity().getApplication()).getSession().getUser().getUsername() + ": " + message);

                } else Toast.makeText(getContext(), "You are already in this activity", Toast.LENGTH_SHORT).show();

                userExists = false;

                if (activity.getCreatorId() == user.getId()) {
                    leaveButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = ((MyApplication) getActivity().getApplication()).getSession().getUser();
                Toast.makeText(getContext(), "You have successfully canceled your participation in the activity", Toast.LENGTH_SHORT).show();
                db.leaveActivity(user.getId(), activityId);
                userExists = false;
                leaveButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
