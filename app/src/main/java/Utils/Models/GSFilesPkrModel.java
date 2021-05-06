package Utils.Models;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

public class GSFilesPkrModel implements Serializable{
    String file_name;
    String file_path;
    String file_extention;
    //File file;
    String fileType;
    // Uri uri;

  /*  public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }*/

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /* public File getFile() {
         return file;
     }

     public void setFile(File file) {
         this.file = file;
     }
 */
    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_extention() {
        return file_extention;
    }

    public void setFile_extention(String file_extention) {
        this.file_extention = file_extention;
    }
}
