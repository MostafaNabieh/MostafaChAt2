package com.example.mostafachat;

import de.hdodenhof.circleimageview.CircleImageView;

public class all_users_model {
    private String Name;
    private String status;
    private String image;
    private String Image_thumb;

    public all_users_model() {

    }


    public all_users_model(String name, String status,String image) {
        this.Name = name;
        this.status = status;
        this.Image_thumb=image;
    }



    public String getImage_thumb() {
        return Image_thumb;
    }

    public void setImage_thumb(String image) {
        this.Image_thumb = image;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
