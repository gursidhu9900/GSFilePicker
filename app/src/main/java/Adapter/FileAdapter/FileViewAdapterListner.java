package Adapter.FileAdapter;

import Utils.Models.GSFilesPkrModel;

public interface FileViewAdapterListner {
    void selectFile(GSFilesPkrModel filesModel, int position);
    void unSelectFile(GSFilesPkrModel filesModel, int position);
}
