package com.company.interview.assignment.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
public class TreeTest {

    @Test
    public void isBelong() {
        Tree tree = new Tree(0d, 0d);

        Assert.assertTrue(tree.isBelong(0d, 0d, 1d));
        Assert.assertTrue(tree.isBelong(-1d, 0d, 1d));
        Assert.assertTrue(tree.isBelong(-1d, -1d, 1.415d));

        Assert.assertFalse(tree.isBelong(-1d, -1d, 1d));
        Assert.assertFalse(tree.isBelong(-1d, -1d, 1.414d));
    }

}
