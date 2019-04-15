package com.holidu.interview.assignment.service;

import java.util.Map;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 14.04.2019
 */
public interface ITreeDataSearchService {

    Map<String, Integer> findTreesCount(Double xc, Double yc, Double r);
}
