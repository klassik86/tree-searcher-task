package com.company.interview.assignment.service;

import com.company.interview.assignment.model.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
public class TreeDataSearchServicePerfomanceBenchmark {

    private static final Logger LOG = LoggerFactory.getLogger(TreeDataSearchServicePerfomanceBenchmark.class);

    private static final Random RANDOM = new Random();
    private static final Integer MAX_EXP = 10_000;
    private static final Integer MAX_X = 100_000;
    private static final Integer MAX_Y = 100_000;
    private static final Double CENTER_X = 50_000d;
    private static final Double CENTER_Y = 50_000d;
    private static List<Integer> TREE_COUNT_LIST = Arrays.asList(1_000);
//    private static List<Integer> TREE_COUNT_LIST = Arrays.asList(1_000, 10_000, 20_000, 100_000);
    private static List<Double> RADIUS_LIST = Arrays.asList(1_000d);
//    private static List<Double> RADIUS_LIST = Arrays.asList(10d, 100d, 1_000d, 10_000d, 25_000d, 40_000d, 50_000d, 75_000d, 100_000d);

    public static void main(String[] args) {
        TreeDataSearchServicePerfomanceBenchmark benchmark = new TreeDataSearchServicePerfomanceBenchmark();
        benchmark.process();
    }

    private void process() {
        double[][][] results = new double[TREE_COUNT_LIST.size()][RADIUS_LIST.size()][2];
        for (int t = 0; t < TREE_COUNT_LIST.size(); t++) {
            List<Tree> trees = generateTrees(TREE_COUNT_LIST.get(t));

            for (int r = 0; r < RADIUS_LIST.size(); r++) {
                for (int alg = 0; alg < 2; alg++) {
                    TreeDataSearchService service = new TreeDataSearchService(() -> trees);
                    service.init();
                    results[t][r][alg] = runAlgorithm(service, r, alg);
                }
            }
        }
        for (int t = 0; t < TREE_COUNT_LIST.size(); t++) {
            LOG.debug("There are experiments results.\nTree count = {}:\n{}", TREE_COUNT_LIST.get(t), printStat(results[t]));
        }
    }

    private double runAlgorithm(TreeDataSearchService service, int r, int alg) {
        LOG.debug("runAlgorithm >> r = {}, alg = {}", r, alg);
        long startMs = System.currentTimeMillis();
        List<Tree> result = new ArrayList<>();
        for (int exp = 0; exp < MAX_EXP; exp++) {
            if (alg == 0) {
                result = service.findTreesByCutoffAlgorithm(CENTER_X, CENTER_Y, RADIUS_LIST.get(r));
            } else {
                result = service.findTreesByTraverseAlgorithm(CENTER_X, CENTER_Y, RADIUS_LIST.get(r));
            }
        }
        double time = (double) (System.currentTimeMillis() - startMs) / 1_000;
        LOG.debug("runAlgorithm << time = {}, treeCount = {}", time, result.size());
        return time;
    }

    /**
     * Print pretty statistics of experiments
     * @param result experiments data
     * @return statistics
     */
    private String printStat(double[][] result) {
        String emptyField = "          ";
        StringBuilder sb = new StringBuilder();
        sb.append("radius:                 \t");
        for (int r = 0; r < RADIUS_LIST.size(); r++) {
            sb.append(getConstantLengthString(String.valueOf(RADIUS_LIST.get(r)), emptyField)).append("\t");
        }
        for (int alg = 0; alg < 2; alg++) {
            sb.append("\n");
            if (alg == 0) {
                sb.append("cutoffAlgorithm (sec):  \t");
            } else {
                sb.append("traverseAlgorithm (sec):\t");
            }
            for (int r = 0; r < RADIUS_LIST.size(); r++) {
                String value = getConstantLengthString(String.valueOf(result[r][alg]), emptyField);
                sb.append(value).append("\t");
            }
        }
        return sb.toString();
    }

    /**
     * Create new String with size 'emptyField.length' from 'value' filled by spaces
     */
    private String getConstantLengthString(String value, String emptyField) {
        value = value + emptyField;
        if (value.length() > emptyField.length()) {
            value = value.substring(0, emptyField.length());
        }
        return value;
    }

    /**
     * Generate a list of random trees
     * @param treeCount count of trees
     * @return the list of random trees
     */
    private List<Tree> generateTrees(int treeCount) {
        LOG.debug(">> generateTrees");
        List<Tree> trees = new ArrayList<>();
        for (int i = 0; i < treeCount; i++) {
            Tree tree = new Tree();
            tree.setId(i + 1L);
            tree.setName("_"); /*does not matter in this benchmark*/
            tree.setX(RANDOM.nextDouble() * MAX_X);
            tree.setY(RANDOM.nextDouble() * MAX_Y);
            trees.add(tree);
        }
        LOG.debug("<< generateTrees");
        return trees;
    }

}
