package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;

/**
 * DTO class for selecting horses by their id, name or dateOfBirth
 *
 */

public record HorseSelectionDto(
    long id,
    String name,
    LocalDate dateOfBirth
) {
}
