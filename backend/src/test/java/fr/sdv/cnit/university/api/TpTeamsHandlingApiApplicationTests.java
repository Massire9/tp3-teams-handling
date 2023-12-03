package fr.sdv.cnit.university.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sdv.cnit.university.api.controller.TeamController;
import fr.sdv.cnit.university.api.dto.TeamDTO;
import fr.sdv.cnit.university.api.entity.TeamEntity;
import fr.sdv.cnit.university.api.exception.TeamInvalidException;
import fr.sdv.cnit.university.api.service.TeamService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;
    @MockBean
    private TeamService teamService;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    void testCreateTeam() throws Exception {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setSlogan("Test Slogan");
        teamDTO.setName("Test Nom");

        when(teamService.create(any(TeamEntity.class))).thenReturn(new TeamEntity(1, "Test Slogan", "Test Nom"));

        mockMvc.perform(post("/teams").contentType("application/json").content(mapper.writeValueAsString(teamDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slogan", equalTo("Test Slogan")));

        verify(teamService, times(1)).create(any(TeamEntity.class));
        reset(teamService);
    }

    @Test
    void testGetAllTeams() throws Exception {
        when(teamService.getAllTeams()).thenReturn(Arrays.asList(
                new TeamEntity(1, "Slogan1", "Nom1"),
                new TeamEntity(2, "Slogan2", "Nom2")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/teams"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Nom1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Nom2")));

        verify(teamService, times(1)).getAllTeams();
        reset(teamService);
    }

    @Test
    void testGetTeamById() throws Exception {
        int teamId = 1;

        when(teamService.getById(teamId)).thenReturn(Optional.of(new TeamEntity(teamId, "Slogan1", "Nom1")));

        mockMvc.perform(get("/teams/{id}", teamId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Nom1")));

        verify(teamService, times(1)).getById(teamId);
        reset(teamService);
    }

    @Test
    void testUpdateTeam() throws Exception {
        int teamId = 1;

        TeamDTO updatedTeamDTO = new TeamDTO();
        updatedTeamDTO.setSlogan("Updated Slogan");
        updatedTeamDTO.setName("Updated Nom");

        when(teamService.getById(teamId)).thenReturn(Optional.of(new TeamEntity(teamId, "Slogan1", "Nom1")));
        when(teamService.update(any(TeamEntity.class))).thenReturn(new TeamEntity(teamId, "Updated Slogan", "Updated Nom"));

        mockMvc.perform(put("/teams/{id}", teamId).contentType("application/json").content(mapper.writeValueAsString(updatedTeamDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slogan", equalTo("Updated Slogan")));

        verify(teamService, times(1)).getById(teamId);
        verify(teamService, times(1)).update(any(TeamEntity.class));
        reset(teamService);
    }

    @Test
    void testDeleteTeam() throws Exception {
        int teamId = 1;

        when(teamService.getById(teamId)).thenReturn(Optional.of(new TeamEntity(teamId, "Slogan1", "Nom1")));

        mockMvc.perform(delete("/teams/{id}", teamId).contentType("application/json"))
                .andExpect(status().isOk());

        verify(teamService, times(1)).getById(teamId);
        verify(teamService, times(1)).delete(teamId);
        reset(teamService);
    }

    @Test
    void testExceptionTeamAdd() throws Exception {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setSlogan("Test Slogan");

        when(teamService.create(any(TeamEntity.class))).thenReturn(new TeamEntity(1, "Test Slogan", ""));

        mockMvc.perform(post("/teams").contentType("application/json").content(mapper.writeValueAsString(teamDTO)))
                .andExpect(status().isBadRequest());

        verify(teamService, times(1)).create(any(TeamEntity.class));
        reset(teamService);
    }
}
