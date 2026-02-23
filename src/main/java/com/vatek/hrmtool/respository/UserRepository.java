//package com.vatek.hrmtool.respository;
//
//import com.vatek.hrmtool.entity.neww.UserEntity;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//
//public interface UserRepository extends JpaRepository<UserEntity, Long> , JpaSpecificationExecutor<UserEntity> {
//
//    Optional<UserEntity> findByEmailOrIdentityCard(String email,String identityCard);
//
//    Optional<UserEntity> findByEmail(String email);
//
//    Optional<UserEntity> findByUsername(String username);
//
//    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
//
//    int countAllByEmail(String email);
//
//    @Query("select u from UserEntity u where lower(u.name) like lower(concat('%', :keyword, '%')) or lower(u.email) like lower(concat('%', :keyword, '%')) or u.phoneNumber1 like concat('%', :keyword, '%')")
//    Page<UserEntity> searchByKeyword(String keyword, Pageable pageable);
//
//    Optional<UserEntity> findAllByEmail(String email);
//
//    Optional<UserEntity> findUserEntityById(Long id);
//
//    UserEntity findUserEntityByEmail(String email);
//
//    Collection<UserEntity> findUserEntitiesByIdIn(List<Long> id);
//
//    boolean existsByEmail(String email);
//
//    boolean existsByIdentityCard(String identityCard);
//
//    boolean existsByUsername(String username);
//}
