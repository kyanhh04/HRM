//package com.vatek.hrmtool.entity.neww;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.vatek.hrmtool.entity.common.CommonEntity;
//import com.vatek.hrmtool.enumeration.Role;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.Collection;
//
//@Entity
//@Table(name = "roles")
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class RoleEntity extends CommonEntity {
//    @Enumerated(EnumType.STRING)
//    private Role role;
//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    private Collection<UserEntity> users;
//}
