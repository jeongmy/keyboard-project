package chosun.keyboard_project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Connection {

    @Id
    private Integer id;

    private String label;

    @ManyToMany(mappedBy = "connections")
    private List<Keyboard> keyboards = new ArrayList<>();
}