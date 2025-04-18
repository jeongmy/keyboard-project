package chosun.keyboard_project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Purpose {

    @Id
    private Integer id;

    private String label;

    @ManyToMany(mappedBy = "purposes")
    private List<Keyboard> keyboards = new ArrayList<>();
}