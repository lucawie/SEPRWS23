package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * Detailed class for Tournament Dtos
 * Containing all properties of a tournament
 */

public record TournamentDetailDto(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        TournamentDetailParticipantDto[] participants
) {
}
