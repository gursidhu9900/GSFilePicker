package Adapter.FileAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gurpreet.sidhu.documentpicker.R;
import com.gurpreet.sidhu.documentpicker.R2;

import java.io.File;
import java.util.ArrayList;

import Utils.Models.GSFilesPkrModel;
import Utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewAdapter extends RecyclerView.Adapter<FileViewAdapter.FileViewHolder> {

    private Context mContext;
    private ArrayList<GSFilesPkrModel> mFiles;
    private FileViewAdapterListner mFileViewAdapterListner;
    private boolean isGridView = false;
    private boolean isImage = false;
    private int color;
    private ArrayList<GSFilesPkrModel> mSelectedFiles ;

    public FileViewAdapter(Context mContext, ArrayList<GSFilesPkrModel> mFiles, FileViewAdapterListner mFileViewAdapterListner, boolean isGridView, boolean isImage,ArrayList<GSFilesPkrModel> selectedFiles) {
        this.mContext = mContext;
        this.mFiles = mFiles;
        this.mFileViewAdapterListner = mFileViewAdapterListner;
        this.isGridView = isGridView;
        this.isImage = isImage;
        this.mSelectedFiles = selectedFiles;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isGridView){
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_file_show_lay, null);
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.simple_file_show_lay, null);
        }

        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileViewHolder holder, final int position) {
        final boolean[] isSelected = {false};
        final GSFilesPkrModel filesModel = mFiles.get(position);

        String fileName = Utils.CheckForNullValue(filesModel.getFile_name());
        String extension = Utils.CheckForNullValue(filesModel.getFile_extention());
        String fileType = Utils.CheckForNullValue(filesModel.getFileType());
        File file=new File(filesModel.getFile_path());
        int mDrawable = R.drawable.other_doc_back;
        Bitmap thumbnail = null;

        if (isGridView){
            if (file.exists()){
               /* try {
                    thumbnail =  ThumbnailUtils.createVideoThumbnail(filesModel.getFile_path(),
                            MediaStore.Images.Thumbnails.MINI_KIND);
                }catch (Exception e){

                }

                if (thumbnail!=null){
                    holder.mImageView_Document_Thumb.setImageBitmap(thumbnail);
                }else {
                    int THUMBSIZE = R.dimen.gridfile_lay_size;
                    Uri image_uri1 = Uri.fromFile(file);
                    try {
                        thumbnail=  ThumbnailUtils.extractThumbnail(
                                BitmapFactory.decodeFile(file.getAbsolutePath()),
                                THUMBSIZE,
                                THUMBSIZE);
                    }catch (Exception e){}
                    if (thumbnail!=null){
                        holder.mImageView_Document_Thumb.setImageBitmap(thumbnail);
                    }else {
                        holder.mImageView_Document_Thumb.setImageURI(image_uri1);
                    }
                }*/
                Glide.with(mContext).
                        load(file).
                        thumbnail(0.1f).
                        into(holder.mImageView_Document_Thumb);
            }else {
                File file2=new File(filesModel.getFile_path());
                Uri image_uri1 = Uri.fromFile(file);
                if (!file.exists()){
                    //   Uri image_uri1 = Uri.fromFile(file);
                    holder.mImageView_Document_Thumb.setImageURI(image_uri1);
                }

/*
                Glide.with(mContext).
                        load(image_uri1).
                        thumbnail(0.1f).
                        into(holder.mImageView_Document_Thumb);
*/
            }

            if (isImage){
                holder.mLinearLayout_PlayButton.setVisibility(View.GONE);
            }else {
                holder.mLinearLayout_PlayButton.setVisibility(View.VISIBLE);
            }


        }
        if (fileType.equalsIgnoreCase("File")){
            holder.mImageView_Document_Thumb.setVisibility(View.GONE);
            holder.LinearLayout_DocumentBack.setVisibility(View.VISIBLE);

            mDrawable = R.drawable.other_doc_back;
            if (extension.equalsIgnoreCase("PDF") || extension.equalsIgnoreCase("pdf")) {
                mDrawable = R.drawable.pdf_back;
            } else if (extension.equalsIgnoreCase("doc") || extension.equalsIgnoreCase("DOC")) {
                mDrawable = R.drawable.doc_back;
            } else if (extension.equalsIgnoreCase("docx") || extension.equalsIgnoreCase("DOCX")) {
                mDrawable = R.drawable.doc_back;
            } else if (extension.equalsIgnoreCase("TXT") || extension.equalsIgnoreCase("txt")) {
                mDrawable = R.drawable.txt_back;
            } else if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("XLSX")) {
                mDrawable = R.drawable.xls_back;
            }
            if (extension.length()>4){
                holder.mTextView_DocumenExtention.setTextSize(13);
            }else {
                holder.mTextView_DocumenExtention.setTextSize(16);
            }
            holder.mTextView_DocumenExtention.setText(extension);
            holder.LinearLayout_DocumentBack.setBackground(ContextCompat.getDrawable(mContext, mDrawable));
        }else {
            holder.mImageView_Document_Thumb.setVisibility(View.VISIBLE);
            holder.LinearLayout_DocumentBack.setVisibility(View.GONE);
        }

        if (mSelectedFiles.contains(filesModel)){
            holder.mLinearLayout_SelectedItem.setVisibility(View.VISIBLE);
        }else {
            holder.mLinearLayout_SelectedItem.setVisibility(View.GONE);
        }




        holder.mImageView_Selection.setColorFilter(ContextCompat.getColor(mContext,R.color.gray), PorterDuff.Mode.SRC_ATOP);
        holder.mTextView_DocumenName.setText(fileName);
        holder.mLinearLayout_mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isSelected[0]) {
                if ( holder.mLinearLayout_SelectedItem.getVisibility()==View.VISIBLE) {
                    isSelected[0] = false;
                    mFileViewAdapterListner.unSelectFile(filesModel, position);
                    holder.mImageView_Selection.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_uncheck));
                    holder.mImageView_Selection.setColorFilter(ContextCompat.getColor(mContext,R.color.gray), PorterDuff.Mode.SRC_ATOP);
                    holder.mLinearLayout_mainLay.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                    holder.mLinearLayout_SelectedItem.setVisibility(View.GONE);
                } else {
                    isSelected[0] = true;
                    holder.mLinearLayout_SelectedItem.setVisibility(View.VISIBLE);
                    mFileViewAdapterListner.selectFile(filesModel, position);
                    holder.mImageView_Selection.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_check));
                    holder.mImageView_Selection.setColorFilter(ContextCompat.getColor(mContext,R.color.gray), PorterDuff.Mode.SRC_ATOP);
                    holder.mLinearLayout_mainLay.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selection_color));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView_Selection;


