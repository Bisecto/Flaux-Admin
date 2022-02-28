package com.bisector.flauxad;

public class VideoUploadDetail {
    String VideoSlide,VideoUrl,VideoName,VideoDescription,VideoCategory,VideoCategory2;

    public VideoUploadDetail(String videoSlide, String videoType, String videoThumb, String videoUrl, String videoName, String videoDescription, String videoCategory, String videoCategory2) {
        VideoSlide = videoSlide;
        VideoUrl = videoUrl;
        VideoName = videoName;
        VideoDescription = videoDescription;
        VideoCategory = videoCategory;
        VideoCategory2 = videoCategory2;
    }

    public VideoUploadDetail() {
    }

    public String getVideoSlide() {
        return VideoSlide;
    }

    public void setVideoSlide(String videoSlide) {
        VideoSlide = videoSlide;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoDescription() {
        return VideoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        VideoDescription = videoDescription;
    }

    public String getVideoCategory() {
        return VideoCategory;
    }

    public void setVideoCategory(String videoCategory) {
        VideoCategory = videoCategory;
    }
    public String getVideoCategory2() {
        return VideoCategory2;
    }

    public void setVideoCategory2(String videoCategory2) {
        VideoCategory2 = videoCategory2;
    }
}

