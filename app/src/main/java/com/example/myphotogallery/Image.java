package com.example.myphotogallery;

import java.io.Serializable;

public class Image implements Serializable {
    public String Id;
    public String URL;

    public Image(String id, String url)
    {
        Id = id;
        URL = url;
    }
}

