package com.holidu.interview.assignment.controller;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 14.04.2019
 */
public class TreeDataControllerTest {

    @Test
    public void testCoordValidation() {
        Pattern pattern = Pattern.compile(TreeDataController.POSITIVE_DOUBLE_REGEXP);

        Assert.assertTrue(pattern.matcher("10").matches());
        Assert.assertTrue(pattern.matcher("10.1").matches());
        Assert.assertTrue(pattern.matcher("1.1").matches());
        Assert.assertTrue(pattern.matcher("12345.12345").matches());

        Assert.assertFalse(pattern.matcher("0").matches());
        Assert.assertFalse(pattern.matcher("-1").matches());
        Assert.assertFalse(pattern.matcher("1.55.55").matches());
        Assert.assertFalse(pattern.matcher("1,55").matches());
        Assert.assertFalse(pattern.matcher("1.").matches());
    }

}
