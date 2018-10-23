package com.xmasworking.project02.entity;

import com.github.binarywang.java.emoji.EmojiConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/17 - 下午5:24
 * Created by IntelliJ IDEA.
 */
@Entity
@Data
@Table(name = "account")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * 用户的唯一标识
     */
    String openid;
    /**
     * 用户昵称
     */
    String nickname;

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname =  EmojiConverter.getInstance().toHtml(nickname);
    }
}
