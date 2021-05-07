package com.gurpreet.sidhu.documentpicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Adapter.FileAdapter.FileViewAdapter;
import Adapter.FolderAdapter.FolderAdapter;
import Adapter.FolderAdapter.FolderSelectionListner;
import Adapter.FileAdapter.FileViewAdapterListner;
import Utils.Models.GSFilesPkrModel;
import Utils.Models.FolderFileModel;
import Utils.Utils;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GSFilePickerActivity extends AppCompatActivity {


    //    @BindView(R2.id.LinearLayout_Toolbar)
    LinearLayout mLinearLayout_Toolbar;

    //    @BindView(R2.id.ImageView_Back)
    ImageView mImageView_Back;

    //    @BindView(R2.id.RecyclerView_FilesView)
    RecyclerView mRecyclerView_FilesView;

    //    @BindView(R2.id.RecyclerView_FoldersView)
    RecyclerView mRecyclerView_FoldersView;

    //    @BindView(R2.id.LinearLayout_Back)
    LinearLayout mLinearLayout_Back;

    //    @BindView(R2.id.TextView_Title)
    TextView mTextView_Title;

    //    @BindView(R2.id.TextView_Done)
    TextView mTextView_Done;

    private SelectUnSelectFiles mSelectUnSelectFiles = new SelectUnSelectFiles();
    private FolderSelected mFolderSelected = new FolderSelected();
    private FileViewAdapter mFileViewAdapter;
    private FolderAdapter mFolderAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManagerFolders = new GridLayoutManager(this, 2);
    private boolean isGridView = false;
    private boolean isFolderDetailOpened = false;
    private boolean isImages = true;
    private boolean isImagesVideos = true;
    private String path;
    private int mAppBarColor;
    private int mAppBarTextColor;
    private String mFileType;
    private boolean isFileLimitMessageShow = false;
    private List<File> mDirectoryList = new ArrayList<>();
    private ArrayList<FolderFileModel> mFolderFilsList = new ArrayList<>();
    private ArrayList<GSFilesPkrModel> mFilesPath = new ArrayList<>();
    private ArrayList<GSFilesPkrModel> mSelectedFiles = new ArrayList<>();
    private ArrayList<String> m_item = new ArrayList<String>();
    private ArrayList<String> m_path = new ArrayList<String>();
    private String[] fileTypes;
    private ArrayList<String> m_filesPath = new ArrayList<String>();
    private int mLastPathCount = 999999999;
    final private int Request_Location = 101;
    private String m_root = Environment.getExternalStorageDirectory().getPath();
    public static String GET_DATA = "selected_files";
    private boolean isHiddenFolderAddable = false;
    private boolean isFolderView = false;
    private boolean isFirstFolderCount = true;
    private int mRecyclerChacheSize=70;
    private int mSelectedFileLimit=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_g_s_file_picker);
