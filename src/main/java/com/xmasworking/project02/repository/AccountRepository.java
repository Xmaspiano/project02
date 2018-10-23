package com.xmasworking.project02.repository;

import com.xmasworking.project02.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/17 - 下午5:26
 * Created by IntelliJ IDEA.
 */
public interface AccountRepository
        extends JpaRepository<Account, Long> {
}
