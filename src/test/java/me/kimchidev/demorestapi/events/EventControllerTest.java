package me.kimchidev.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kimchidev.demorestapi.accounts.BaseControllerTest;
import me.kimchidev.demorestapi.common.RestDocsConfiguration;
import me.kimchidev.demorestapi.common.TestDescripion;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;



    @Test
    @DisplayName("??????????????? ???????????? ???????????? ?????????")
    public void createEvent() throws Exception {
        //given
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST PAI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,04,10,21,31))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,04,11,21,31))
                .beginEventDateTime(LocalDateTime.of(2021,04,12,21,31))
                .endEventDateTime(LocalDateTime.of(2021,04,13,21,34))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? d2 ????????? ?????????")
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offLine").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to update an existing event")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("data time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("data time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("data time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("data time of close of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("HAL JSON")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("data time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("data time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("data time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("data time of close of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offLine").description("it tells is this event is offline meeting or not"),
                                fieldWithPath("eventStatus").description("eventStatus"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update events"),
                                fieldWithPath("_links.profile.href").description("link to update events")


                        )
                ));
        //when

        //then

    }


    @Test
    @DisplayName("??????????????? ?????? ?????? ?????????????????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request() throws Exception {
        //given
        Event event = Event.builder()
                .name("Spring")
                .description("REST PAI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,04,10,21,31))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,04,11,21,31))
                .beginEventDateTime(LocalDateTime.of(2021,04,12,21,31))
                .endEventDateTime(LocalDateTime.of(2021,04,13,21,34))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? d2 ????????? ?????????")
                .free(true)
                .offLine(false)
                .build();


        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        //when

        //then

    }

    @Test
    @DisplayName("???????????? ????????????????????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        //given
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());

        //when

        //then

    }


    @Test
    @DisplayName("???????????? ????????? ????????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        //given
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST PAI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,04,13,21,31))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,04,12,21,31))
                .beginEventDateTime(LocalDateTime.of(2021,04,11,21,31))
                .endEventDateTime(LocalDateTime.of(2021,04,10,21,34))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? d2 ????????? ?????????")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;

        //when

        //then

    }

    @Test
    @DisplayName("30?????? ???????????? 10?????? ????????? ????????? ????????????")
    public void queryEvents() throws Exception {
        //given
        IntStream.range(0, 30).forEach(this::generateEvent);
        //when
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","name,DESC")
                .contentType(MediaType.APPLICATION_JSON)
        )/*

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to update an existing event"),
                                linkWithRel("first").description("link to first page in data"),
                                linkWithRel("last").description("link to last page in data"),
                                linkWithRel("next").description("link to next page in data"),
                                linkWithRel("prev").description("link to previous page in data")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("data time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("data time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("data time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("data time of close of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("HAL JSON")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("data time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("data time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("data time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("data time of close of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offLine").description("it tells is this event is offline meeting or not"),
                                fieldWithPath("eventStatus").description("eventStatus"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to update events"),
                                fieldWithPath("_links.first.href").description("link to first page for this data"),
                                fieldWithPath("_links.last.href").description("link to last page for this data"),
                                fieldWithPath("_links.prev.href").description("link to previous page"),
                                fieldWithPath("_links.next.href").description("link to next page")
                        )

                        ))*/
        ;
        //then

    }

    @Test
    @DisplayName("????????? ???????????? ?????? ?????? ??????")
    public void getEvent() throws Exception {
        //given
        Event event = this.generateEvent(100);
        //when
        this.mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())

        ;
        //then

    }

    @Test
    @DisplayName("?????? ???????????? ???????????? ??? 404 ????????????")
    public void getEvent404 () throws Exception {
        //given

        this.mockMvc.perform(get("/api/events/32132"))
                .andExpect(status().isNotFound());
        //when

        //then

    }

    @Test
    @DisplayName("???????????? ????????? ??????")
    public void updateEvent() throws Exception {
        //given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Update Event";
        eventDto.setName(eventName);

        //when & then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    @Test
    @DisplayName("???????????? ????????? ????????? ????????? ?????? ??????")
    public void updateEvent400_Wrong() throws Exception {
        //given
        Event event = this.generateEvent(200);
        //???????????? ????????? modelmapper??? dto??????
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        //when & then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists());

    }
    
    @Test
    @DisplayName("???????????? ?????? ????????? ????????????")
    public void updateEvent404() throws Exception {
        //given
        Event event = this.generateEvent(200);
        //???????????? ????????? modelmapper??? dto??????
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        //when & then
        this.mockMvc.perform(put("/api/events/1231312",event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    
    }

    @Test
    @DisplayName("???????????? ???????????? ????????? ????????? ?????? ??????")
    public void updateEvent400_Empty() throws Exception {
        //given
        Event event = new Event();
        //???????????? ????????? modelmapper??? dto??????
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Update Event";
        eventDto.setName(eventName);

        //when & then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("event "+ i)
                .description("REST PAI Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,04,10,21,31))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,04,11,21,31))
                .beginEventDateTime(LocalDateTime.of(2021,04,12,21,31))
                .endEventDateTime(LocalDateTime.of(2021,04,13,21,34))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? d2 ????????? ?????????")
                .free(false)
                .offLine(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }



}