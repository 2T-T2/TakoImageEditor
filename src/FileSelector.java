
import java.awt.FileDialog;
import java.awt.Frame;

public class FileSelector {
    final FileDialog fd;
    FileSelector(Frame parent) { fd = new FileDialog(parent); }
    public void show(int mode) {
        fd.setMode( mode );
        fd.setVisible(true);
    }
    public String getFileName() {
        String fileName = fd.getDirectory()+fd.getFile();
        return fileName;
    }
}