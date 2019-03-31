package com.xmasworking.project02.repository;

import com.xmasworking.project02.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/8/31 - 下午4:18
 * Created by IntelliJ IDEA.
 */
public interface CodeRepository
        extends JpaRepository<Code, Long> {

    int countByCode(String code);
}
