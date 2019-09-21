package com.videoweb.ying.po;

/**
 * Author lbh
 * Date 2019-07-27
 */
public class OrderType {

    private Integer type;

    private String name;

    public OrderType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
