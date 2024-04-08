package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HorseValidator {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Validation function for updating existing horses
   *
   * @param horse whos new data needs to be checked
   * @throws ValidationException if horses new parameters are not okay
   */
  public void validateForUpdate(HorseDetailDto horse) throws ValidationException {
    LOG.trace("validateForUpdate({})", horse);
    List<String> validationErrors = new ArrayList<>();

    //check if id is given
    if (horse.id() == null) {
      validationErrors.add("No ID given");
    }

    //check name
    if (horse.name() == null || horse.name().isBlank()) {
      validationErrors.add("No name given");
    } else {
      if (horse.name().length() > 255) {
        validationErrors.add("Length of name must not exceed 255 characters");
      }
      if (!horse.name().matches("[a-zA-Z]+")) {
        validationErrors.add("Only letters and spaces allowed in name");
      }
    }


    //check if sex is given
    if (horse.sex() == null) {
      validationErrors.add("No sex given");
    }

    //check date of birth
    if (horse.dateOfBirth() == null) {
      validationErrors.add("No date of birth given");
    } else if (horse.dateOfBirth().isAfter(LocalDate.now())) {
      validationErrors.add("Date of birth cannot be in the future");
    }

    //check height
    if (horse.height() <= 0 || horse.height() >= 3) {
      validationErrors.add("Height must be between 0 and 3");
    }

    //check weight
    if (horse.weight() <= 30 || horse.weight() >= 1000) {
      validationErrors.add("Weight must be between 30 and 1000");
    }

    //check if breed is given
    if (horse.breed() != null) {
      if (horse.breed().id() == 0) {
        validationErrors.add("No breed Id given");
      }
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Update validation of horse failed", validationErrors);
    }
  }

  /**
   * Validation function for creating new horses
   *
   * @param horse whos data needs to be checked
   * @throws ValidationException if horses parameters are not okay
   */
  public void validateCreate(HorseCreateDto horse) throws ValidationException {
    LOG.trace("validateForCreate({})", horse);
    List<String> validationErrors = new ArrayList<>();

    if (horse.name() == null || horse.name().isBlank()) {
      validationErrors.add("No name given");
    } else {
      if (horse.name().length() > 255) {
        validationErrors.add("Length of name must not exceed 255 characters");
      }
      if (!horse.name().matches("[a-zA-Z]+")) {
        validationErrors.add("Only letters and spaces allowed in name");
      }
    }

    if (horse.sex() == null) {
      validationErrors.add("No sex given");
    }

    if (horse.dateOfBirth() == null) {
      validationErrors.add("No date of birth given");
    } else if (horse.dateOfBirth().isAfter(LocalDate.now())) {
      validationErrors.add("Date of birth cannot be in the future");
    }

    if (horse.height() <= 0 || horse.height() >= 3) {
      validationErrors.add("Height must be between 0 and 3");
    }

    if (horse.weight() <= 30 || horse.weight() >= 1000) {
      validationErrors.add("Weight must be between 30 and 1000");
    }

    if (horse.breed() != null) {
      if (horse.breed().id() == 0) {
        validationErrors.add("No breed ID given");
      }
    }

    if (!validationErrors.isEmpty()) {
      throw new ValidationException("Validation of horse for update failed", validationErrors);
    }
  }

}
