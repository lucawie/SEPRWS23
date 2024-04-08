package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Dto for standings of a tournament
 */

public record TournamentStandingsDto(
        long id,
        String name,
        TournamentDetailParticipantDto[] participants,
        TournamentStandingsTreeDto tree
) {
}
