package at.ac.tuwien.sepr.assignment.individual.service;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseSelectionDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TournamentValidator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Validation function for creating new tournaments
     *
     * @param tournamentCreateDto whos data needs to be checked
     * @throws ValidationException if horses parameters are not okay
     */
    public void validateCreate(TournamentCreateDto tournamentCreateDto, List<Long> horseIds) throws ValidationException {
        LOG.trace("validateForCreate({})", tournamentCreateDto);
        List<String> validationErrors = new ArrayList<>();

        if (tournamentCreateDto.name() == null || tournamentCreateDto.name().isBlank()) {
            validationErrors.add("No name given");
        } else if (tournamentCreateDto.name().length() > 255) {
            validationErrors.add("Length of name must not exceed 255 characters");
        }
        if (!tournamentCreateDto.name().matches("[a-zA-Z]+")) {
            validationErrors.add("Only letters and spaces allowed in name");
        }

        if (tournamentCreateDto.startDate() == null) {
            validationErrors.add("No start date given");
        }

        if (tournamentCreateDto.endDate() == null) {
            validationErrors.add("No end date given");
        } else if (tournamentCreateDto.endDate().isBefore(tournamentCreateDto.startDate())) {
            validationErrors.add("Start date must be before end date");
        }

        if (tournamentCreateDto.participants() == null) {
            validationErrors.add("No participants given");
        } else {
            if (!allHorseIdsExist(tournamentCreateDto.participants(), horseIds)) {
                validationErrors.add("One or more of the participants have invalid horse IDs - Make sure the ID is spelled 'id' and the horse actually exists already");
            }
        }


        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of horse for update failed", validationErrors);
        }
    }

    /**
     * Function for checking if every horse Id of the participants does exist
     *
     * @param participants of the tournament
     * @param horseIds list with all existing horse Ids
     * @return true if every Id is existing
     */
    private boolean allHorseIdsExist(HorseSelectionDto[] participants, List<Long> horseIds) {
        LOG.trace("allHorseIDsExist({})", participants);

        for (HorseSelectionDto participant : participants) {
            if (participant != null) {
                if (participant.id() == 0) {
                    return false;
                }
                int counter = 0;

                for (Long horseId : horseIds) {
                    if (participant.id() == horseId) {
                        counter++;
                    }
                }
                if (counter == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void validateForID(long id) throws ValidationException {
        LOG.trace("validateForID({})", id);
        if (id == 0) {
            throw new ValidationException("ID-Validation failed", List.of("ID cannot be null"));
        }
    }
}
