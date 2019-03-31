package com.xmasworking.project02.repository;

import com.xmasworking.project02.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2019/3/31 - 3:57 PM
 * Created by IntelliJ IDEA.
 */
public interface UserInfoRepositiory
        extends JpaRepository<UserInfoEntity,Long> {

}
