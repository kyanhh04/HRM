package com.vatek.hrmtool.entity;


import com.vatek.hrmtool.entity.common.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image extends CommonEntity {
    @Column
    private String name;
    @Column
    private String src;
}
