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
public class ConfigServiceImpl implements ConfigService, InitializingBean {
    @Autowired
    private ConfigRepository configRepository;
    @Override
    public void afterPropertiesSet () throws Exception {
        initConfigs();
    }
    public void initConfigs(){
        if(configRepository.countByKey(ConfigKey.LEVEL.getValue()) == 0){
            Level[] levels = Level.values();
            List<Config> levelConfigs = mapValuesByKey(ConfigKey.LEVEL.getValue(), Arrays.stream(levels).map(Level::getValue).toArray(String[]::new));
            configRepository.saveAll(levelConfigs);
        }
        if(configRepository.countByKey(ConfigKey.STATUS.getValue()) == 0){
            TimesheetStatus[] statuses = TimesheetStatus.values();
            List<Config> statusConfigs = mapValuesByKey(ConfigKey.STATUS.getValue(), Arrays.stream(statuses).map(TimesheetStatus::getValue).toArray(String[]::new));
            configRepository.saveAll(statusConfigs);
        }
        if(configRepository.countByKey(ConfigKey.POSITION.getValue()) == 0){
            Position[] positions = Position.values();
            List<Config> positionConfigs = mapValuesByKey(ConfigKey.POSITION.getValue(), Arrays.stream(positions).map(Position::getValue).toArray(String[]::new));
            configRepository.saveAll(positionConfigs);
        }
        if(configRepository.countByKey(ConfigKey.WORKINGTYPE.getValue()) == 0){
            WorkingType[] workingTypes = WorkingType.values();
            List<Config> workingTypeConfigs = mapValuesByKey(ConfigKey.WORKINGTYPE.getValue(),Arrays.stream(workingTypes).map(WorkingType::getType).toArray(String[]::new));
            configRepository.saveAll(workingTypeConfigs);
        }
    }
    public List<Config> mapValuesByKey(String key, String[] values){
        List<Config> configs = new ArrayList<>();
        for(String value : values){
            Config config = new Config();
            config.setKey(key);
            config.setValue(value);
            config.setCreatedBy("0");
            config.setModifiedBy("0");
            configs.add(config);
        }
        return configs;
    }

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
