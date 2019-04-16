package com.company.interview.assignment.utils;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
public class UnitConverterUtils {

    private static final Double METERS_TO_FEET_MULT = 3.2808;

    /**
     * Convert from unit feet to unit meters
     * @param feet count of feet
     * @return meters
     */
    public static Double convertFeetToMeters(Double feet) {
        return feet / METERS_TO_FEET_MULT;
    }

}
