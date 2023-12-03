package fr.sdv.cnit.university.api.service;

import fr.sdv.cnit.university.api.entity.TeamEntity;
import fr.sdv.cnit.university.api.exception.TeamInvalidException;
import fr.sdv.cnit.university.api.repository.TeamRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TeamService {
    private final TeamRepository repository;

    @Autowired
    public TeamService(TeamRepository _repository) {
        this.repository = _repository;
    }

    public List<TeamEntity> getAllTeams() {
        return this.repository.findAll();
    }

    public Optional<TeamEntity> getById(Integer id) {
        return this.repository.findById(id);
    }

    public TeamEntity create(@NotNull TeamEntity team) throws TeamInvalidException {
        if(team.getName() == null || Objects.equals(team.getName().trim(), "") || Objects.equals(team.getSlogan().trim(), "") || team.getSlogan() == null) {
            throw new TeamInvalidException();
        }
        return this.repository.save(team);
    }

    public TeamEntity update(TeamEntity team) {
        return this.repository.save(team);
    }

    public void delete(Integer id) {
        this.repository.deleteById(id);
    }
}
