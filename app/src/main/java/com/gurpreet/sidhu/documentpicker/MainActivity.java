package com.gurpreet.sidhu.documentpicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import Utils.Models.GSFilesPkrModel;

public class MainActivity extends AppCompatActivity {

    private String path;
    private ArrayList<String> mFilesPath=new ArrayList<>();
    final private int Request_Location = 101;
    private int GET_SELECTED_FILES=987;
    private String m_root= Environment.getExternalStorageDirectory().getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Use the current directory as title
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                // only for gingerbread and newer versions
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE}, Request_Location);
            } else {
                firstAttempt();
            }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Request_Location:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    firstAttempt();
                } else {
                }
                break;
        }
    }

    private void firstAttempt() {
        String exts="";
      /*  Intent intent=new Intent(this,GSFilePickerActivity.class);
        intent.putExtra("extentions",exts);
        intent.putExtra("isImagesVideo","Images");
        intent.putExtra("showfolder",true);
        startActivityForResult(intent,GET_SELECTED_FILES);*/



         }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GET_SELECTED_FILES) {
            ArrayList<GSFilesPkrModel> mSelectedFiles = new ArrayList<>();
            mSelectedFiles= (ArrayList<GSFilesPkrModel>) data.getSerializableExtra(GSFilePicker.GET_DATA);
            Gson gson = new Gson();
            Log.e("onActivityResult ", " mSelectedFiles size " + mSelectedFiles.size());
            Log.e("onActivityResult ", " mSelectedFiles " + gson.toJson(mSelectedFiles));
        }
    }

    public void getVideos(View view) {
        String exts="";
        new GSFilePicker(this)
                .selectableExtentions(exts)
                .selectVideos()
                .showFolders()
                .setTitle(getString(R.string.FilePicker))
                .build(GET_SELECTED_FILES);
    }

    public void getImages(View view) {
        String exts="";
        new GSFilePicker(this)
                .selectableExtentions(exts)
                .selectImages()
                .showFolders()
                .setAppBarColor(R.color.app_bartst)
                .setstatusBarTextColor(R.color.stts_bartst)
                .setAppBarTextColor(R.color.black)
                .setTitle(getString(R.string.FilePicker))
                .build(GET_SELECTED_FILES);
    }


    public void getFiles(View view) {
//        String exts="txt,pdf,doc,docx,xlsx,xls,ppt,pptx";
        String exts="txt,pdf,doc,docx,xlsx,xls,ppt,pptx";
        new GSFilePicker(this)
               // .selectableExtentions(exts)
                .setTitle(getString(R.string.FilePicker))
                .showFolders()
                .build(GET_SELECTED_FILES);
    }
}
