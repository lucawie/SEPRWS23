package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.BreedDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Breed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * A class representing a mapper for converting breeds to Dtos and vice versa
 */

@Component
public class BreedMapper {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Convert a breed entity to a {@link BreedDto}
   *
   * @param breed entity to be converted to a dto
   * @return the converted {@link BreedDto}
   */
  public BreedDto entityToDto(Breed breed) {
    LOG.trace("entityToDto({})", breed); //TODO: check if needed
    return new BreedDto(breed.getId(), breed.getName());
  }
}