//        @BindView(R2.id.LinearLayout_mainLay)
        LinearLayout mLinearLayout_mainLay;

//        @BindView(R2.id.LinearLayout_SelectedItem)
        LinearLayout mLinearLayout_SelectedItem;

//        @BindView(R2.id.LinearLayout_PlayButton)
        LinearLayout mLinearLayout_PlayButton;

//        @BindView(R2.id.ImageView_Document_Thumb)
        ImageView mImageView_Document_Thumb;


//        @BindView(R2.id.TextView_DocumenName)
        TextView mTextView_DocumenName;

//        @BindView(R2.id.LinearLayout_DocumentBack)
        LinearLayout LinearLayout_DocumentBack;

//        @BindView(R2.id.TextView_DocumenExtention)
        TextView mTextView_DocumenExtention;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            mImageView_Selection=itemView.findViewById(R.id.ImageView_Selection);
            mLinearLayout_mainLay=itemView.findViewById(R.id.LinearLayout_mainLay);
            mLinearLayout_SelectedItem=itemView.findViewById(R.id.LinearLayout_SelectedItem);
            mLinearLayout_PlayButton=itemView.findViewById(R.id.LinearLayout_PlayButton);
            mImageView_Document_Thumb=itemView.findViewById(R.id.ImageView_Document_Thumb);
            mTextView_DocumenName=itemView.findViewById(R.id.TextView_DocumenName);
            LinearLayout_DocumentBack=itemView.findViewById(R.id.LinearLayout_DocumentBack);
            mTextView_DocumenExtention=itemView.findViewById(R.id.TextView_DocumenExtention);
        }
    }

    public void updateSelectedList(ArrayList<GSFilesPkrModel> selectedFiles){
        mSelectedFiles=selectedFiles;
    }
}
