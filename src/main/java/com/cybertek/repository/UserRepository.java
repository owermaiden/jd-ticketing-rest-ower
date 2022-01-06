package com.cybertek.repository;

import com.cybertek.entity.Role;
import com.cybertek.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String username);
    List<User> findAllByRoleDescriptionIgnoreCase(String description);

    @Transactional
    void deleteByUserName(String username);
}
