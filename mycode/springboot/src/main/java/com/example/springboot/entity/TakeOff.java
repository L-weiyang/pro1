package com.example.springboot.entity;

import javax.persistence.*;

@Table(name = "takeoff")
public class TakeOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "userId")
    private Integer userId;

    /**
     * 请假人名称
     */
    @Column(name = "userName")
    private String userName;

    /**
     * 请假人缘由
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 审核结果
     */
    @Column(name = "examine")
    private String examine;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }
}
