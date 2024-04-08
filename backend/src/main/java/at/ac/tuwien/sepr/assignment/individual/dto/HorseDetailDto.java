package at.ac.tuwien.sepr.assignment.individual.dto;

import at.ac.tuwien.sepr.assignment.individual.type.Sex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;

/**
 * Detailed class for Horse DTOs
 * Containing all properties of a Horse
 */
public record HorseDetailDto(
    Long id,
    String name,
    Sex sex,
    LocalDate dateOfBirth,
    float height,
    float weight,
    BreedDto breed
) {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Returns the HorseDetailDto with the given Id
   *
   * @param newId Id to be added
   * @return the HorseDetailDto
   */
  public HorseDetailDto withId(long newId) {
    LOG.trace("withId({})", newId);
    return new HorseDetailDto(
        newId,
        name,
        sex,
        dateOfBirth,
        height,
        weight,
        breed);
  }
}
