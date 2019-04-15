package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.dto.TreeDto;
import com.holidu.interview.assignment.mapper.TreeMapper;
import com.holidu.interview.assignment.model.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 13.04.2019
 */
@Service
public class TreeDataCensusRestService implements ITreeDataAcquireService {

    private static final Logger LOG = LoggerFactory.getLogger(TreeDataCensusRestService.class);

    private final RestTemplate restTemplate;

    @Value("${cityofnewyork.rest.tree-data}")
    private String treeDataUrl;

    @Autowired
    public TreeDataCensusRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Get tree data from Census enpoint by rest
     * @return list of tree data
     */
    @Override
    public List<Tree> getTreeList() {
        LOG.info(">> getTreeList");
        ResponseEntity<List<TreeDto>> response = restTemplate.exchange(
                treeDataUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TreeDto>>() {}
        );
        List<Tree> trees = TreeMapper.MAPPER.toModels(response.getBody());
        LOG.info("<< getTreeList < trees count = {}", trees.size());
        return trees;
    }
}
