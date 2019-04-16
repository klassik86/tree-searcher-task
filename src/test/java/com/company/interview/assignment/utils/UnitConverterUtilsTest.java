package com.company.interview.assignment.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
public class UnitConverterUtilsTest {

    @Test
    public void testConvertFeetToMeters() {
        Assert.assertEquals(new BigDecimal("1.000"),
                BigDecimal.valueOf(UnitConverterUtils.convertFeetToMeters(3.2808d)).setScale(3, BigDecimal.ROUND_HALF_UP));
        Assert.assertEquals(new BigDecimal("0.000"),
                BigDecimal.valueOf(UnitConverterUtils.convertFeetToMeters(0d)).setScale(3, BigDecimal.ROUND_HALF_UP));
    }

}
