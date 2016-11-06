package com.greenlucky.backend.persistence.responsitories;

import com.greenlucky.backend.persistence.domain.backend.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Role repository extends CrudResponsitory
 * Created by Mr Lam on 6/11/2016.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{
}
