package com.vatek.hrmtool.respository.old;

import com.vatek.hrmtool.entity.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<Config, String> {
    Page<Config> findByKey(String key, Pageable pageable);
    Page<Config> findByKeyAndValueContaining(String key, String value, Pageable pageable);
    long countByKey(String key);
    Optional<Config> findByKeyAndValue(String key, String value);
}
