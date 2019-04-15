package com.holidu.interview.assignment.mapper;

import com.holidu.interview.assignment.dto.TreeDto;
import com.holidu.interview.assignment.model.Tree;
import com.holidu.interview.assignment.utils.UnitConverterUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 14.04.2019
 */
public class TreeMapperTest {

    @Test
    public void testToModel() {
        TreeDto treeDto = new TreeDto(1L, "oak", 2d, 3d);
        Tree expectedTree = new Tree(1L, "oak", UnitConverterUtils.convertFeetToMeters(2d), UnitConverterUtils.convertFeetToMeters(3d));

        Tree actualTree = TreeMapper.MAPPER.toModel(treeDto);

        Assert.assertEquals(expectedTree, actualTree);
    }

}
