package com.company.interview.assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
public class TreeDto {

    /**
     * Street Tree Census id
     */
    @JsonProperty("tree_id")
    private long id;

    /**
     * Common name for species, e.g. "red maple"
     */
    @JsonProperty("spc_common")
    private String name;

    /**
     * X coordinate, in state plane. Units are feet.
     * (description from data.cityofnewyork.us)
     */
    @JsonProperty("x_sp")
    private Double xFoot;

    /**
     * Y coordinate, in state plane. Units are feet.
     * (description from data.cityofnewyork.us)
     */
    @JsonProperty("y_sp")
    private Double yFoot;

    public TreeDto() {
    }

    public TreeDto(long id, String name, Double xFoot, Double yFoot) {
        this.id = id;
        this.name = name;
        this.xFoot = xFoot;
        this.yFoot = yFoot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getxFoot() {
        return xFoot;
    }

    public void setxFoot(Double xFoot) {
        this.xFoot = xFoot;
    }

    public Double getyFoot() {
        return yFoot;
    }

    public void setyFoot(Double yFoot) {
        this.yFoot = yFoot;
    }

    @Override
    public String toString() {
        return "TreeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", xFoot=" + xFoot +
                ", yFoot=" + yFoot +
                '}';
    }
}
