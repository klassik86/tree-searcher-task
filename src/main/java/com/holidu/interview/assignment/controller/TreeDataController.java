package com.holidu.interview.assignment.controller;

import com.holidu.interview.assignment.service.ITreeDataSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
public class TreeDataController {

    private static final Logger LOG = LoggerFactory.getLogger(TreeDataController.class);
    private final ITreeDataSearchService treeDataSearchService;

    final static String POSITIVE_DOUBLE_REGEXP = "([1-9][0-9]*)(\\.[0-9]+)?";

    @Autowired
    public TreeDataController(ITreeDataSearchService treeDataSearchService) {
        this.treeDataSearchService = treeDataSearchService;
    }

    @RequestMapping(
            name = "retrieveTreezByZoneEndpoint",
            method = RequestMethod.GET,
            value = "/tree/count",
            produces = "application/json"
    )
    public Map<String, Integer> retrieveTreezByZone(
            @Valid @Pattern(regexp = POSITIVE_DOUBLE_REGEXP + ";" + POSITIVE_DOUBLE_REGEXP,
                    message = "Coordinates must be positive and divided by semicolons, for example: 10.5;30")
            @RequestParam("coord") String coordParam,
            @RequestParam("radius") @Min(0) Double radiusParam) {

        LOG.debug(">> retrieveTreezByZone > coordParam = {}, radiusParam = {}", coordParam, radiusParam);

        List<Double> coordinates = Arrays.stream(coordParam.split(";"))
                .map(Double::new)
                .collect(Collectors.toList());
        Map<String, Integer> treesCount = treeDataSearchService.findTreesCount(coordinates.get(0), coordinates.get(1), radiusParam);

        LOG.debug("<< retrieveTreezByZone");
        return treesCount;
    }

}
