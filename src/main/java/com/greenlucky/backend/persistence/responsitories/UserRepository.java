package com.greenlucky.backend.persistence.responsitories;

import com.greenlucky.backend.persistence.domain.backend.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * User repository extends CRUDRepository
 * Created by Mr Lam on 6/11/2016.
 */
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long>{

    /**
     * Returns a User given a username and null if not found
     * @param username
     * @return a User given a username and null if not found
     */
    public User findByUsername(String username);


    /**
     * Returns a User given email and null if not found
     * @param email
     * @return a User
     */
    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.password = :password where u.id = :userId")
    void updateUserPassword(@Param("userId") long userId, @Param("password") String password);
}
