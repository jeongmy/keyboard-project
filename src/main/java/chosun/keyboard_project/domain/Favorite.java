package chosun.keyboard_project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "keyboard_id"})
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyboard_id", nullable = false)
    private Keyboard keyboard;

    private LocalDateTime createdAt;

    public Favorite(User user, Keyboard keyboard) {
        this.user = user;
        this.keyboard = keyboard;
        this.createdAt = LocalDateTime.now();
    }
}

