package com.company.interview.assignment.service;

import com.company.interview.assignment.model.Tree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeDataSearchServiceTest {

    @Mock
    private TreeDataCensusRestService treeDataRestService;

    @Test
    public void testFindTreesCount() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1L, "red maple", 1d, 1d));
        trees.add(new Tree(2L, "American linden",2d, 2d));
        trees.add(new Tree(3L, "London planetree",3d, 5d));
        trees.add(new Tree(4L, "red maple",5d, 3d));
        when(treeDataRestService.getTreeList()).thenReturn(trees);
        TreeDataSearchService service = spy(new TreeDataSearchService(treeDataRestService));
        doReturn(trees).when(service).findTreesByCutoffAlgorithm(any(), any(), any());

        service.init();
        Map<String, Integer> treesCount = service.findTreesCount(0d, 0d, 10d);

        Assert.assertEquals(3, treesCount.size());
        Assert.assertEquals(2, treesCount.get("red maple").intValue());
        Assert.assertEquals(1, treesCount.get("American linden").intValue());
        Assert.assertEquals(1, treesCount.get("London planetree").intValue());
    }

    @Test
    public void testFindTreesByTraverseAlgorithm() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1L, "", 1d, 1d));
        trees.add(new Tree(2L, "",2d, 2d));
        trees.add(new Tree(3L, "",3d, 5d));
        trees.add(new Tree(4L, "",5d, 3d));
        when(treeDataRestService.getTreeList()).thenReturn(trees);
        TreeDataSearchService service = new TreeDataSearchService(treeDataRestService);

        service.init();

        Assert.assertEquals(Collections.emptyList(), extractIds(service.findTreesByTraverseAlgorithm(0d, 0d, 0.5d)));
        Assert.assertEquals(Collections.singletonList(1L), extractIds(service.findTreesByTraverseAlgorithm(0d, 0d, 1.42d)));
        Assert.assertEquals(Arrays.asList(1L, 2L), extractIds(service.findTreesByTraverseAlgorithm(1d, 1d, 1.42d)));
        Assert.assertEquals(Arrays.asList(3L, 4L), extractIds(service.findTreesByTraverseAlgorithm(4d, 4d, 1.42)));

        Assert.assertEquals(Collections.singletonList(4L), extractIds(service.findTreesByTraverseAlgorithm(6d, 3d, 1d)));
        Assert.assertEquals(Collections.emptyList(), extractIds(service.findTreesByTraverseAlgorithm(6d, 3d, 0.9d)));
        Assert.assertEquals(Collections.singletonList(3L), extractIds(service.findTreesByTraverseAlgorithm(2d, 7d, 3d)));
        Assert.assertEquals(Arrays.asList(1L, 2L, 3L, 4L), extractIds(service.findTreesByTraverseAlgorithm(1d, 1d, 10d)));
    }

    @Test
    public void testFindTreesByCutoffAlgorithm() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1L, "", 1d, 1d));
        trees.add(new Tree(2L, "",2d, 2d));
        trees.add(new Tree(3L, "",3d, 5d));
        trees.add(new Tree(4L, "",5d, 3d));
        when(treeDataRestService.getTreeList()).thenReturn(trees);
        TreeDataSearchService service = new TreeDataSearchService(treeDataRestService);

        service.init();

        Assert.assertEquals(Collections.emptyList(), extractIds(service.findTreesByCutoffAlgorithm(0d, 0d, 0.5d)));
        Assert.assertEquals(Collections.singletonList(1L), extractIds(service.findTreesByCutoffAlgorithm(0d, 0d, 1.42d)));
        Assert.assertEquals(Arrays.asList(1L, 2L), extractIds(service.findTreesByCutoffAlgorithm(1d, 1d, 1.42d)));
        Assert.assertEquals(Arrays.asList(3L, 4L), extractIds(service.findTreesByCutoffAlgorithm(4d, 4d, 1.42)));

        Assert.assertEquals(Collections.singletonList(4L), extractIds(service.findTreesByCutoffAlgorithm(6d, 3d, 1d)));
        Assert.assertEquals(Collections.emptyList(), extractIds(service.findTreesByCutoffAlgorithm(6d, 3d, 0.9d)));
        Assert.assertEquals(Collections.singletonList(3L), extractIds(service.findTreesByCutoffAlgorithm(2d, 7d, 3d)));
        Assert.assertEquals(Arrays.asList(1L, 2L, 3L, 4L), extractIds(service.findTreesByCutoffAlgorithm(1d, 1d, 10d)));
    }

    @Test
    public void testBinarySearchMinIndex() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1d, 0d));
        trees.add(new Tree(2d, 0d));
        trees.add(new Tree(3d, 0d));
        trees.add(new Tree(4d, 0d));
        trees.add(new Tree(5d, 0d));
        trees.sort(Comparator.comparingDouble(Tree::getX));
        Tree[] treeArrays = trees.toArray(new Tree[]{});
        TreeDataSearchService service = new TreeDataSearchService(null);

        Assert.assertEquals(0, service.binarySearchMinIndex(new Tree(0.5d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(1, service.binarySearchMinIndex(new Tree(1.5d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(3, service.binarySearchMinIndex(new Tree(3.7d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(0, service.binarySearchMinIndex(new Tree(1d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(3, service.binarySearchMinIndex(new Tree(4d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(5, service.binarySearchMinIndex(new Tree(7d, 0d), treeArrays, Tree::getX));
    }

    @Test
    public void testBinarySearchMaxIndex() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1d, 0d));
        trees.add(new Tree(2d, 0d));
        trees.add(new Tree(3d, 0d));
        trees.add(new Tree(4d, 0d));
        trees.add(new Tree(5d, 0d));
        trees.sort(Comparator.comparingDouble(Tree::getX));
        Tree[] treeArrays = trees.toArray(new Tree[]{});
        TreeDataSearchService service = new TreeDataSearchService(null);

        Assert.assertEquals(0, service.binarySearchMaxIndex(new Tree(0.5d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(1, service.binarySearchMaxIndex(new Tree(1.5d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(3, service.binarySearchMaxIndex(new Tree(3.7d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(1, service.binarySearchMaxIndex(new Tree(1d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(4, service.binarySearchMaxIndex(new Tree(4d, 0d), treeArrays, Tree::getX));
        Assert.assertEquals(5, service.binarySearchMaxIndex(new Tree(7d, 0d), treeArrays, Tree::getX));
    }

    @Test
    public void testCleanNotValidData() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(1L, "oak", 2d, 0d));
        trees.add(new Tree(2L, null, 1d, 0d));
        TreeDataSearchService service = new TreeDataSearchService(null);

        service.cleanNotValidData(trees);

        Assert.assertEquals(1, trees.size());
        Assert.assertEquals(1L, trees.get(0).getId().longValue());
    }

    private List<Long> extractIds(List<Tree> trees) {
        return trees.stream()
                .map(Tree::getId)
                .collect(Collectors.toList());
    }
}
