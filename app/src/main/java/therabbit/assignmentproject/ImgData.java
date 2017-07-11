package therabbit.assignmentproject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Nutherabbit on 10/7/2560.
 */

public class ImgData extends RealmObject {
    @PrimaryKey
    int imd_id;

    @Required
    String img_path;

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
}
