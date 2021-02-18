package com.asset.appwork.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class GenericService {
    public abstract  <T> List<T> updateResult(List<T> result);
}
