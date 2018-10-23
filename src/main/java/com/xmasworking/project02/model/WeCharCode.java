package com.xmasworking.project02.model;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/17 - 下午4:03
 * Created by IntelliJ IDEA.
 */
@Data
public class WeCharCode extends WeCharError {
    String access_token;
    String expires_in;
    String refresh_token;
    String openid;
    String scope;
}
