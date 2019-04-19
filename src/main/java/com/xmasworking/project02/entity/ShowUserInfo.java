package com.xmasworking.project02.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/8/30 - 下午3:24
 * Created by IntelliJ IDEA.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "showuserinfo")
public class ShowUserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String type;
    String department;
    String username;
    String company;
    String age;
    String sex;
    String addworktime;
    String workage;
    String edu;
    String certificate;
    String job;
    String achievement;
    String glory;
}
