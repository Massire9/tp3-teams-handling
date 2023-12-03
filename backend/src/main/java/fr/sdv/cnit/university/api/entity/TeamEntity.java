package fr.sdv.cnit.university.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "team", schema = "team_manager")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "slogan", length = 500)
    private String slogan;

    public TeamEntity(int i, String slogan1, String nom1) {
        this.setId(i);
        this.setName(nom1);
        this.setSlogan(slogan1);
    }
    public TeamEntity() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

}