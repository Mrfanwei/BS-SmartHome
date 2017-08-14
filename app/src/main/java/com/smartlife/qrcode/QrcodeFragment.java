package com.smartlife.qrcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import com.smartlife.MainActivity;
import com.smartlife.R;
import com.smartlife.qrcode.utils.ZXingUtils;

public class QrcodeFragment extends DialogFragment implements View.OnClickListener {

    private String TAG = "SmartLife/QrcodeFrag";
    public MainActivity mActivity;
    public Activity mContext;
    private ViewGroup mContent;
    private LayoutInflater mLayoutInflater;
    private View mLoadView;
    private ImageView ivQrcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (MainActivity)getActivity();

        mContent = (ViewGroup) inflater.inflate(R.layout.fragment_recommend_container, container, false);

        mLayoutInflater = LayoutInflater.from(mContext);
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mLoadView = mLayoutInflater.inflate(R.layout.fragment_qrcode, container, false);
        ivQrcode = (ImageView) mLoadView.findViewById(R.id.iv_qrcode);
        mLoadView.findViewById(R.id.btn_test).setOnClickListener(this);

        mContent.addView(mLoadView);

        return mContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.56);
        int dialogWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.63);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onClick(View view) {
        String url = "fanwei";
        Bitmap bitmap = ZXingUtils.createQRImage(url, ivQrcode.getWidth(), ivQrcode.getHeight());
        ivQrcode.setImageBitmap(bitmap);
    }
}
