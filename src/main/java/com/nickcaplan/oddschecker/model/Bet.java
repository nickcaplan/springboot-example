package com.nickcaplan.oddschecker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.StringJoiner;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Database entity representing a list of odds.
 */
@Entity
public class Bet {
    private @Id @JsonIgnore Long betId;
    @OneToMany(targetEntity = Odds.class, mappedBy = "betId", fetch = FetchType.EAGER)
    private List<Odds> odds;

    // Default constructor required for Hibernate
    public Bet() { }

    public Bet(Long betId, List<Odds> odds) {
        this.odds = odds;
        this.betId = betId;
    }

    public List<Odds> getOdds() {
        return odds;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Bet.class.getSimpleName() + "[", "]")
                .add("betId=" + betId)
                .add("odds=" + odds)
                .toString();
    }
}
