package com.nickcaplan.oddschecker.repository;

import com.nickcaplan.oddschecker.model.Odds;

import org.springframework.data.repository.CrudRepository;

/**
 * Provides access to the {@link Odds} table.
 */
public interface OddsRepository extends CrudRepository<Odds, Long> {
    Odds findByBetIdAndUserId(Long betId, String userId);
}
