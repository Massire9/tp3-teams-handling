package fr.sdv.cnit.university.api;

import fr.sdv.cnit.university.api.entity.TeamEntity;
import fr.sdv.cnit.university.api.exception.TeamInvalidException;
import fr.sdv.cnit.university.api.repository.TeamRepository;
import fr.sdv.cnit.university.api.service.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TeamServiceTests {
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;


    @Test
    public void testSaveTeam() throws TeamInvalidException {
        TeamEntity team = new TeamEntity();
        team.setSlogan("Test Slogan");
        team.setName("Test Nom");

        Mockito.when(teamRepository.save(Mockito.any(TeamEntity.class))).thenReturn(team);

        TeamEntity savedTeam = teamService.create(team);

        assertNotNull(savedTeam);
        assertEquals("Test Slogan", savedTeam.getSlogan());
        assertEquals("Test Nom", savedTeam.getName());
    }

    @Test
    public void testGetTeamById() {
        TeamEntity team = new TeamEntity();
        team.setId(1);
        team.setSlogan("Test Slogan");
        team.setName("Test Nom");

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        TeamEntity retrievedTeam = teamService.getById(1).get();

        assertNotNull(retrievedTeam);
        assertEquals(1, retrievedTeam.getId());
        assertEquals("Test Slogan", retrievedTeam.getSlogan());
        assertEquals("Test Nom", retrievedTeam.getName());
    }

    @Test
    public void testGetAllTeams() {
        List<TeamEntity> teams = Arrays.asList(
                new TeamEntity(1, "Slogan1", "Nom1"),
                new TeamEntity(2, "Slogan2", "Nom2")
        );

        Mockito.when(teamRepository.findAll()).thenReturn(teams);

        List<TeamEntity> allTeams = teamService.getAllTeams();

        assertNotNull(allTeams);
        assertEquals(2, allTeams.size());
    }

    @Test
    public void testDeleteTeam() {
        teamService.delete(1);

        Mockito.verify(teamRepository, Mockito.times(1)).deleteById(1);
    }
}
