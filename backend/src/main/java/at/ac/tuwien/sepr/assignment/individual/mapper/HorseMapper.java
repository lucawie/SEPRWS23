package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.BreedDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A class representing a mapper for converting horses to Dtos and vice versa
 */
@Component
public class HorseMapper {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Convert a horse entity object to a {@link HorseListDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param horse the horse to convert
   * @param breeds a map of breeds identified by their id, required for mapping horses
   * @return the converted {@link HorseListDto}
   */
  public HorseListDto entityToListDto(Horse horse, Map<Long, BreedDto> breeds) {
    LOG.trace("entityToListDto({}, {})", horse, breeds);
    if (horse == null) {
      return null;
    }

    return new HorseListDto(
        horse.getId(),
        horse.getName(),
        horse.getSex(),
        horse.getDateOfBirth(),
        breedFromMap(horse, breeds)
    );
  }

  /**
   * Convert a horse entity object to a {@link HorseDetailDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param horse the horse to convert
   * @return the converted {@link HorseDetailDto}
   */
  public HorseDetailDto entityToDetailDto(Horse horse, Map<Long, BreedDto> breeds) {
    LOG.trace("entityToDto({}, {})", horse, breeds);
    if (horse == null) {
      return null;
    }

    return new HorseDetailDto(
        horse.getId(),
        horse.getName(),
        horse.getSex(),
        horse.getDateOfBirth(),
        horse.getHeight(),
        horse.getWeight(),
        breedFromMap(horse, breeds)
    );
  }

  /**
   * Convert a horse entity object to a {@link HorseDetailDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param horse the horse to convert
   * @return the converted {@link HorseCreateDto}
   */
  public HorseCreateDto entityToCreateDto(Horse horse, Map<Long, BreedDto> breeds) {
    LOG.trace("entityToCreateDto({}, {})", horse, breeds);
    if (horse == null) {
      return null;
    }

    return new HorseCreateDto(
            horse.getName(),
            horse.getSex(),
            horse.getDateOfBirth(),
            horse.getHeight(),
            horse.getWeight(),
            breedFromMap(horse, breeds)
    );
  }

  /**
   * Convert a {@link HorseDetailDto} object to a horse entity object.
   *
   * @param horseDetailDto DTO containing the horses data
   * @return the new horse entity object
   */
  public Horse detailDtoToEntity(HorseDetailDto horseDetailDto) {
    LOG.trace("dtoToEntity({})", horseDetailDto);
    if (horseDetailDto == null) {
      return null;
    }

    return new Horse()
            .setId(horseDetailDto.id())
            .setName(horseDetailDto.name())
            .setSex(horseDetailDto.sex())
            .setDateOfBirth(horseDetailDto.dateOfBirth())
            .setHeight(horseDetailDto.height())
            .setWeight(horseDetailDto.weight())
            .setBreedId(horseDetailDto.breed() != null ? horseDetailDto.breed().id() : null);
  }

  /**
   * Convert a {@link HorseCreateDto} object to a horse entity object.
   *
   * @param horseCreateDto DTO containing the horses data except Id
   * @return the new horse entity object
   */
  public Horse createDtoToEntity(HorseCreateDto horseCreateDto) {
    LOG.trace("createDtoToEntity({})", horseCreateDto);
    if (horseCreateDto == null) {
      return null;
    }

    return new Horse()
            .setName(horseCreateDto.name())
            .setSex(horseCreateDto.sex())
            .setDateOfBirth(horseCreateDto.dateOfBirth())
            .setHeight(horseCreateDto.height())
            .setWeight(horseCreateDto.weight())
            .setBreedId(horseCreateDto.breed() != null ? horseCreateDto.breed().id() : null);
  }

  /**
   * Convert a horse + breed(map) to a breedDto
   *
   * @param horse horse refering to a breed
   * @param map containing the breedID and the Dto of the refering breed
   * @return the matching BreedDto / Breed for matching the horse
   */
  private BreedDto breedFromMap(Horse horse, Map<Long, BreedDto> map) {
    LOG.trace("breedFromMap({}, {})", horse, map); //TODO: check if needed (logged with other methods of this class)
    var breedId = horse.getBreedId();
    if (breedId == null) {
      return null;
    } else {
      return Optional.ofNullable(map.get(breedId))
          .orElseThrow(() -> new FatalException(
              "Saved horse with id " + horse.getId() + " refers to non-existing breed with id " + breedId));
    }
  }
}
