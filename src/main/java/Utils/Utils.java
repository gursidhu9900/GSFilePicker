package Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.gurpreet.sidhu.documentpicker.GSFilePickerActivity;
import com.gurpreet.sidhu.documentpicker.R;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Utils.Models.GSFilesPkrModel;
import Utils.Models.FolderFileModel;

public class Utils {

    private static String videoExtentions = "mkv,flv,vob,ogv,ogg,avi,wmv,qt,mov,mp4,mpeg,mpg,svi,3gp,flv,f4v,f4p,f4a,f4b,3g2";
    private static String imageExtentions = "jpeg,png,jpg,bmp";
    private static String[] mDefaultVideoListArray = videoExtentions.split(",");
    private static String[] mDefaultImagesListArray = imageExtentions.split(",");

    public static String[] getDefaultVideoExtentions() {
        String[] videoTypes;
        videoTypes = videoExtentions.split(",");
        return videoTypes;
    }

    public static String[] getDefaultImagesExtentions() {
        String[] ImagesTypes;
        ImagesTypes = imageExtentions.split(",");
        return ImagesTypes;
    }

    public static String[] validateVideoExtentions(String[] filetypes) {
        String[] VideoTypes;
        String[] DefaultVideoTypes;
        String newExtentions = "";
        DefaultVideoTypes = videoExtentions.split(",");
        for (String fileType : filetypes) {
            for (int i = 0; i < DefaultVideoTypes.length; i++) {
                if (fileType.equalsIgnoreCase(DefaultVideoTypes[i])) {
                    if (newExtentions.length() > 0) {
                        newExtentions = fileType;
                    } else {
                        newExtentions = newExtentions + "," + fileType;
                    }
                }
            }
        }
        VideoTypes = newExtentions.split(",");
        return VideoTypes;
    }

    public static String[] validateImagesExtentions(String[] filetypes) {
        String[] ImagesTypes;
        String[] DefaultImagesTypes;
        String newExtentions = "";
        DefaultImagesTypes = imageExtentions.split(",");
        for (String fileType : filetypes) {
            for (int i = 0; i < DefaultImagesTypes.length; i++) {
                if (fileType.equalsIgnoreCase(DefaultImagesTypes[i])) {
                    if (newExtentions.length() > 0) {
                        newExtentions = fileType;
                    } else {
                        newExtentions = newExtentions + "," + fileType;
                    }
                }
            }
        }
        ImagesTypes = newExtentions.split(",");
        return ImagesTypes;
    }

    public static ArrayList<FolderFileModel> getFolderFiles(ArrayList<String> paths, String[] fileTypes, boolean isHiddenFolderAddable) {
        ArrayList<FolderFileModel> mFolderFilesPath = new ArrayList<>();
        for (String path : paths) {
            Boolean m_isRoot = true;
            File m_file = new File(path);
            if (!isHiddenFolderAddable) {
                if (!m_file.getName().startsWith(".")) {
                    FolderFileModel folderFileModel=getFolderData(m_file, fileTypes, path);
                    if (folderFileModel!=null&&!mFolderFilesPath.contains(folderFileModel)){
                        mFolderFilesPath.add(folderFileModel);
                    }
                }
            } else {
                FolderFileModel folderFileModel=getFolderData(m_file, fileTypes, path);
                if (folderFileModel!=null&&!mFolderFilesPath.contains(folderFileModel)){
                    mFolderFilesPath.add(folderFileModel);
                }
            }
        }
        //Removing Duplicates;
        Set<FolderFileModel> s = new HashSet<FolderFileModel>();
        s.addAll(mFolderFilesPath);
        mFolderFilesPath = new ArrayList<FolderFileModel>();
        mFolderFilesPath.addAll(s);
        //SyncedContactsList = Contacts;

        return mFolderFilesPath;
    }


