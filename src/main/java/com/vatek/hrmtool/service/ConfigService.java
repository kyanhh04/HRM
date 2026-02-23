package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.BaseQueryDto;
import com.vatek.hrmtool.dto.Config.ConfigDto;
import com.vatek.hrmtool.dto.Config.GetConfigDto;
import com.vatek.hrmtool.entity.Config;

import java.util.List;

public interface ConfigService {
    Config create(ConfigDto configDto);
    void remove(String id);
    List<Config> getAll(BaseQueryDto dto);
    List<Config> getValuesByKey(GetConfigDto dto);
}
