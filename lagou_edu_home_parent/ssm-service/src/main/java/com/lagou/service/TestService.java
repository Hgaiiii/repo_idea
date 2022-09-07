package com.lagou.service;

import com.lagou.domain.Test;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TestService {

    public List<Test> findAll();
}
