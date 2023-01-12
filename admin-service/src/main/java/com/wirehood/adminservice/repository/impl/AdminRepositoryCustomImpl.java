package com.wirehood.adminservice.repository.impl;

import com.wirehood.adminservice.repository.AdminRepositoryCustom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class AdminRepositoryCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;
}
