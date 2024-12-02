package com.example.springboot.entity;


import cn.hutool.core.annotation.Alias;

import javax.persistence.*;

@Table(name = "user")
public class User  extends Account {


        /**
         * 所属班级
         */
        @Alias("班级Id")
        @Column(name = "gradeId")
        private Integer gradeId;

        @Transient
        private String gradeName;



    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }
    /**
     * 班级名
     */

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}


