package at.ac.tuwien.sepr.assignment.individual.persistence;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Standing;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

/**
 * Data Access Object for tournaments.
 * Implements access functionality to the application's persistent data store regarding tournaments.
 */

public interface TournamentDao {

    /**
     * Get the tournaments that match the given search parameters.
     * Parameters that are {@code null} are ignored.
     * The name is considered a match, if the given parameter is a substring of the field in tournament.
     * Every tournament is displayed that has at least one tournament day in this date range.
     *
     * @param searchParameters the parameters to use in searching.
     * @return the tournaments where all given parameters match.
     */
    Collection<Tournament> search(TournamentSearchDto searchParameters);

    /**
     * Create a tournament in the persistent data store.
     *
     * @param tournamentCreateDto the dto of the tournament to be created
     */
    Tournament create(TournamentCreateDto tournamentCreateDto) throws NotFoundException;

    /**
     * Get a tournament with the ID given by {@param id}
     * from the persistent data store.
     *
     * @param tournamentId the tournament to get.
     * @return the tournament that matches the given ID.
     * @throws NotFoundException if the tournament with the given ID does not exist in the persistent data store.
     */
    Tournament getById(long tournamentId) throws NotFoundException;

    /**
     * Get the standing of the tournament participant with horse ID {@param horseID}
     * from the tournament with ID {@param tournamentID} form the data store.
     *
     * @param tournamentId the ID of the tournament to look for the participant.
     * @param horseId the ID of the tournament participant to get the standing from.
     * @return the standing of the tournament participant with the given horse ID.
     * @throws NotFoundException if no tournament or participant standing is found with the given ID's in the data store.
     */
    Standing getStandingById(long tournamentId, long horseId) throws NotFoundException;

    /**
     * Get a list of all the horse IDs that participate in the tournament with the ID given in {@param tournamentID}
     *
     * @param tournamentId to look for the participant IDs
     * @return the list of horse ID's
     */
    List<Long> getStandingIds(long tournamentId) throws NotFoundException;

    /**
     * Get a list of all the tournament IDs where a horse with {@param horseId} is participating
     *
     * @param horseId ID of horse
     * @return list of the tournament IDs
     */
    List<Long> getAllTournamentIdsByHorseId(long horseId);
}