//        ButterKnife.bind(this);

        mLinearLayout_Toolbar=findViewById(R.id.LinearLayout_Toolbar);
        mImageView_Back=findViewById(R.id.ImageView_Back);
        mRecyclerView_FilesView=findViewById(R.id.RecyclerView_FilesView);
        mRecyclerView_FoldersView=findViewById(R.id.RecyclerView_FoldersView);
        mTextView_Title=findViewById(R.id.TextView_Title);
        mLinearLayout_Toolbar=findViewById(R.id.LinearLayout_Toolbar);
        mTextView_Done=findViewById(R.id.TextView_Done);
        mLinearLayout_Back=findViewById(R.id.LinearLayout_Back);
        mTextView_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedFiles == null || mSelectedFiles.size() <= 0) {
                    Utils.ShowToast(GSFilePickerActivity.this, "No file is selected");
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(GET_DATA, mSelectedFiles);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        mLinearLayout_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*Setting up UI of screen*/
        settingUI();

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            // only for gingerbread and newer versions
            ActivityCompat.requestPermissions(GSFilePickerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, Request_Location);
        } else {
            firstAttempt();
        }


    }


    private void settingUI() {

        if (getIntent().hasExtra("tabColor")) {
            mAppBarColor=getIntent().getIntExtra("tabColor",R.color.colorPrimaryDark);
//            mLinearLayout_Toolbar.setBackgroundColor(mAppBarColor);

            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.setStatusBarColor(Color.parseColor(mColorConfigModel.getAppTopBarColor()));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.selection_color));

        }

          if (getIntent().hasExtra("tabColorHaxCode")){
            String tabColor=getIntent().getStringExtra("tabColorHaxCode");
            Utils.getToolBarBack(this, tabColor, new Utils.DrawableCallback() {
                @Override
                public void setDrawable(Drawable drawable) {
                    mLinearLayout_Toolbar.setBackground(drawable);
                }
            });

        }
          
        if (getIntent().hasExtra("tabColorHaxCode")){
            String tabColor=getIntent().getStringExtra("tabColorHaxCode");
            Utils.getToolBarBack(this, tabColor, new Utils.DrawableCallback() {
                @Override
                public void setDrawable(Drawable drawable) {
                    mLinearLayout_Toolbar.setBackground(drawable);
                }
            });

        }

        if (getIntent().hasExtra("statusBarColor")) {
            int statusBarColor=getIntent().getIntExtra("statusBarColor",mAppBarColor);
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }

         if (getIntent().hasExtra("statusBarColorHex")) {
            int statusBarColor=Color.parseColor(getIntent().getStringExtra("statusBarColorHex"));
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }

        if (getIntent().hasExtra("title")) {
            String title=Utils.CheckForNullValue(getIntent().getStringExtra("title"));
            if (!title.equalsIgnoreCase("")){
                mTextView_Title.setText(title);
            }
        }

        if (getIntent().hasExtra("tabIconColorHaxCode")) {
            String TextColor=getIntent().getStringExtra("tabTextColorHaxCode");
            mImageView_Back.setColorFilter(Color.parseColor(TextColor), PorterDuff.Mode.SRC_ATOP);
        }
        if (getIntent().hasExtra("tabTextColorHaxCode")) {
            String TextColor=getIntent().getStringExtra("tabTextColorHaxCode");
            mTextView_Done.setTextColor(Color.parseColor(TextColor));
            mTextView_Title.setTextColor(Color.parseColor(TextColor));
        }else if (getIntent().hasExtra("TextColor")) {
            mAppBarTextColor=getIntent().getIntExtra("TextColor",R.color.white);
            mTextView_Done.setTextColor(mAppBarTextColor);
            mTextView_Title.setTextColor(mAppBarTextColor);

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Request_Location:
                if (grantResults!=null&&grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    firstAttempt();
                } else {
                    Utils.ShowToast(GSFilePickerActivity.this, "Please grant the permission");
                }
                break;
        }
    }

    private void firstAttempt() {
        /*Get Configurations*/
        getConfigurations();

        /*Getting all folders on device*/
        getFolders();
    }

    /*Get Configurations*/
    private void getConfigurations() {

        if (getIntent().hasExtra("SelectedFileLimit")) {
            mSelectedFileLimit=getIntent().getIntExtra("SelectedFileLimit",0);
            if (mSelectedFileLimit>1){
//                Utils.showAlertMessage(this,"You can select only "+mSelectedFileLimit+" files");
//                Utils.ShowToast(GSFilePickerActivity.this,"You can select only "+mSelectedFileLimit+" files");
            }
        }

        /*Getting extentions to show files*/
        String[] tempFileTypes = null;

        if (getIntent().hasExtra("extentions")) {
            String extentions = getIntent().getStringExtra("extentions");
            tempFileTypes = extentions.split(",");
            if (tempFileTypes != null && tempFileTypes.length == 1 && tempFileTypes[0].equalsIgnoreCase("")) {
                tempFileTypes = null;
            }
        }

        path = m_root;
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        if (getIntent().hasExtra("FileLimitMessageShow")) {
            isFileLimitMessageShow=getIntent().getBooleanExtra("FileLimitMessageShow",false);

        }

        if (getIntent().hasExtra("showHiddenFolder")) {
            isHiddenFolderAddable = getIntent().getBooleanExtra("showHiddenFolder", false);
        }


        if (getIntent().hasExtra("isImagesVideo")) {
            isImagesVideos = true;

            /*For detail view*/
            isGridView = true;

            String selectionType = getIntent().getStringExtra("isImagesVideo");
            if (selectionType.equalsIgnoreCase("Images")) {
                isImages = true;
                mFileType = "image";
            } else {
                isImages = false;
                mFileType = "video";
            }
            /*Setting default extentions for videos and images */
            if (fileTypes != null && fileTypes.length > 0) {
                if (isImages) {
                    fileTypes = Utils.validateImagesExtentions(tempFileTypes);
                } else {
                    fileTypes = Utils.validateVideoExtentions(tempFileTypes);
                }
            } else {
                if (isImages) {
                    fileTypes = Utils.getDefaultImagesExtentions();
                } else {
                    fileTypes = Utils.getDefaultVideoExtentions();
                }
            }
        } else {
            fileTypes = tempFileTypes;
            isImages = false;
            isImagesVideos = false;
        }

        /*Show folder wise or show all files in list*/
        if (getIntent().hasExtra("showfolder")) {
            isFolderView = getIntent().getBooleanExtra("showfolder", false);
            if (isFolderView) {
                mRecyclerView_FoldersView.setVisibility(View.VISIBLE);
                mRecyclerView_FilesView.setVisibility(View.GONE);
                mTextView_Done.setVisibility(View.GONE);

//                mRecyclerView_FoldersView.setHasFixedSize(true);
                mRecyclerView_FoldersView.setItemViewCacheSize(mRecyclerChacheSize);
//                mRecyclerView_FoldersView.setDrawingCacheEnabled(true);
                mRecyclerView_FoldersView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

//                mRecyclerView_FilesView.setHasFixedSize(true);
                mRecyclerView_FilesView.setItemViewCacheSize(mRecyclerChacheSize);
//                mRecyclerView_FilesView.setDrawingCacheEnabled(true);
                mRecyclerView_FilesView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            } else {
                mRecyclerView_FoldersView.setVisibility(View.GONE);
                mRecyclerView_FilesView.setVisibility(View.VISIBLE);
                mTextView_Done.setVisibility(View.VISIBLE);

//                mRecyclerView_FilesView.setHasFixedSize(true);
                mRecyclerView_FilesView.setItemViewCacheSize(mRecyclerChacheSize);
//                mRecyclerView_FilesView.setDrawingCacheEnabled(true);
                mRecyclerView_FilesView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            }
        }

        /*To open details of video and image folder*/
        if (isGridView) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new GridLayoutManager(this, 1);
        }

    }


    /*Getting all folders on device*/
    private void getFolders() {

        // setTitle(path);

        // Read all files sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
        }
        Collections.sort(values);
        getDirFromRoot(path);

        //List<File> dirList= getAllFolders(new File(path));
       /* for (File file:dirList){
            getAllFiles(file);
        }
*/
      /*  mFileViewAdapter = new FileViewAdapter(this, mFilesPath, mSelectUnSelectFiles, isGridView,isImages);
        mRecyclerView_FilesView.setLayoutManager(mLayoutManager);
        mRecyclerView_FilesView.setAdapter(mFileViewAdapter);*/
    }

    public void getDirFromRoot(String p_rootPath) {
        if (!isFirstFolderCount&&m_path.size() == mLastPathCount) {
            if (isFolderView) {
                mFolderFilsList = Utils.getFolderFiles(m_path, fileTypes, isHiddenFolderAddable);
                mFolderAdapter = new FolderAdapter(this, mFolderFilsList, mFolderSelected, isImages, mFileType);
                mRecyclerView_FoldersView.setLayoutManager(mLayoutManagerFolders);
                mRecyclerView_FoldersView.setAdapter(mFolderAdapter);
            } else {
                mFilesPath = Utils.getFiles(m_path, fileTypes, isHiddenFolderAddable);
                mFileViewAdapter = new FileViewAdapter(this, mFilesPath, mSelectUnSelectFiles, isGridView, isImages,mSelectedFiles);
                mRecyclerView_FilesView.setLayoutManager(mLayoutManager);
                mRecyclerView_FilesView.setAdapter(mFileViewAdapter);
            }

           /* Gson gson = new Gson();
            Log.e("Final_result ", " mFilesPath " + gson.toJson(mFilesPath));*/

        } else {
            if (isFirstFolderCount) {
                isFirstFolderCount = false;
            } else {
                mLastPathCount = m_path.size();
            }

            if (m_path == null) {
                m_path = new ArrayList<String>();
                m_path.add(p_rootPath);
            } else if (m_path.size() == 0) {
                m_path.add(p_rootPath);
            }
            ArrayList<String> paths = (ArrayList<String>) m_path.clone();
            for (String path : paths) {
                Boolean m_isRoot = true;
                File m_file = new File(path);
                File[] m_filesArray = m_file.listFiles();
                if (!path.equals(m_root)) {
                    m_item.add("../");
//                    m_path.add(m_file.getParent());
                    m_isRoot = false;
                }
//                m_path.add(m_file.getParent());
                //  m_curDir=p_rootPath;
                //sorting file list in alphabetical order
                if (m_filesArray != null) {
                    Arrays.sort(m_filesArray);


                    for (int i = 0; i < m_filesArray.length; i++) {
                        File file = m_filesArray[i];
                        if (file.isDirectory()) {
//                            m_item.add(file.getName());
//                            m_path.add(file.getPath());
                            if (!m_path.contains(file.getPath())) {
                                if (isHiddenFolderAddable) {
                                    if (!file.getPath().contains("Android/data/")||file.getPath().contains("Android/data/com.merg")){
                                        m_path.add(file.getPath());
                                    }

                                } else {
                                    if (!file.getPath().startsWith(".")) {
                                        if (!file.getPath().contains("Android/data/")||file.getPath().contains("Android/data/com.merg")){
                                            m_path.add(file.getPath());
                                        }/*else if (file.getPath().contains("com.merg")){
                                            m_path.add(file.getPath());
                                        }*/
                                    }
                                }

                            }
                        } else {
                      /*  m_files.add(file.getName());
                        m_filesPath.add(file.getPath());
                        Log.e("getDirFromRoot "," file.getPath() "+file.getPath());
                        Log.e("getDirFromRoot "," file.getName() "+file.getName());
                        mFilesPath.add(file.getPath());*/
                        }
                    }
                /*for (String m_AddFile : m_files) {
                    m_item.add(m_AddFile);
                }*/
/*
                    for (String m_AddPath : m_filesPath) {
                        if (!m_path.contains(m_AddPath)) {
                            if (isHiddenFolderAddable) {
                                m_path.add(m_AddPath);
                            } else {
                                if (!m_AddPath.startsWith(".")) {
                                    m_path.add(m_AddPath);
                                }
                            }

                        }

                    }
*/

                }
            }
            getDirFromRoot(p_rootPath);

        }
    }


    /*Folder select listner*/
    public class FolderSelected implements FolderSelectionListner {
        @Override
        public void selectFolder(FolderFileModel folderFileModel, int position) {
            if (isGridView) {
                mLayoutManager = new GridLayoutManager(GSFilePickerActivity.this, 3);
            } else {
                mLayoutManager = new GridLayoutManager(GSFilePickerActivity.this, 1);
            }
            isFolderDetailOpened = true;
            ArrayList<GSFilesPkrModel> FilesPath = new ArrayList<>();
            FilesPath = folderFileModel.getFiles();
            mRecyclerView_FoldersView.setVisibility(View.GONE);
            mRecyclerView_FilesView.setVisibility(View.VISIBLE);
            mTextView_Done.setVisibility(View.VISIBLE);
            mFileViewAdapter = new FileViewAdapter(GSFilePickerActivity.this, FilesPath, mSelectUnSelectFiles, isGridView, isImages,mSelectedFiles);
            mRecyclerView_FilesView.setLayoutManager(mLayoutManager);
            mRecyclerView_FilesView.setAdapter(mFileViewAdapter);
        }
    }

    /*Select unselect files*/
    public class SelectUnSelectFiles implements FileViewAdapterListner {
        @Override
        public void selectFile(GSFilesPkrModel filesModel, int position) {
            if (mSelectedFileLimit==0){
                mSelectedFiles.add(filesModel);
                mFileViewAdapter.updateSelectedList(mSelectedFiles);
            }else {
                mSelectedFiles.add(filesModel);
                if (mSelectedFileLimit==mSelectedFiles.size()){
                   /* Intent returnIntent = new Intent();
                    returnIntent.putExtra(GET_DATA, mSelectedFiles);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();*/

                    if (isFileLimitMessageShow){
                        Utils.showAlertMessage(GSFilePickerActivity.this, "You can select only " + mSelectedFileLimit + " files", new Utils.AlertCallback() {
                            @Override
                            public void onOkPress() {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra(GET_DATA, mSelectedFiles);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        });

                    }else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(GET_DATA, mSelectedFiles);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
              /*  int totalCount=mSelectedFiles.size()+1;
                if (totalCount>mSelectedFileLimit){
                    Utils.ShowToast(GSFilePickerActivity.this,"You can't select files more than "+mSelectedFileLimit);
                }else {
                    mSelectedFiles.add(filesModel);
                }
*/
            }

        }

        @Override
        public void unSelectFile(GSFilesPkrModel filesModel, int position) {
            mSelectedFiles.remove(filesModel);
            mFileViewAdapter.updateSelectedList(mSelectedFiles);
        }
    }

    @Override
    public void onBackPressed() {

        if (mSelectedFiles != null && mSelectedFiles.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GSFilePickerActivity.this);
            builder.setTitle("Alert!");
            builder.setMessage("Your selected files will be lost.");
            builder.setCancelable(true);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mSelectedFiles.clear();
                    if (isFolderDetailOpened) {
                        mTextView_Done.setVisibility(View.GONE);
                        mRecyclerView_FilesView.setVisibility(View.GONE);
                        mRecyclerView_FoldersView.setVisibility(View.VISIBLE);
                    } else {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }

                }
            });

            builder.show();
        } else {
            if (isFolderDetailOpened) {
                isFolderDetailOpened = false;
                mTextView_Done.setVisibility(View.GONE);
                mRecyclerView_FilesView.setVisibility(View.GONE);
                mRecyclerView_FoldersView.setVisibility(View.VISIBLE);
            } else {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }

    }


}
