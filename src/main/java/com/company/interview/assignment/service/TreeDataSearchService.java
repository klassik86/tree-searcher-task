package com.company.interview.assignment.service;

import com.company.interview.assignment.model.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
@Service
public class TreeDataSearchService implements ITreeDataSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(TreeDataSearchService.class);

    private final ITreeDataAcquireService dataRestService;

    /**
     * List of trees sorted by x
     */
    private List<Tree> xSortedTrees;
    /**
     * Array of trees sorted by x. It needs for binary search. Creating only one instance is better for performance.
     */
    private Tree[] xSortedTreeArray;

    @Autowired
    public TreeDataSearchService(ITreeDataAcquireService dataRestService) {
        this.dataRestService = dataRestService;
    }

    @PostConstruct
    public void init() {
        List<Tree> treeList = dataRestService.getTreeList();
        cleanNotValidData(treeList);
        xSortedTrees = treeList.stream()
                .sorted(Comparator.comparingDouble(Tree::getX))
                .collect(Collectors.toList());
        xSortedTreeArray = xSortedTrees.toArray(new Tree[]{});
    }

    /**
     * Find trees which belongs to the circle
     * @param xc x coordinate of the center
     * @param yc y coordinate of the center
     * @param r radius
     * @return Map<String, Integer> key - name of a tree, value - count of trees with this name in the circle
     */
    @Override
    public Map<String, Integer> findTreesCount(Double xc, Double yc, Double r) {
        LOG.trace(">> findTreesCount > xc = {}, yc = {}, r = {}", xc, yc, r);
        List<Tree> trees = findTreesByCutoffAlgorithm(xc, yc, r);
        Map<String, Integer> result = trees.stream()
                .collect(Collectors.groupingBy(Tree::getName))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
        LOG.trace("<< findTreesCount");
        return result;
    }

    /**
     * Simple Traverse Algorithm. It traverses all points and checks the distance from a point to the center.
     * @param xc x coordinate of the center
     * @param yc y coordinate of the center
     * @param r radius
     * @return list of trees belonging to the circle
     */
    List<Tree> findTreesByTraverseAlgorithm(Double xc, Double yc, Double r) {
        List<Tree> result = new ArrayList<>();
        for (Tree tree : xSortedTrees) {
            if (tree.isBelong(xc, yc, r)) {
                result.add(tree);
            }
        }
        return result;
    }

    /**
     * Cutoff Algorithm. You can find the description in README.md in 'Cutoff Algorithm (CA)' section.
     *
     * @param xc x coordinate of the center
     * @param yc y coordinate of the center
     * @param r radius
     * @return list of trees belonging to the circle
     */
    List<Tree> findTreesByCutoffAlgorithm(Double xc, Double yc, Double r) {
        Tree xMinTree = new Tree(xc - r, 0d);
        Tree xMaxTree = new Tree(xc + r, 0d);

        int xMinIndex = binarySearchMinIndex(xMinTree, xSortedTreeArray, Tree::getX);
        int xMaxIndex = binarySearchMaxIndex(xMaxTree, xSortedTreeArray, Tree::getX);
        LOG.trace("xMinIndex = {}, xMaxIndex = {}", xMinIndex, xMaxIndex);

        List<Tree> result = new ArrayList<>();
        for (int i = xMinIndex; i < xMaxIndex; i++) {
            Tree tree = xSortedTrees.get(i);
            if ((yc - r <= tree.getY() && tree.getY() <= yc + r)
                    && tree.isBelong(xc, yc, r)) {
                result.add(tree);
            }
        }
        return result;
    }

    int binarySearchMinIndex(Tree minTree, Tree[] sortedTreeArray, Function<Tree, Double> comparingField) {
        int result = Arrays.binarySearch(sortedTreeArray, minTree, Comparator.comparingDouble(comparingField::apply));
        return result < 0 ? -(result + 1) : result;
    }

    int binarySearchMaxIndex(Tree maxTree, Tree[] sortedTreeArray, Function<Tree, Double> comparingField) {
        int result = Arrays.binarySearch(sortedTreeArray, maxTree, Comparator.comparingDouble(comparingField::apply));
        return result < 0 ? -result - 1 : result + 1;
    }

    /**
     * Some data from Census contains 'null' names, so clean defect trees
     * @param treeList correct tree list
     */
    void cleanNotValidData(List<Tree> treeList) {
        List<Tree> nameAbsent = new ArrayList<>();
        for (Tree tree : treeList) {
            if (tree.getName() == null) {
                nameAbsent.add(tree);
            }
        }

        if (nameAbsent.isEmpty()) {
            LOG.info("All data is correct");
        } else {
            LOG.info("There are data whithout names: ids = {}, count = {}. These data will be deleted.",
                    getIdsFromTreeList(nameAbsent), nameAbsent.size());
            treeList.removeAll(nameAbsent);
        }
    }

    private List<Long> getIdsFromTreeList(List<Tree> nameAbsent) {
        return nameAbsent.stream().map(Tree::getId).collect(Collectors.toList());
    }

}
