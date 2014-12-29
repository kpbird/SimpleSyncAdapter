package com.kpbird.simplesyncadapter.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ketan.parmar on 11/4/2014.
 */
public class ToDoModel {
    private int id;
    private String title;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
