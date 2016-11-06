package com.greenlucky.backend.persistence.responsitories;

import com.greenlucky.backend.persistence.domain.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User repository extends CRUDRepository
 * Created by Mr Lam on 6/11/2016.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>{

}
