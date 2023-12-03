package fr.sdv.cnit.university.api.controller;

import fr.sdv.cnit.university.api.dto.TeamDTO;
import fr.sdv.cnit.university.api.entity.TeamEntity;
import fr.sdv.cnit.university.api.exception.TeamInvalidException;
import fr.sdv.cnit.university.api.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    private TeamEntity convertToEntity(TeamDTO teamDTO) {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setSlogan(teamDTO.getSlogan());
        teamEntity.setName(teamDTO.getName());
        return teamEntity;
    }

    private TeamDTO convertToDTO(TeamEntity teamEntity) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setSlogan(teamEntity.getSlogan());
        teamDTO.setName(teamEntity.getName());
        return teamDTO;
    }

    // Create a new team
    @PostMapping
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) throws TeamInvalidException {
        TeamEntity team = convertToEntity(teamDTO);
        TeamEntity savedTeam = teamService.create(team);
        TeamDTO savedTeamDTO = convertToDTO(savedTeam);
        return new ResponseEntity<>(savedTeamDTO, HttpStatus.CREATED);
    }

    // Get all teams
    @GetMapping
    public ResponseEntity<List<TeamEntity>> getAllTeams() {
        List<TeamEntity> teams = teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    // Get team by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable int id) {
        Optional<TeamEntity> team = teamService.getById(id);
        if (team.isPresent()) {
            TeamDTO teamDTO = convertToDTO(team.get());
            return new ResponseEntity<>(teamDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update team by ID
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable int id, @RequestBody TeamDTO updatedTeamDTO) {
        Optional<TeamEntity> existingTeam = teamService.getById(id);
        if (existingTeam.isPresent()) {
            TeamEntity updatedTeam = convertToEntity(updatedTeamDTO);
            updatedTeam.setId(id);
            TeamEntity savedTeam = teamService.update(updatedTeam);
            TeamDTO savedTeamDTO = convertToDTO(savedTeam);
            return new ResponseEntity<>(savedTeamDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete team by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable int id) {
        Optional<TeamEntity> existingTeam = teamService.getById(id);

        if (existingTeam.isPresent()) {
            teamService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(TeamInvalidException.class)
    public ResponseEntity<Object> ExceptionHandler() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}