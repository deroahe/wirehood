package com.wirehood.adminservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("AdminRepositoryCustom")
public interface AdminRepositoryCustom {
}
