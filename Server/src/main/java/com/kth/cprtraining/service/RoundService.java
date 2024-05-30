package com.kth.cprtraining.service;

import com.kth.cprtraining.dto.LeaderboardDTO;
import com.kth.cprtraining.dto.RoundDTO;
import com.kth.cprtraining.dto.RoundFromWebDTO;
import com.kth.cprtraining.model.Round;

import java.util.List;

/**
 * RoundService provides an interface for managing operations related to CPR training rounds.
 * It covers functionalities such as retrieving and saving round data, accessing leaderboard information,
 * and managing detailed round metrics like pressures and frequencies.
 */
public interface RoundService {

    /**
     * Finds and retrieves a round by its ID.
     * @param roundId The identifier of the round to be retrieved.
     * @return A RoundDTO containing the round data if found, or null otherwise.
     */
    RoundDTO findRoundById(Long roundId);

    /**
     * Saves a round with associated pressure and frequency data.
     * @param roundDTO Data transfer object containing the round details.
     * @param pressures List of pressure measurements associated with the round.
     * @param frequencies List of frequency measurements associated with the round.
     * @return true if the round and associated data were successfully saved, false otherwise.
     */
    boolean saveRoundWithPressuresAndFrequencies(RoundDTO roundDTO, List<Integer> pressures, List<Double> frequencies);

    /**
     * Retrieves all pressure data associated with a specific round.
     * @param roundId The identifier of the round.
     * @return A list of integer values representing the pressure data points.
     */
    List<Integer> getPressuresForRound(Long roundId);

    /**
     * Retrieves the top 100 rounds for leaderboard display, sorted by score.
     * @return A list of LeaderboardDTOs representing the top 100 rounds.
     */
    List<LeaderboardDTO> getLeaderboardTop100();

    /**
     * Retrieves all rounds associated with a specific username.
     * @param username The username to search rounds for.
     * @return A list of RoundDTOs representing the rounds associated with the given username.
     */
    List<RoundDTO> getRoundsByUsername(String username);

    /**
     * Retrieves all frequency data associated with a specific round.
     * @param roundId The identifier of the round.
     * @return A list of double values representing the frequency data points.
     */
    List<Double> getFrequenciesForRound(Long roundId);
}
