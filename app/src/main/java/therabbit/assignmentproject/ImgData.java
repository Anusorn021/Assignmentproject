package therabbit.assignmentproject;

/**
 * Created by Nutherabbit on 10/7/2560.
 */

public class ImgData  {

    int imd_id;
    String img_path;
    String type;

    byte bb[];

    public int getImd_id() {
        return imd_id;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImd_id(int imd_id) {
        this.imd_id = imd_id;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public byte[] getBb() {
        return bb;
    }

    public void setBb(byte[] bb) {
        this.bb = bb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
