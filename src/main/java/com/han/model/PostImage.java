package com.han.model;

import lombok.Data;

@Data
public class PostImage {
    private Integer id;
    private Integer postId;
    private String url;

    public PostImage(Integer id, Integer postId, String url) {
        this.id = id;
        this.postId = postId;
        this.url = url;
    }
}
