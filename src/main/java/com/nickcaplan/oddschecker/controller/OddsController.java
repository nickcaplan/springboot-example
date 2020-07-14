package com.nickcaplan.oddschecker.controller;

import com.nickcaplan.oddschecker.exception.BetNotFoundException;
import com.nickcaplan.oddschecker.exception.MissingParameterException;
import com.nickcaplan.oddschecker.exception.OddsNotValidException;
import com.nickcaplan.oddschecker.model.Bet;
import com.nickcaplan.oddschecker.model.Odds;
import com.nickcaplan.oddschecker.repository.BetRepository;
import com.nickcaplan.oddschecker.repository.OddsRepository;
import com.nickcaplan.oddschecker.validator.OddsValidator;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for rest endpoints relating to reading and modifying odds.
 */
@Api(tags = "odds")
@RestController
@RequestMapping("/odds")
public class OddsController {
    private final OddsRepository oddsRepository;
    private final BetRepository betRepository;

    OddsController(OddsRepository oddsRepository, BetRepository betRepository) {
        this.oddsRepository = oddsRepository;
        this.betRepository = betRepository;
    }

    /**
     * Offer odds for a bet. If odds for a given bet ID and user ID already exist, they will be updated with the provided
     * values.
     *
     * @param odds Odds that should be offered for a bet.
     * @return A {@link ResponseEntity} object containing information that the odds have been saved, or an exception if something
     * has gone wrong.
     * @throws OddsNotValidException if the odds provided are not valid (e.g. "3/X").
     * @throws MissingParameterException if the bet ID or the user ID is not provided.
     * @throws BetNotFoundException if no bet exists for the given bet ID.
     */
    @ApiOperation(value = "Offer odds for a bet", tags = "odds")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Odds have been created for bet"),
            @ApiResponse(code = 400, message = "Invalid format of Odds")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(produces = {"application/json"})
    public ResponseEntity<String> offerOddsForBet(@RequestBody @ApiParam(name = "body",
                                                                         value = "Odds that should be offered for a bet",
                                                                         required = true)
                                                              Odds odds) {
        if (!OddsValidator.isValid(odds.getOdds())) {
            throw new OddsNotValidException();
        }

        if (odds.getBetId() == null) {
            throw new MissingParameterException("betId");
        }

        if (StringUtils.isEmpty(odds.getUserId())) {
            throw new MissingParameterException("userId");
        }

        betRepository.findById(odds.getBetId()).orElseThrow(BetNotFoundException::new);

        Odds existingOdds = oddsRepository.findByBetIdAndUserId(odds.getBetId(), odds.getUserId());
        if (existingOdds == null) {
            oddsRepository.save(odds);
        } else {
            existingOdds.setOdds(odds.getOdds());
            oddsRepository.save(existingOdds);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Odds have been created for bet");
    }

    /**
     * Returns a list of odds for a given bet ID. If no bet exists for the given bet ID, a {@link BetNotFoundException} will be
     * thrown.
     *
     * @param betId The id of the bet to return odds for.
     * @return A list of {@link Odds} for the given bet ID.
     */
    @ApiOperation(value = "Returns a list of odds for a given bet ID", tags = "odds")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Odds are returned for bet ID"),
            @ApiResponse(code = 400, message = "Invalid Bet ID supplied"),
            @ApiResponse(code = 404, message = "Bet not found for given ID")
    })
    @GetMapping(value = "{betId}", produces = { "application/json" })
    public List<Odds> findOddsByBetId(@PathVariable Long betId) {
        Optional<Bet> bet = betRepository.findById(betId);
        if (bet.isPresent()) {
            return bet.get().getOdds();
        } else {
            throw new BetNotFoundException();
        }
    }
}

