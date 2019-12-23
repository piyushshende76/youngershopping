package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child_Home_Model {
    @SerializedName("child_id")
    @Expose
    private String childId;
    @SerializedName("child_name")
    @Expose
    private String childName;
    @SerializedName("child_image")
    @Expose
    private String childImage;


    public Child_Home_Model() {
    }


    public Child_Home_Model(String childId, String childName, String childImage) {
        this.childId = childId;
        this.childName = childName;
        this.childImage = childImage;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildImage() {
        return childImage;
    }

    public void setChildImage(String childImage) {
        this.childImage = childImage;
    }


}
