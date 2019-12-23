package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDailog {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("mrp")
    @Expose
    private String mrp;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("stock")
    @Expose
    private String stock;
    @SerializedName("Count")
    @Expose
    private String count;
    @SerializedName("PCount")
    @Expose
    private String pCount;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("galaries")
    @Expose
    private List<Galary> galaries = null;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPCount() {
        return pCount;
    }

    public void setPCount(String pCount) {
        this.pCount = pCount;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public List<Galary> getGalaries() {
        return galaries;
    }

    public void setGalaries(List<Galary> galaries) {
        this.galaries = galaries;
    }

    public class Galary {

        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("photo")
        @Expose
        private String photo;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

}



}
