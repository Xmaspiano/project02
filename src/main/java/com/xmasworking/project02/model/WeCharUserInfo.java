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
public class WeCharUserInfo extends WeCharError {
    /**
     * 用户的唯一标识
     */
    String openid;
    /**
     * 用户昵称
     */
    String nickname;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    String sex;
    /**
     * 用户个人资料填写的省份
     */
    String province;
    /**
     * 普通用户个人资料填写的城市
     */
    String city;
    /**
     * 国家，如中国为CN
     */
    String country;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    String headimgurl;
    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
     */
    String privilege;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    String unionid;
    String language;


}
