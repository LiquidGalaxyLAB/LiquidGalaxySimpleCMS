package com.lglab.diego.simple_cms.web_scraping.data;

public enum Constant {
    INFO_WEB_SCRAPING(0), TECH_CONFERENCES_SPAIN(1);

    private final int id;

    Constant(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
