package com.xmasworking.project02.repository;

import com.xmasworking.project02.entity.UseCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/8/31 - 下午4:18
 * Created by IntelliJ IDEA.
 */
public interface UseCodeRepository
        extends JpaRepository<UseCode, Long> {

    /**
     * 查询code的情况
     * @param openid
     * @return
     */
    UseCode findOneByOpenid(String openid);

    int countByOpenidAndStatus(String openid,Integer status);
    int countByCodeAndStatus(String code,Integer status);
}
