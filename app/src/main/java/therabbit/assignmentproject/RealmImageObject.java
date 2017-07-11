package therabbit.assignmentproject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Nutherabbit on 11/7/2560.
 */

public class RealmImageObject extends RealmObject{

    @PrimaryKey
    int imd_id;

    String img_path;
    String type;
    byte bitByte[];

    public int getImd_id() {
        return imd_id;
    }

    public String getImg_path() {
        return img_path;
    }

    public String getType() {
        return type;
    }

    public byte[] getbitByte() {
        return bitByte;
    }

    public void setImd_id(int imd_id) {
        this.imd_id = imd_id;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setbitByte(byte[] bitByte) {
        this.bitByte = bitByte;
    }
}
