package com.smartlife.qintin.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.smartlife.MainApplication;

/**
 * Created by wm on 2016/3/17.
 */
public class AttachFragment extends Fragment {
    private String TAG = "SmartLifee/AttachF";
    public Activity mContext;
    public MainApplication mApplicatin;
    //public String mTokenType;
    //public String mAccessToken;
   // private int mExpiresIn;
    //public CredentialModel mCredentialModel;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.mContext = activity;
    }
}
