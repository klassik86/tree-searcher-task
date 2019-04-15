package com.holidu.interview.assignment.mapper;

import com.holidu.interview.assignment.dto.TreeDto;
import com.holidu.interview.assignment.model.Tree;
import com.holidu.interview.assignment.utils.UnitConverterUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * MapStruct Tree converter between dto and domain representations.
 * If the project does not compile here, please enable 'Enable annotation processing'.
 * See MapStruct section in README.md
 *
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
@Mapper
public interface TreeMapper {

    TreeMapper MAPPER = Mappers.getMapper(TreeMapper.class);

    Tree toModel(TreeDto treeDto);

    List<Tree> toModels(List<TreeDto> treeDto);

    @AfterMapping
    default void postTreeMapping(TreeDto treeDto, @MappingTarget Tree tree) {
        /* TreeDataController endpoint gets data in meters so convert feet to meters */
        tree.setX(UnitConverterUtils.convertFeetToMeters(treeDto.getxFoot()));
        tree.setY(UnitConverterUtils.convertFeetToMeters(treeDto.getyFoot()));
    }
}
