package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * Detailed class for Tournament Dtos
 * Containing all properties (except Id) needed to create a tournament
 */

public record TournamentCreateDto(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        HorseSelectionDto[] participants
) {
}
