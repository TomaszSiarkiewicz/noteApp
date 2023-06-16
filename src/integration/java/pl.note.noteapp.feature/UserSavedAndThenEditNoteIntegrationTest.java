package pl.note.noteapp.feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.note.noteapp.NoteAppApplication;
import pl.note.noteapp.dtos.FindAllDto;
import pl.note.noteapp.dtos.NewNoteResponseDto;
import pl.note.noteapp.dtos.NoteDto;
import pl.note.noteapp.dtos.NoteFindAllDto;
import pl.note.noteapp.notes.NotesFacade;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {NoteAppApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("integration")
public class UserSavedAndThenEditNoteIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    NotesFacade notesFacade;
    @Autowired
    ObjectMapper objectMapper;

    public static final String WIRE_MOCK_HOST = "http://localhost";
    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("notes.notes-app.baseURL", () -> WIRE_MOCK_HOST + ":" + wireMockServer.getPort());
    }

    @Test
    public void user_should_add_two_new_notes_then_search_for_all_and_edit_first_one() throws Exception {

        // step 1:user made 2 POST requests with new notes to /note - system returned 201 CREATED

        //given
        //when
        ResultActions newNoteResponse1 = mockMvc.perform(MockMvcRequestBuilders.post("/note").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "title": "title",
                "content": "content"
                }
                """));
        ResultActions newNoteResponseInvalid = mockMvc.perform(MockMvcRequestBuilders.post("/note").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "title": "",
                "content": "content"
                }
                """));
        ResultActions newNoteResponse3 = mockMvc.perform(MockMvcRequestBuilders.post("/note").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "title": "title",
                "content": "content"
                }
                """));
        //then
        newNoteResponse1.andExpect(status().isCreated());
        newNoteResponse3.andExpect(status().isCreated());
        newNoteResponseInvalid.andExpect(status().isNotAcceptable());
        String json = newNoteResponse1.andReturn().getResponse().getContentAsString();
        NewNoteResponseDto createdNoteDto = objectMapper.readValue(json, NewNoteResponseDto.class);
        json = newNoteResponseInvalid.andReturn().getResponse().getContentAsString();
        NewNoteResponseDto invalidNoteDto = objectMapper.readValue(json, NewNoteResponseDto.class);

        assertAll(
                () -> assertThat(createdNoteDto.noteDto().noteId()).isNotNull(),
                () -> assertThat(createdNoteDto.noteDto().lastEditDate()).isNotNull(),
                () -> assertThat(createdNoteDto.noteDto().title()).isEqualTo("title"),
                () -> assertThat(invalidNoteDto.message().equals("Title can not be blank!")));


        // Step 2: user made GET request to /notes - system returned 200 OK

        //given
        //when
        ResultActions getAllResponse = mockMvc.perform(MockMvcRequestBuilders.get("/notes"));

        //then
        getAllResponse.andExpect(status().isOk());
        json = getAllResponse.andReturn().getResponse().getContentAsString();
        FindAllDto findAllDto = objectMapper.readValue(json, FindAllDto.class);
        List<NoteFindAllDto> notes = findAllDto.notes();

        assertAll(
                () -> assertThat(notes.size()).isEqualTo(2),
                () -> assertThat(notes.get(0).id()).isEqualTo(createdNoteDto.noteDto().noteId()));

        // Step 3: user made GET request to /notes - system returned 200 OK

        //given
        String noteId = findAllDto.notes().get(0).id();
        //when
        ResultActions getById = mockMvc.perform(MockMvcRequestBuilders.get("/note/" + noteId));
        //then
        getById.andExpect(status().isOk());
        json = getById.andReturn().getResponse().getContentAsString();
        NoteDto returnedById = objectMapper.readValue(json, NoteDto.class);

        assertThat(returnedById.noteId()).isEqualTo(noteId);

        // Step 4: user made PUT request to /note/{noteId} - system returned 200 OK

        //given
        //when

        ResultActions updateNote = mockMvc.perform(MockMvcRequestBuilders.put("/note").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "noteId": "%s",
                "title": "new title",
                "content": "new content"
                }
                """.formatted(noteId)));
        //then
        updateNote.andExpect(status().isOk());
        json = updateNote.andReturn().getResponse().getContentAsString();
        NewNoteResponseDto updated = objectMapper.readValue(json, NewNoteResponseDto.class);

        assertThat(updated.noteDto().title()).isEqualTo("new title");
    }

}
