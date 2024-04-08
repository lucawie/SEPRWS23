package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;

import java.util.stream.Stream;

/**
 * Service for working with tournaments.
 */

public interface TournamentService {

    /**
     * Search for tournaments in the persistent data store matching all provided fields.
     * The name is considered a match, if the given parameter is a substring of the field in tournament.
     * Every tournament is displayed that has at least one tournament day in this date range.
     *
     * @param searchParameters the search parameters to use in filtering.
     * @return the tournaments where the given fields match.
     */
    Stream<TournamentListDto> search(TournamentSearchDto searchParameters);

    /**
     * Create a tournament for the given DTO.
     *
     * @param tournamentCreateDto the DTO containing the data of the tournament
     * @return the tournament
     */
    TournamentDetailDto create(TournamentCreateDto tournamentCreateDto) throws NotFoundException, ValidationException;

    /**
     * Get the matching tournament with details for given Id
     *
     * @param id of the tournament
     * @return the tournament with {@code id}
     * @throws NotFoundException if the given id does not exist
     */
    TournamentDetailDto getTournamentDetailById(long id) throws NotFoundException, ValidationException;

    /**
     * Get the matching tournament with details and standings for given Id
     *
     * @param id tof the tournament
     * @return the tournament with ID {@code id}.
     * @throws NotFoundException if the given id does not exist
     */
    TournamentStandingsDto getTournamentStandingById(long id) throws NotFoundException, ValidationException;
}
