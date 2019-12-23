package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerCartModel {

    @SerializedName("sub_id")
    @Expose
    private String subId;
    @SerializedName("sub_name")
    @Expose
    private String subName;
    @SerializedName("sub_image")
    @Expose
    private String subImage;

    public BannerCartModel() {
    }

    public BannerCartModel(String subId, String subName, String subImage) {
        this.subId = subId;
        this.subName = subName;
        this.subImage = subImage;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }
}
