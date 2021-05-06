package Utils.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class FolderFileModel implements Serializable {
    String folderName;

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    String folderPath;
    ArrayList<GSFilesPkrModel> files;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<GSFilesPkrModel> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<GSFilesPkrModel> files) {
        this.files = files;
    }
}