    public static ArrayList<GSFilesPkrModel> getFiles(ArrayList<String> paths, String[] fileTypes, boolean isHiddenFolderAddable) {
        ArrayList<GSFilesPkrModel> mFilesPath = new ArrayList<>();
        for (String path : paths) {
            Boolean m_isRoot = true;
            File m_file = new File(path);
            if (isHiddenFolderAddable){
                File[] m_filesArray = m_file.listFiles();
           /* if(!path.equals(m_root))
            {
                m_item.add("../");
                m_path.add(m_file.getParent());
                m_isRoot=false;
            }*/
                //  m_curDir=p_rootPath;
                //sorting file list in alphabetical order
                if (m_filesArray != null) {
                    Arrays.sort(m_filesArray);


                    for (int i = 0; i < m_filesArray.length; i++) {
                        File file = m_filesArray[i];
                        if (file.isDirectory()) {
                       /* m_item.add(file.getName());
                        m_path.add(file.getPath());*/
                        } else {
                            String ext = FilenameUtils.getExtension(file.getPath());
                            if ((fileTypes != null && fileTypes.length > 0)) {

                                for (String extention : fileTypes) {
                                    if (ext.equalsIgnoreCase(extention)) {
                                        if (file.exists()) {
                                            GSFilesPkrModel filesModel = new GSFilesPkrModel();
                                            filesModel.setFile_extention(ext);
                                            filesModel.setFile_name(file.getName());
                                            filesModel.setFile_path(file.getAbsolutePath());
                                            filesModel.setFileType(getFilrType(ext));
                                            //   filesModel.setFile(file);

                                            mFilesPath.add(filesModel);
                                        }

                                    }
                                }

                            } else {
                                if (!ext.equalsIgnoreCase("")) {
                                    if (file.exists()) {
                                        GSFilesPkrModel filesModel = new GSFilesPkrModel();
                                        filesModel.setFile_extention(ext);
                                        filesModel.setFile_name(file.getName());
                                        filesModel.setFileType(getFilrType(ext));
                                        filesModel.setFile_path(file.getAbsolutePath());
                                        //  filesModel.setFile(file);
                                        mFilesPath.add(filesModel);
                                    }
                                }

                            }

                        }
                    }
               /* for (String m_AddFile : m_files) {
                    m_item.add(m_AddFile);
                }*/

                }
            }else {
                if (!m_file.getName().startsWith(".")){
                    File[] m_filesArray = m_file.listFiles();
           /* if(!path.equals(m_root))
            {
                m_item.add("../");
                m_path.add(m_file.getParent());
                m_isRoot=false;
            }*/
                    //  m_curDir=p_rootPath;
                    //sorting file list in alphabetical order
                    if (m_filesArray != null) {
                        Arrays.sort(m_filesArray);


                        for (int i = 0; i < m_filesArray.length; i++) {
                            File file = m_filesArray[i];
                            if (file.isDirectory()) {
                       /* m_item.add(file.getName());
                        m_path.add(file.getPath());*/
                            } else {
                                String ext = FilenameUtils.getExtension(file.getPath());
                                if ((fileTypes != null && fileTypes.length > 0)) {

                                    for (String extention : fileTypes) {
                                        if (ext.equalsIgnoreCase(extention)) {
                                            if (file.exists()) {
                                                GSFilesPkrModel filesModel = new GSFilesPkrModel();
                                                filesModel.setFile_extention(ext);
                                                filesModel.setFile_name(file.getName());
                                                filesModel.setFile_path(file.getAbsolutePath());
                                                filesModel.setFileType(getFilrType(ext));
                                                //   filesModel.setFile(file);

                                                mFilesPath.add(filesModel);
                                            }

                                        }
                                    }

                                } else {
                                    if (!ext.equalsIgnoreCase("")) {
                                        if (file.exists()) {
                                            GSFilesPkrModel filesModel = new GSFilesPkrModel();
                                            filesModel.setFile_extention(ext);
                                            filesModel.setFile_name(file.getName());
                                            filesModel.setFileType(getFilrType(ext));
                                            filesModel.setFile_path(file.getAbsolutePath());
                                            //  filesModel.setFile(file);
                                            mFilesPath.add(filesModel);
                                        }
                                    }

                                }

                            }
                        }
               /* for (String m_AddFile : m_files) {
                    m_item.add(m_AddFile);
                }*/

                    }
                }
            }

        }

        return mFilesPath;
    }

    public static String CheckForNullValue(String value) {
        if (value == null || value.equalsIgnoreCase("null")) {
            value = "";
        }
        return value;
    }

