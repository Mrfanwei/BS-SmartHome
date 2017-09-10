package com.smartlife.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;

import com.smartlife.R;
import com.smartlife.qrcode.utils.ZXingUtils;

    public class QRScanFragment extends DialogFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    private String TAG = "SmartLife/QrcodeFrag";
    private ImageView iv_qrcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        iv_qrcode = (ImageView) view.findViewById(R.id.iv_qrcode);
        iv_qrcode.getViewTreeObserver().addOnGlobalLayoutListener(this);
        initView();
        return view;
    }

    /**
     * 设置fragment高度 、宽度
     */
    private void initView() {
        int dialogHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.56);
        int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.63);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onGlobalLayout() {
        String url = "fanwei";
        Bitmap bitmap = ZXingUtils.createQRImage(url, iv_qrcode.getWidth(), iv_qrcode.getHeight());
        iv_qrcode.setImageBitmap(bitmap);
    }
}
