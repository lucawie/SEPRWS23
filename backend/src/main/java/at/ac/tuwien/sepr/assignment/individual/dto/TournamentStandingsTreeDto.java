package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Dto for the tree representing the standings of a tournament
 */

public record TournamentStandingsTreeDto(
        TournamentDetailParticipantDto thisParticipant,
        TournamentStandingsTreeDto[] branches
) {
}
