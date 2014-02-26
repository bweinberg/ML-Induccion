package com.mercadolibre.dto;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by bweinberg on 18/02/14.
 */
public class Search implements Serializable {

    private String site_id;
    private String query;
    private ArrayList<Item> results;
    private Paging paging;



    public Search(String site_id, String query, ArrayList<Item> results, Paging paging) {

        this.site_id = site_id;
        this.query = query;
        this.results = results;
        this.paging = paging;

    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<Item> getResults() {
        return results;
    }

    public void setResults(ArrayList<Item> results) {
        this.results = results;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }



}
