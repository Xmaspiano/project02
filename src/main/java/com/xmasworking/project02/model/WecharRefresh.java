package com.xmasworking.project02.model;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2019/3/31 - 2:16 PM
 * Created by IntelliJ IDEA.
 */
@Data
public class WecharRefresh {
    String access_token;
    Integer expires_in;
    String refresh_token;
    String openid;
    String scope;
}
