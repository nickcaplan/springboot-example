package com.nickcaplan.oddschecker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.StringJoiner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * Database entity representing the odds for a bet, including who has provided those odds.
 */
@Entity
@Table(name = "odds",
       indexes = {@Index(name = "unique_bets_per_user", columnList = "betId,userId", unique = true)})
public class Odds {
    private @JsonIgnore @Id @GeneratedValue Long oddsId;
    private Long betId;
    @ApiModelProperty(value = "ID of user who is offering the odds")
    private String userId;
    @ApiModelProperty(example = "1/10")
    private String odds;

    // Default constructor required by Hibernate
    public Odds() { }

    public Odds(Long oddsId, Long betId, String userId, String odds) {
        this.oddsId = oddsId;
        this.betId = betId;
        this.userId = userId;
        this.odds = odds;
    }

    public Long getOddsId() {
        return oddsId;
    }

    public void setOddsId(Long oddsId) {
        this.oddsId = oddsId;
    }

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Odds.class.getSimpleName() + "[", "]")
                .add("betId=" + betId)
                .add("userId='" + userId + "'")
                .add("odds='" + odds + "'")
                .toString();
    }
}
