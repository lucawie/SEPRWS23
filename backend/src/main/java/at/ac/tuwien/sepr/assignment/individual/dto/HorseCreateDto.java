package at.ac.tuwien.sepr.assignment.individual.dto;

import at.ac.tuwien.sepr.assignment.individual.type.Sex;
import java.time.LocalDate;

/**
 * Detailed class for Horse DTOs
 * Containing all properties of a Horse except the Id
 */


public record HorseCreateDto(
        String name,
        Sex sex,
        LocalDate dateOfBirth,
        float height,
        float weight,
        BreedDto breed
) {
}
