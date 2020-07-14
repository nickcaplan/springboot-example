package com.nickcaplan.oddschecker.controller;

import com.nickcaplan.oddschecker.exception.BetNotFoundException;
import com.nickcaplan.oddschecker.exception.MissingParameterException;
import com.nickcaplan.oddschecker.exception.OddsNotValidException;
import com.nickcaplan.oddschecker.model.Bet;
import com.nickcaplan.oddschecker.model.Odds;
import com.nickcaplan.oddschecker.repository.BetRepository;
import com.nickcaplan.oddschecker.repository.OddsRepository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Tests for the OddsController")
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = OddsController.class)
public class OddsControllerTest {

    private static final Long BET_ID = 3L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OddsRepository oddsRepository;

    @MockBean
    private BetRepository betRepository;

    private final Odds odds = new Odds(1L, BET_ID, "james", "2/1");
    private final Bet betWithOdds = new Bet(BET_ID, List.of(odds));
    private final Bet betWithoutOdds = new Bet(BET_ID, List.of());
    private final String oddsJson = "{\"betId\":3,\"userId\":\"james\",\"odds\":\"2/1\"}";

    @DisplayName("Find odds by bet id when odds exist for bet - odds should be returned")
    @Test
    public void findOddsByBetId_oddsExist_oddsReturned() throws Exception {
        String betWithOddsJson = "[" + oddsJson + "]";
        when(betRepository.findById(BET_ID)).thenReturn(Optional.of(betWithOdds));

        var requestBuilder = MockMvcRequestBuilders.get(
                "/odds/3").accept(
                MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(betWithOddsJson, result.getResponse().getContentAsString(), true);
    }

    @DisplayName("Find odds by bet id when no odds exist for bet - no odds should be returned")
    @Test
    public void findOddsByBetId_noOddsForBet_noOddsReturned() throws Exception {
        when(betRepository.findById(BET_ID)).thenReturn(Optional.of(betWithoutOdds));

        var requestBuilder = MockMvcRequestBuilders.get(
                "/odds/3").accept(
                MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals("[]", result.getResponse().getContentAsString(), true);
    }

    @DisplayName("Find odds by bet id when no bet exists - exception should be thrown, 404 returned")
    @Test
    public void findOddsByBetId_betIdDoesNotExist_exceptionThrown() throws Exception {
        when(betRepository.findById(BET_ID)).thenReturn(Optional.empty());

        var requestBuilder = MockMvcRequestBuilders.get(
                "/odds/3").accept(
                MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        var exceptionThrown = result.getResolvedException();
        assertNotNull(exceptionThrown);
        assertEquals(BetNotFoundException.class, exceptionThrown.getClass());
        assertEquals("Bet not found for given ID", exceptionThrown.getMessage());
    }

    @DisplayName("Find odds by bet id when bet id not a number - exception should be thrown, 404 returned")
    @Test
    public void findOddsByBetId_betIdNotANumber_exceptionThrown() throws Exception {
        when(betRepository.findById(BET_ID)).thenReturn(Optional.empty());

        var requestBuilder = MockMvcRequestBuilders.get(
                "/odds/XXX").accept(
                MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        var exceptionThrown = result.getResolvedException();
        assertNotNull(exceptionThrown);
        assertEquals(MethodArgumentTypeMismatchException.class, exceptionThrown.getClass());
    }

    @DisplayName("Offer odds for bet when no odds exist - odds should be saved")
    @Test
    public void offerOddsForBet_oddsDoNotAlreadyExist_oddsReturnedInRequest() throws Exception {
        when(betRepository.findById(BET_ID)).thenReturn(Optional.of(betWithOdds));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/odds")
                .accept(MediaType.APPLICATION_JSON).content(oddsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        verify(oddsRepository).save(any(Odds.class));

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @DisplayName("Offer odds for bet when odds already exist - odds should be updated")
    @Test
    public void offerOddsForBet_oddsAlreadyExist_oddsReturnedInRequest() throws Exception {
        when(betRepository.findById(BET_ID)).thenReturn(Optional.of(betWithOdds));
        when(oddsRepository.findByBetIdAndUserId(BET_ID, "james")).thenReturn(odds);

        String updatedOddsJson = "{\"betId\":3,\"userId\":\"james\",\"odds\":\"5/1\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/odds")
                .accept(MediaType.APPLICATION_JSON).content(updatedOddsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        verify(oddsRepository).save(any(Odds.class));

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    @DisplayName("Offer odds for bet when odds are invalid - exception should be thrown, 400 returned")
    @Test
    public void offerOddsForBet_oddsAreInvalid_exceptionThrown() throws Exception {
        String invalidOddsJson = "{\"betId\":3,\"userId\":\"james\",\"odds\":\"2/XXX\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/odds")
                .accept(MediaType.APPLICATION_JSON).content(invalidOddsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        var exceptionThrown = result.getResolvedException();
        assertNotNull(exceptionThrown);
        assertEquals(OddsNotValidException.class, exceptionThrown.getClass());
        assertEquals("Invalid format of Odds", exceptionThrown.getMessage());
    }

    @DisplayName("Offer odds for bet when bet ID is not provided - exception should be thrown, 400 returned")
    @Test
    public void offerOddsForBet_betIdNotProvided_exceptionThrown() throws Exception {
        String invalidOddsJson = "{\"userId\":\"james\",\"odds\":\"2/1\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/odds")
                .accept(MediaType.APPLICATION_JSON).content(invalidOddsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        var exceptionThrown = result.getResolvedException();
        assertNotNull(exceptionThrown);
        assertEquals(MissingParameterException.class, exceptionThrown.getClass());
        assertEquals("The following parameter is required but was not provided: [betId]", exceptionThrown.getMessage());
    }

    @DisplayName("Offer odds for bet when user ID is not provided - exception should be thrown, 400 returned")
    @Test
    public void offerOddsForBet_userIdNotProvided_exceptionThrown() throws Exception {
        String invalidOddsJson = "{\"betId\":3,\"odds\":\"2/1\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/odds")
                .accept(MediaType.APPLICATION_JSON).content(invalidOddsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        var exceptionThrown = result.getResolvedException();
        assertNotNull(exceptionThrown);
        assertEquals(MissingParameterException.class, exceptionThrown.getClass());
        assertEquals("The following parameter is required but was not provided: [userId]", exceptionThrown.getMessage());
    }

    @DisplayName("Offer odds for bet when bet does not exist - exception should be thrown, 404 returned")
    @Test
    public void offerOddsForBet_betDoesNotExist_exceptionThrown() throws Exception {
        when(betRepository.findById(BET_ID)).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/odds")
                .accept(MediaType.APPLICATION_JSON).content(oddsJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        var exceptionThrown = result.getResolvedException();
        assertNotNull(exceptionThrown);
        assertEquals(BetNotFoundException.class, exceptionThrown.getClass());
        assertEquals("Bet not found for given ID", exceptionThrown.getMessage());
    }
}
