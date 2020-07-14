package com.nickcaplan.oddschecker.repository;

import com.nickcaplan.oddschecker.model.Bet;
import com.nickcaplan.oddschecker.model.Odds;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Populates the database with {@link Bet} records.
 */
@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(BetRepository repository) {
    return args -> {
      for (long i = 1; i <= 5; i++) {
        log.info("Preloading " + repository.save(new Bet(i, List.of())));
      }
    };
  }
}
