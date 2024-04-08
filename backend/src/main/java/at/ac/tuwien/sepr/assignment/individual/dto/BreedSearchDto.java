package at.ac.tuwien.sepr.assignment.individual.dto;

/**
 * Class for searching breeds
 * Containing the given search name and the limit of how many breeds to show
 *
 */

public record BreedSearchDto(
    String name,
    Integer limit
) {
}
