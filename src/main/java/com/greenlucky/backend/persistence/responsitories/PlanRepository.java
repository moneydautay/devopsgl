package com.greenlucky.backend.persistence.responsitories;

import com.greenlucky.backend.persistence.domain.backend.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Plan repository extends CrudReponsitory
 * Created by Mr Lam on 6/11/2016.
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer>{
}
