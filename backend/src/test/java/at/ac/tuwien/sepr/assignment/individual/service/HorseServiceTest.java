package at.ac.tuwien.sepr.assignment.individual.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import at.ac.tuwien.sepr.assignment.individual.TestBase;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepr.assignment.individual.type.Sex;
import java.time.LocalDate;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test", "datagen"}) // enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseServiceTest extends TestBase {

  @Autowired
  HorseService horseService;

  @Autowired
  HorseMapper horseMapper;

  @Test
  public void searchByBreedWelFindsThreeHorses() throws ValidationException {
    var searchDto = new HorseSearchDto(null, null, null, null, "Wel", null);
    var horses = horseService.search(searchDto);
    assertNotNull(horses);
    // We don't have height and weight of the horses here, so no reason to test for them.
    assertThat(horses)
        .extracting("id", "name", "sex", "dateOfBirth", "breed.name")
        .as("ID, Name, Sex, Date of Birth, Breed Name")
        .containsOnly(
            tuple(-32L, "Luna", Sex.FEMALE, LocalDate.of(2018, 10, 10), "Welsh Cob"),
            tuple(-21L, "Bella", Sex.FEMALE, LocalDate.of(2003, 7, 6), "Welsh Cob"),
            tuple(-2L, "Hugo", Sex.MALE, LocalDate.of(2020, 2, 20), "Welsh Pony")
        );
  }

  @Test
  public void searchByBirthDateBetween2017And2018ReturnsFourHorses() throws ValidationException {
    var searchDto = new HorseSearchDto(null, null,
        LocalDate.of(2017, 3, 5),
        LocalDate.of(2018, 10, 10),
        null, null);
    var horses = horseService.search(searchDto);
    assertNotNull(horses);
    assertThat(horses)
        .hasSize(4)
        .extracting(HorseListDto::id, HorseListDto::name, HorseListDto::sex, HorseListDto::dateOfBirth, (h) -> h.breed().name())
        .containsExactlyInAnyOrder(
            tuple(-24L, "Rocky", Sex.MALE, LocalDate.of(2018, 8, 19),
                "Dartmoor Pony"),
            tuple(-26L, "Daisy", Sex.FEMALE, LocalDate.of(2017, 12, 1),
                "Hanoverian"),
            tuple(-31L, "Leo", Sex.MALE, LocalDate.of(2017, 3, 5),
                "Haflinger"),
            tuple(-32L, "Luna", Sex.FEMALE, LocalDate.of(2018, 10, 10),
                "Welsh Cob"));
  }

  @Test
  public void getByIdPositive() throws NotFoundException {
    HorseDetailDto horse = horseService.getById(-1L);
    AssertionsForClassTypes.assertThat(horse).isNotNull();
    AssertionsForClassTypes.assertThat(horse.name()).isEqualTo("Wendy");
    AssertionsForClassTypes.assertThat(horse.dateOfBirth().toString()).isEqualTo("2022-10-12");
    AssertionsForClassTypes.assertThat(horse.sex()).isEqualTo(Sex.FEMALE);
    AssertionsForClassTypes.assertThat(horse.height()).isEqualTo(1.40);
    AssertionsForClassTypes.assertThat(horse.weight()).isEqualTo(380);
    AssertionsForClassTypes.assertThat(horse.breed().id()).isEqualTo(-15);
  }

  @Test
  public void getByIdNegative() {
    Assertions.assertThrows(NotFoundException.class, () -> horseService.getById(-100L));
  }

  @Test
  public void createEditDeletePositive() throws ValidationException, ConflictException, NotFoundException {
    HorseCreateDto horseCreateDto = new HorseCreateDto( "Some New Horse", Sex.MALE,
            LocalDate.parse("2020-01-01"), 0, 0, null);

    HorseCreateDto returnHorse = horseService.create(horseCreateDto);
    AssertionsForClassTypes.assertThat(returnHorse).isNotNull();
    AssertionsForClassTypes.assertThat(returnHorse.name()).isEqualTo("Some New Horse");
    AssertionsForClassTypes.assertThat(returnHorse.sex()).isEqualTo(Sex.MALE);
    AssertionsForClassTypes.assertThat(returnHorse.dateOfBirth()).isEqualTo(LocalDate.parse("2020-01-01"));


    returnHorse = new HorseCreateDto( "Horsy",
            Sex.FEMALE, null, 0, 0, null);
    HorseDetailDto horseDetailDto = horseMapper.entityToDetailDto(horseMapper.createDtoToEntity(returnHorse), null);
    horseService.update(horseDetailDto);

    AssertionsForClassTypes.assertThat(horseDetailDto).isNotNull();
    AssertionsForClassTypes.assertThat(horseDetailDto.name()).isEqualTo("Horsy");
    AssertionsForClassTypes.assertThat(horseDetailDto.sex()).isEqualTo(Sex.FEMALE);
    AssertionsForClassTypes.assertThat(horseDetailDto.dateOfBirth()).isEqualTo(LocalDate.parse("2020-01-01"));

    horseService.delete(horseDetailDto.id());
    HorseDetailDto finalHorseDto = horseDetailDto;
    Assertions.assertThrows(NotFoundException.class, () -> horseService.getById(finalHorseDto.id()));
  }

  @Test
  public void createEditNegative() {
    HorseCreateDto horseDto = new HorseCreateDto(null, null, null, 0, 0, null);
    Assertions.assertThrows(ValidationException.class, () -> horseService.create(horseDto));

    Assertions.assertThrows(ConflictException.class, () -> horseService.update(horseMapper.entityToDetailDto(horseMapper.createDtoToEntity(horseDto), null)));
  }
}
