package at.ac.tuwien.sepr.assignment.individual.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import at.ac.tuwien.sepr.assignment.individual.TestBase;
import at.ac.tuwien.sepr.assignment.individual.dto.BreedDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.mapper.HorseMapper;
import at.ac.tuwien.sepr.assignment.individual.type.Sex;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
public class HorseDaoTest extends TestBase {

  @Autowired
  HorseDao horseDao;

  @Autowired
  HorseMapper horseMapper;

  @Test
  public void searchByBreedWelFindsThreeHorses() {
    var searchDto = new HorseSearchDto(null, null, null, null, "Wel", null);
    var horses = horseDao.search(searchDto);
    assertNotNull(horses);
    assertThat(horses)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrder(
            (new Horse())
                .setId(-32L)
                .setName("Luna")
                .setSex(Sex.FEMALE)
                .setDateOfBirth(LocalDate.of(2018, 10, 10))
                .setHeight(1.62f)
                .setWeight(670)
                .setBreedId(-19L),
            (new Horse())
                .setId(-21L)
                .setName("Bella")
                .setSex(Sex.FEMALE)
                .setDateOfBirth(LocalDate.of(2003, 7, 6))
                .setHeight(1.50f)
                .setWeight(580)
                .setBreedId(-19L),
            (new Horse())
                .setId(-2L)
                .setName("Hugo")
                .setSex(Sex.MALE)
                .setDateOfBirth(LocalDate.of(2020, 2, 20))
                .setHeight(1.20f)
                .setWeight(320)
                .setBreedId(-20L));
  }

  @Test
  public void searchByBirthDateBetween2017And2018ReturnsFourHorses() {
    var searchDto = new HorseSearchDto(null, null,
        LocalDate.of(2017, 3, 5),
        LocalDate.of(2018, 10, 10),
        null, null);
    var horses = horseDao.search(searchDto);
    assertNotNull(horses);
    assertThat(horses)
        .hasSize(4)
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrder(
            (new Horse())
                .setId(-24L)
                .setName("Rocky")
                .setSex(Sex.MALE)
                .setDateOfBirth(LocalDate.of(2018, 8, 19))
                .setHeight(1.42f)
                .setWeight(480)
                .setBreedId(-6L),
            (new Horse())
                .setId(-26L)
                .setName("Daisy")
                .setSex(Sex.FEMALE)
                .setDateOfBirth(LocalDate.of(2017, 12, 1))
                .setHeight(1.28f)
                .setWeight(340)
                .setBreedId(-9L),
            (new Horse())
                .setId(-31L)
                .setName("Leo")
                .setSex(Sex.MALE)
                .setDateOfBirth(LocalDate.of(2017, 3, 5))
                .setHeight(1.70f)
                .setWeight(720)
                .setBreedId(-8L),
            (new Horse())
                .setId(-32L)
                .setName("Luna")
                .setSex(Sex.FEMALE)
                .setDateOfBirth(LocalDate.of(2018, 10, 10))
                .setHeight(1.62f)
                .setWeight(670)
                .setBreedId(-19L));
  }

  @Test
  public void getByIdPositive() throws NotFoundException {
    Horse horse = horseDao.getById(-1L);
    AssertionsForClassTypes.assertThat(horse).isNotNull();
    AssertionsForClassTypes.assertThat(horse.getName()).isEqualTo("Wendy");
    AssertionsForClassTypes.assertThat(horse.getDateOfBirth().toString()).isEqualTo("2022-10-12");
    AssertionsForClassTypes.assertThat(horse.getSex()).isEqualTo(Sex.FEMALE);
    AssertionsForClassTypes.assertThat(horse.getHeight()).isEqualTo(1.40);
    AssertionsForClassTypes.assertThat(horse.getWeight()).isEqualTo(380);
    AssertionsForClassTypes.assertThat(horse.getBreedId()).isEqualTo(-15);
  }

  @Test
  public void getByIdNegative() {
    Assertions.assertThrows(NotFoundException.class, () -> horseDao.getById(-100L));
  }

  @Test
  public void createEditDeletePositive() throws NotFoundException {
    Horse horse = new Horse();
    horse.setName("Some Horse");
    horse.setSex(Sex.MALE);
    horse.setDateOfBirth(LocalDate.parse("2020-01-01"));
    horse.setHeight(1.50F);
    horse.setWeight(200);
    Horse returnHorse = horseDao.create(horseMapper.entityToCreateDto(horse, null));
    Horse insertedHorse = horseDao.getById(returnHorse.getId());
    AssertionsForClassTypes.assertThat(insertedHorse).isNotNull();
    AssertionsForClassTypes.assertThat(insertedHorse.getName()).isEqualTo("Some Horse");
    AssertionsForClassTypes.assertThat(insertedHorse.getSex()).isEqualTo(Sex.MALE);
    AssertionsForClassTypes.assertThat(insertedHorse.getDateOfBirth().equals(LocalDate.parse("2020-01-01"))).isTrue();
    AssertionsForClassTypes.assertThat(horse.getHeight()).isEqualTo(1.50F);
    AssertionsForClassTypes.assertThat(horse.getWeight()).isEqualTo(200);


    horseDao.update(horseMapper.entityToDetailDto(horse, null));

    insertedHorse = horseDao.getById(returnHorse.getId());
    AssertionsForClassTypes.assertThat(insertedHorse).isNotNull();
    AssertionsForClassTypes.assertThat(insertedHorse.getName()).isEqualTo("Some Horse");
    AssertionsForClassTypes.assertThat(insertedHorse.getSex()).isEqualTo(Sex.MALE);
    AssertionsForClassTypes.assertThat(insertedHorse.getDateOfBirth().equals(LocalDate.parse("2020-01-01"))).isTrue();
    AssertionsForClassTypes.assertThat(horse.getHeight()).isEqualTo(1.50F);
    AssertionsForClassTypes.assertThat(horse.getWeight()).isEqualTo(200);

    horseDao.delete(horse.getId());
    Assertions.assertThrows(NotFoundException.class, () -> horseDao.getById(horse.getId()));
  }

  @Test
  public void createEditNegative() {
    //Cannot access specific Data if sent directly to persistence layer
    Assertions.assertThrows(NullPointerException.class, () -> horseDao.create(horseMapper.entityToCreateDto(new Horse(), null)));
    Assertions.assertThrows(BadSqlGrammarException.class, () -> horseDao.update(new HorseDetailDto(null, null, null, null, 0, 0, null)));
  }
}
