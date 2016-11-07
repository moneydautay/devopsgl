package com.greenlucky.backend.persistence.responsitories;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToke;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@Repository
public interface PasswordResetTokeRepository extends CrudRepository<PasswordResetToke, Long>{

    PasswordResetToke findByToken(String token);

    @Query("select ptr from PasswordResetToken ptr inner join ptr.user u where ptr.user.id = ?1")
    Set<PasswordResetToke> findAllByUserId(Long userId);
}
