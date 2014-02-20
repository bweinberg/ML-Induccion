package com.mercadolibre.dto;

import java.io.Serializable;

/**
 * Created by bweinberg on 20/02/14.
 */
public class Picture implements Serializable{

    private String id;
    private String url;

    public Picture(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}


