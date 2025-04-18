package chosun.keyboard_project.controller;

import chosun.keyboard_project.dto.KeyboardDto;
import chosun.keyboard_project.dto.KeyboardFilterRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import chosun.keyboard_project.service.KeyboardService;

import java.util.List;

@RestController
@RequestMapping("/keyboards")
public class KeyboardController {

    private final KeyboardService keyboardService;

    @Autowired
    public KeyboardController(KeyboardService keyboardService) {
        this.keyboardService = keyboardService;
    }

    @GetMapping("/hello")
    public String hell(){
        return "hello";
    }

    @PostMapping("filter")
    public List<KeyboardDto> filterKeyboards(@RequestBody KeyboardFilterRequestDto filterDto) {
        return keyboardService.getFilteredKeyboards(
                filterDto.getWeightLabels(),
                filterDto.getKeyPressureLabels(),
                filterDto.getConnections(),
                filterDto.getPurposes(),
                filterDto.getMaterials(),
                filterDto.getLayouts(),
                filterDto.getBacklights(),
                filterDto.getSwitchTypes(),
                filterDto.getManufacturers(),
                filterDto.getSounds()
        );
    }

}
