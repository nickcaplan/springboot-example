package com.nickcaplan.oddschecker.repository;

import com.nickcaplan.oddschecker.model.Bet;

import org.springframework.data.repository.CrudRepository;

/**
 * Provides access to the {@link Bet} table.
 */
public interface BetRepository extends CrudRepository<Bet, Long> {
}