    public static void ShowToast(Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_SHORT);
    }



    public interface AlertCallback{
        void onOkPress();
    }

    public static void showAlertMessage(Activity activity,String message,AlertCallback callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Alert!");
        builder.setMessage(message);
        builder.setCancelable(false);
      /*  builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onOkPress();
                dialog.dismiss();


            }
        });

        builder.show();
    }

    public void getAllFiles(File parent) {
        List<File> directories = new ArrayList<>();
        List<File> files = new ArrayList<>();
        //adding initial parent whose files we want to fetch
        directories.add(parent);
        while (directories.size() != 0) {
            File f = directories.remove(0);
            if (f.isDirectory()) {
                if (f.list().length > 0) {
                    //directory filter to filter out directories if any
                    List<File> directoryList = Arrays.asList(f.listFiles(directoryFilter));
                    //file filter to filter out files if any
                    List<File> fileList = Arrays.asList(f.listFiles(fileFilter));
                    //adding directories to directory list
                    directories.addAll(directoryList);
                    //adding files to file list
                    files.addAll(fileList);
                }
            }
        }
    }

    FilenameFilter fileFilter = new FilenameFilter() {
        @Override
        public boolean accept(File file, String s) {
            return file.isFile();
        }
    };

    FilenameFilter directoryFilter = new FilenameFilter() {
        @Override
        public boolean accept(File file, String s) {
            return file.isDirectory();
        }
    };

    public enum FileTypes {Video, Image, File}

    private static String getFilrType(String extention) {
        String fileType = "";

        if (videoExtentions.contains(extention)) {
            fileType = "Video";
        } else if (imageExtentions.contains(extention)) {
            fileType = "Image";
        } else {
            fileType = "File";
        }
        return fileType;
    }


    private static FolderFileModel getFolderData(File m_file, String[] fileTypes, String path) {
        FolderFileModel folderFileModel = new FolderFileModel();
        File[] m_filesArray = m_file.listFiles();
        //sorting file list in alphabetical order
        if (m_filesArray != null) {
            Arrays.sort(m_filesArray);
            ArrayList<GSFilesPkrModel> FilesPath = new ArrayList<>();

            for (int i = 0; i < m_filesArray.length; i++) {
                File file = m_filesArray[i];
                if (file.isDirectory()) {
                       /* m_item.add(file.getName());
                        m_path.add(file.getPath());*/
                } else {
                    String ext = FilenameUtils.getExtension(file.getPath());
                    if ((fileTypes != null && fileTypes.length > 0)) {
                        for (String extention : fileTypes) {
                            if (ext.equalsIgnoreCase(extention)) {
                                if (file.exists()) {
                                    GSFilesPkrModel filesModel = new GSFilesPkrModel();
                                    filesModel.setFile_extention(ext);
                                    filesModel.setFile_name(file.getName());
                                    filesModel.setFile_path(file.getAbsolutePath());
//                                    filesModel.setUri(Uri.fromFile(file));
                                    filesModel.setFileType(getFilrType(ext));
                                    // filesModel.setFile(file);
                                    FilesPath.add(filesModel);
                                }
                            }
                        }

                    } else {
                        if (!ext.equalsIgnoreCase("")) {
                            if (file.exists()) {
                                GSFilesPkrModel filesModel = new GSFilesPkrModel();
                                filesModel.setFile_extention(ext);
                                filesModel.setFile_name(file.getName());
                                filesModel.setFile_path(file.getAbsolutePath());
//                                filesModel.setUri(Uri.fromFile(file));
                                filesModel.setFileType(getFilrType(ext));
                                //  filesModel.setFile(file);
                                FilesPath.add(filesModel);
                            }
                        }
                    }
                }
            }

            if (FilesPath!=null&&FilesPath != null && FilesPath.size() > 0) {
                folderFileModel.setFiles(FilesPath);
                if (m_file.isDirectory()) {
                    folderFileModel.setFolderName(m_file.getName());
                    folderFileModel.setFolderPath(m_file.getAbsolutePath());
                } else {
                    folderFileModel.setFolderName(path);
                    folderFileModel.setFolderPath(path);
                }
            }else {
                folderFileModel=null;
            }
        }
        return folderFileModel;
    }


    public static interface DrawableCallback {
        void setDrawable(Drawable drawable);
    }
    /*Return Text box outline*/
    public static void getToolBarBack(Context context, String color, DrawableCallback callback) {
        float mRadiusCalander = 30f;
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(Color.parseColor(color));
        shape.setCornerRadius(7);
        shape.setStroke(2, ContextCompat.getColor(context, R.color.gray));
        shape.setCornerRadii(new float[]{0, 0, 0, 0, mRadiusCalander, mRadiusCalander, mRadiusCalander, mRadiusCalander});
//        shape.setStroke(3,ContextCompat.getColor(context,R.color.gray));
        if (shape != null) {
            callback.setDrawable(shape);
        }
    }

    /*Return input drawable with input color */
    public static Drawable getDrawableWithTint(Context context, int drawable, String color) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, drawable);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(color));
        if (wrappedDrawable == null) {
            wrappedDrawable = ContextCompat.getDrawable(context, drawable);
        }
        return wrappedDrawable;
    }

}
