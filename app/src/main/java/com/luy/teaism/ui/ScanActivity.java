package com.luy.teaism.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.luy.teaism.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate {

    QRCodeView qrCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qrCodeView = (QRCodeView) findViewById(R.id.zxing_view);
        qrCodeView.setDelegate(this);
        qrCodeView.startCamera();
        qrCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(result);
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.setPositiveButton("继续扫码", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                qrCodeView.startSpot();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("打开相机出错");
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qrCodeView.stopSpot();
        qrCodeView.stopCamera();
    }
}

