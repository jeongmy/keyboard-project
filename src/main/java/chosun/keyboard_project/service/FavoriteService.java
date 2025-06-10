package chosun.keyboard_project.service;

import chosun.keyboard_project.domain.Favorite;
import chosun.keyboard_project.domain.Keyboard;
import chosun.keyboard_project.domain.User;
import chosun.keyboard_project.dto.keyboardDTO.KeyboardDto;
import chosun.keyboard_project.repository.FavoriteRepository;
import chosun.keyboard_project.repository.KeyboardRepository;
import chosun.keyboard_project.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final KeyboardRepository keyboardRepository;

    public void addFavorite(Long keyboardId, String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        Keyboard keyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new EntityNotFoundException("키보드 없음"));

        favoriteRepository.findByUserAndKeyboard(user, keyboard)
                .ifPresent(fav -> {
                    throw new IllegalStateException("이미 찜한 키보드입니다.");
                });

        Favorite favorite = new Favorite(user, keyboard);
        favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long keyboardId, String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        Keyboard keyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new EntityNotFoundException("키보드 없음"));

        favoriteRepository.deleteByUserAndKeyboard(user, keyboard);
    }

    public List<KeyboardDto> getFavorites(String username) {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        return favoriteRepository.findAllByUser(user)
                .stream()
                .map(fav -> KeyboardDto.from(fav.getKeyboard(), true))
                .collect(Collectors.toList());
    }



}