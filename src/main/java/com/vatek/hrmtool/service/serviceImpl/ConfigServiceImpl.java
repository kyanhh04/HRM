package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.BaseQueryDto;
import com.vatek.hrmtool.dto.Config.ConfigDto;
import com.vatek.hrmtool.dto.Config.GetConfigDto;
import com.vatek.hrmtool.entity.Config;
import com.vatek.hrmtool.enumeration.*;
import com.vatek.hrmtool.respository.old.ConfigRepository;
import com.vatek.hrmtool.service.ConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigRepository configRepository;

    public List<Config> findAll(GetConfigDto param){
        Pageable pageable = PageRequest.of(param.getOffset() / param.getLimit(), param.getLimit());
        if(param.getSearchKey() != null && !param.getSearchKey().isEmpty()){
            if(param.getSearchValue() != null && !param.getSearchValue().isEmpty()){
                return configRepository.findByKeyAndValueContaining(param.getSearchKey(), param.getSearchValue(), pageable).getContent();
            }
            return configRepository.findByKey(param.getSearchKey(), pageable).getContent();
        }
        return configRepository.findAll(pageable).getContent();
    }

    @Override
    public Config create(ConfigDto configDto){
        Config config = new Config();
        config.setValue(config.getValue());
        config.setKey(config.getKey());
        return configRepository.save(config);
    }

    @Override
    public List<Config> getAll(BaseQueryDto dto){
        GetConfigDto getConfigDto = new GetConfigDto();
        getConfigDto.setLimit(dto.getLimit());
        getConfigDto.setOffset(dto.getOffset());
        return findAll(getConfigDto);
    }
    @Override
    public void remove(String id){
        configRepository.deleteById(id);
    }
    @Override
    public List<Config> getValuesByKey(GetConfigDto dto){
        return findAll(dto);
    }

}
