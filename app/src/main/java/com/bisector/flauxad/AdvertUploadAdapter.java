package com.bisector.flauxad;

public class AdvertUploadAdapter {
    String Name,Description,Url,Email,AdvertPicUrl;



    public AdvertUploadAdapter() {
    }

    public AdvertUploadAdapter(String name, String description, String url, String email,String advertPicUrl) {
        Name = name;
        Description = description;
        Url = url;
        Email = email;
        AdvertPicUrl=advertPicUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public String getAdvertPicUrl() {
        return AdvertPicUrl;
    }

    public void setAdvertPicUrl(String advertPicUrl) {
        AdvertPicUrl = advertPicUrl;
    }
}
