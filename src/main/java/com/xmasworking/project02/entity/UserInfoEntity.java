package com.xmasworking.project02.entity;

import com.github.binarywang.java.emoji.EmojiConverter;
import com.xmasworking.project02.model.WeCharUserInfo;
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
@Table(name = "userinfo")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class UserInfoEntity extends WeCharUserInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Override
    public void setNickname(String nickname) {
        super.setNickname(EmojiConverter.getInstance().toHtml(nickname));
    }
}
