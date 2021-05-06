package Adapter.FolderAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gurpreet.sidhu.documentpicker.R;
import com.gurpreet.sidhu.documentpicker.R2;

import java.io.File;
import java.util.ArrayList;

import Utils.Models.GSFilesPkrModel;
import Utils.Models.FolderFileModel;
import Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private Context mContext;
    private ArrayList<FolderFileModel> mFolderFilesList;
    private FolderSelectionListner mFolderSelectionListner;
    private boolean isImages;
    private String mFileType;

    public FolderAdapter(Context mContext, ArrayList<FolderFileModel> mFolderFilesList, FolderSelectionListner mFolderSelectionListner, boolean isImages,String FileType) {
        this.mContext = mContext;
        this.mFolderFilesList = mFolderFilesList;
        this.mFolderSelectionListner = mFolderSelectionListner;
        this.isImages = isImages;
        this.mFileType = FileType;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.folder_lay, null);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        FolderFileModel folderFileModel = mFolderFilesList.get(position);
        File file = null;
        Bitmap thumbnail = null;
        String fileCount="0";
        Uri uri=null;
        if (folderFileModel.getFiles()!=null){
            fileCount = String.valueOf(folderFileModel.getFiles().size());
            if (mFileType!=null){
                for (GSFilesPkrModel filesModel : folderFileModel.getFiles()) {
                    if (mFileType.equalsIgnoreCase("image")||mFileType.equalsIgnoreCase("video")){
                        file=new File(filesModel.getFile_path());
                        holder.mImageView_Thumbnail.setVisibility(View.VISIBLE);
                        holder.mLinearLayout_FileType.setVisibility(View.GONE);
                        //  file = file;
                        if (filesModel.getFileType().equalsIgnoreCase("Video")||filesModel.getFileType().equalsIgnoreCase("Image")) {
                            if (file.exists()) {
                               /* try {
                                    thumbnail = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                                }catch (Exception e){
                                    e.getMessage();
                                }
                                if (thumbnail != null) {
                                    holder.mImageView_Thumbnail.setImageBitmap(thumbnail);
                                    break;
                                }else {
                                    int THUMBSIZE = 200;
                                    try {
                                        thumbnail=  ThumbnailUtils.extractThumbnail(
                                                BitmapFactory.decodeFile(file.getAbsolutePath()),
                                                THUMBSIZE,
                                                THUMBSIZE);
                                    }catch (Exception e){}

                                    if (thumbnail != null) {
                                        holder.mImageView_Thumbnail.setImageBitmap(thumbnail);
                                        break;
                                    }else {
                                        uri=Uri.fromFile(file);
                                        if (uri!=null){
                                            holder.mImageView_Thumbnail.setImageURI(uri);
                                            break;
                                        }
                                    }
                                }*/
                                Glide.with(mContext).
                                        load(file).
                                        thumbnail(0.1f).
                                        into(holder.mImageView_Thumbnail);
                                break;
                                }else {
                                Uri uri1=Uri.parse(filesModel.getFile_path());
                                Glide.with(mContext).
                                        load(uri).
                                        thumbnail(0.1f).
                                        into(holder.mImageView_Thumbnail);
                            }

                        }else {
                            /*File type*/
                            holder.mImageView_Thumbnail.setVisibility(View.GONE);
                            holder.mLinearLayout_FileType.setVisibility(View.VISIBLE);
                        }
                    }else {
                        /*File type*/
                        holder.mImageView_Thumbnail.setVisibility(View.GONE);
                        holder.mLinearLayout_FileType.setVisibility(View.VISIBLE);
                    }

                }

            }else {
                /*File type*/
                holder.mImageView_Thumbnail.setVisibility(View.GONE);
                holder.mLinearLayout_FileType.setVisibility(View.VISIBLE);
            }

        }
       /* if (mFileType!=null&&mFileType.equalsIgnoreCase("image")||mFileType.equalsIgnoreCase("video")){
            holder.mImageView_Thumbnail.setVisibility(View.VISIBLE);
            holder.mLinearLayout_FileType.setVisibility(View.GONE);
        }else {
            *//*File type*//*
            holder.mImageView_Thumbnail.setVisibility(View.GONE);
            holder.mLinearLayout_FileType.setVisibility(View.VISIBLE);
        }*/

        String folderName = Utils.CheckForNullValue(folderFileModel.getFolderName());

        holder.mTextView_folderName.setText(folderName);
        holder.mTextView_FileCount.setText(fileCount);


        String finalFileCount = fileCount;
        holder.mRelativeLayout_main_Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalFileCount.equalsIgnoreCase("0")){
                    mFolderSelectionListner.selectFolder(folderFileModel,position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFolderFilesList.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R2.id.RelativeLayout_main_Lay)
        RelativeLayout mRelativeLayout_main_Lay;

//        @BindView(R2.id.LinearLayout_FileType)
        LinearLayout mLinearLayout_FileType;

//        @BindView(R2.id.ImageView_Thumbnail)
        ImageView mImageView_Thumbnail;

//        @BindView(R2.id.TextView_folderName)
        TextView mTextView_folderName;

//        @BindView(R2.id.TextView_FileCount)
        TextView mTextView_FileCount;


        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            mRelativeLayout_main_Lay=itemView.findViewById(R.id.RelativeLayout_main_Lay);
            mLinearLayout_FileType=itemView.findViewById(R.id.LinearLayout_FileType);
            mImageView_Thumbnail=itemView.findViewById(R.id.ImageView_Thumbnail);
            mTextView_folderName=itemView.findViewById(R.id.TextView_folderName);
            mTextView_FileCount=itemView.findViewById(R.id.TextView_FileCount);
        }
    }
}
