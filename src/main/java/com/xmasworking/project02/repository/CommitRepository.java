package com.xmasworking.project02.repository;

import com.xmasworking.project02.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/8/31 - 下午4:18
 * Created by IntelliJ IDEA.
 */
public interface CommitRepository
        extends JpaRepository<Commit, Long> {

    int countByOpenid(String openid);

    int countByShowid(Long showid);
}
