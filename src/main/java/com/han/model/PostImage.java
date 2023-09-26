package com.han.model;

import lombok.Data;

@Data
public class PostImage {
    private Integer id;
    private Integer postId;
    private String url;
    private int imageOrder;

    public PostImage(Integer id) {
        this.id = id;
    }

    public PostImage(Integer postId, String url, int imageOrder) {
        this.postId = postId;
        this.url = url;
        this.imageOrder = imageOrder;
    }

    public PostImage(Integer id, Integer postId, String url, int imageOrder) {
        this.id = id;
        this.postId = postId;
        this.url = url;
        this.imageOrder = imageOrder;
    }
}
