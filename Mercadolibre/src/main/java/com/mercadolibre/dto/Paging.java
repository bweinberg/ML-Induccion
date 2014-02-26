package com.mercadolibre.dto;

import java.io.Serializable;

/**
 * Created by bweinberg on 20/02/14.
 */
public class Paging implements Serializable {

    private String total;
    private String offset;
    private int limit;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public Paging(String total, String offset, int limit) {
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }


}

