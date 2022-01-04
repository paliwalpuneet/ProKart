package com.example.mobiles.Data;

public class BrandAdapterDetails {

   private int brandImage;
   private String brandButtonText;

    public BrandAdapterDetails() {
    }

    public BrandAdapterDetails(int brandImage, String brandButtonText) {
        this.brandImage = brandImage;
        this.brandButtonText = brandButtonText;
    }

    public int getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(int brandImage) {
        this.brandImage = brandImage;
    }

    public String getBrandButtonText() {
        return brandButtonText;
    }

    public void setBrandButtonText(String brandButtonText) {
        this.brandButtonText = brandButtonText;
    }
}
