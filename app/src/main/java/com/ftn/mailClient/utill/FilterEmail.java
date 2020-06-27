package com.ftn.mailClient.utill;

public class FilterEmail {
    private String searchCriteria;

    public FilterEmail(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
