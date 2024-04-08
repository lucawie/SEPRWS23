package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * Detailed class for all Participants in  a Dto
 * Containing all properties of participants in a tournament
 */

public record TournamentDetailParticipantDto(
        Long horseId,
        String name,
        LocalDate dateOfBirth,
        Long entryNumber,
        Long roundReached
) {
}
