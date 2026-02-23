// package com.vatek.hrmtool.service.serviceImpl;

// import com.vatek.hrmtool.dto.UserDto.UserRoleDto;
// import com.vatek.hrmtool.entity.neww.RoleEntity;
// import com.vatek.hrmtool.entity.neww.UserEntity;
// import com.vatek.hrmtool.respository.RoleRepository;
// import com.vatek.hrmtool.respository.UserRepository;
// import com.vatek.hrmtool.service.UserRoleService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class UserRoleServiceImpl implements UserRoleService {
//     @Autowired
//     RoleRepository roleRepository;
//     @Autowired
//     UserRepository userRepository;
//     @Override
//     public String grantRoleToUser(UserRoleDto dto, Long id){
//         UserEntity user = userRepository.findUserEntityById(id).orElseThrow(()->new RuntimeException("Không tìm thấy người dùng"));
//         List<RoleEntity> roles = roleRepository.findAllById(dto.getRoleId());
//         user.setRoles(roles);
//         userRepository.save(user);
//         return "Gán role thành công";
//     }
// }
