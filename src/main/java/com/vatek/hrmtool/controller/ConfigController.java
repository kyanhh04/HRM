package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.dto.BaseQueryDto;
import com.vatek.hrmtool.dto.Config.*;
import com.vatek.hrmtool.service.ConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/configs")
public class ConfigController {

    @Autowired
    private ConfigService configsService;

    @PostMapping("/create")
    public ResponseEntity<?> addConfig(@Valid @RequestBody ConfigDto body) {
        return ResponseEntity.ok(configsService.create(body));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> remove(@PathVariable String id) {
        configsService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(@Valid BaseQueryDto params) {
        return ResponseEntity.ok(configsService.getAll(params));
    }

    @GetMapping("/findValuesByKey")
    public ResponseEntity<?> getValuesByKey(@Valid GetConfigDto params) {
        return ResponseEntity.ok(configsService.getValuesByKey(params));

    }
}
