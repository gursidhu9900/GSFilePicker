package com.gurpreet.sidhu.documentpicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GSFilePicker {
    private Intent intent;
    private Activity mActivity;
    private String SelectFyleType="";
    private boolean showfolder=false;
    private boolean showHiddenFolder=false;
    public static String GET_DATA =GSFilePickerActivity.GET_DATA;
    public GSFilePicker(Activity activity) {
        this.mActivity=activity;
        intent=new Intent(mActivity,GSFilePickerActivity.class);
    }

    public static GSFilePicker getBuilder(Activity activity) {
        GSFilePicker builder = new GSFilePicker(activity);
        return builder;
    }

    public GSFilePicker selectableExtentions(String extention) {
        intent.putExtra("extentions",extention);
        return this;
    }

    public GSFilePicker setFileLimitExceedMessageShow(boolean FileLimitMessageShow) {
        intent.putExtra("FileLimitMessageShow",FileLimitMessageShow);
        return this;
    }

    public GSFilePicker setSelectFileLimit(int SelectedFileLimit) {
        intent.putExtra("SelectedFileLimit",SelectedFileLimit);
        return this;
    }

    public GSFilePicker setTitle(String title) {
        intent.putExtra("title",title);
        return this;
    }

    public GSFilePicker showHiddenFolderFiles() {
        showHiddenFolder=true;
        return this;
    }

    public GSFilePicker selectImages() {
        intent.putExtra("isImagesVideo","Images");
        return this;
    }

    public GSFilePicker setAppBarColor(int color) {
        int clr= mActivity.getResources().getColor(color);
        intent.putExtra("tabColor",clr);
        return this;
    }

    public GSFilePicker setAppBarTextColor(int color) {
        int clr= mActivity.getResources().getColor(color);
        intent.putExtra("TextColor",clr);
        return this;
    }

    public GSFilePicker setstatusBarTextColor(int color) {
        int clr= mActivity.getResources().getColor(color);
        intent.putExtra("statusBarColor",clr);
        return this;
    }

    public GSFilePicker selectVideos() {
        intent.putExtra("isImagesVideo","Videos");
        return this;
    }

    public GSFilePicker showFolders() {
        showfolder=true;

        return this;
    }


    public void build(int RETURN_CODE) {
        // intent.putExtra("isImagesVideo",SelectFyleType);
        intent.putExtra("showfolder",showfolder);
        intent.putExtra("showHiddenFolder",showHiddenFolder);
        mActivity.startActivityForResult(intent,RETURN_CODE);
    }
}


