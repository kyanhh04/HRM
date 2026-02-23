package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config extends CommonEntity {

    @Column(name = "`key`")
    private String key;

    @Column(name = "`value`")
    private String value;
}
