package chosun.keyboard_project.controller;

import chosun.keyboard_project.dto.SoundSetResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sounds")
public class SoundsController {

    private static final String BASE_URL = "https://keyboard-sound-bucket.s3.ap-northeast-2.amazonaws.com/sounds/";

    @GetMapping("/{switchType}")
    public ResponseEntity<SoundSetResponseDTO> getSounds(@PathVariable("switchType") String switchType){
        String prefix = BASE_URL + switchType.toLowerCase();
        return ResponseEntity.ok(new SoundSetResponseDTO(
                prefix + "Enter",
                prefix + "BackSpace",
                prefix + "LShift",
                prefix + "RShift",
                prefix + "SpaceBar",
                prefix + "NormalKey"
        ));

    }

}
