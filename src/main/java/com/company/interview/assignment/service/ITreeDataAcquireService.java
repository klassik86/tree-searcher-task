package com.company.interview.assignment.service;

import com.company.interview.assignment.model.Tree;

import java.util.List;

/**
 * @author Dudin Pavel | dudinpa86@gmail.com
 * 14.04.2019
 */
public interface ITreeDataAcquireService {

    /**
     * Get tree data
     * @return
     */
    List<Tree> getTreeList();
}
