package com.company.interview.assignment.model;


/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
public class Tree {

    /**
     * Street Tree Census id
     */
    private Long id;

    /**
     * Common name for species, e.g. "red maple"
     */
    private String name;

    /**
     * X coordinate, in state plane. Units are meters.
     */
    private Double x;

    /**
     * Y coordinate, in state plane. Units are meters.
     */
    private Double y;

    public Tree() {
    }

    public Tree(Long id, String name, Double x, Double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Tree(Double x, Double y) {
        this(null, null, x, y);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    /**
     * Calculate whether the point belong to the circle
     * @param xc x coordinate of the center
     * @param yc y coordinate of the center
     * @param r radius
     * @return true - the point belongs the circle, false - otherwise
     */
    public boolean isBelong(Double xc, Double yc, Double r) {
        return Math.sqrt(Math.pow(xc - x, 2) + Math.pow(yc - y, 2)) <= r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tree tree = (Tree) o;

        if (id != tree.id) {
            return false;
        }
        if (name != null ? !name.equals(tree.name) : tree.name != null) {
            return false;
        }
        if (x != null ? !x.equals(tree.x) : tree.x != null) {
            return false;
        }
        return y != null ? y.equals(tree.y) : tree.y == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

}
