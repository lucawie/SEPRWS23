package at.ac.tuwien.sepr.assignment.individual.rest;


import at.ac.tuwien.sepr.assignment.individual.dto.*;
import at.ac.tuwien.sepr.assignment.individual.exception.ConflictException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = TournamentEndpoint.BASE_PATH)
public class TournamentEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static final String BASE_PATH = "/tournaments";

    private final TournamentService service;

    public TournamentEndpoint(TournamentService service) {
        this.service = service;
    }

    @GetMapping
    public Stream<TournamentListDto> searchTournament(TournamentSearchDto searchParameters) {
        LOG.info("GET " + BASE_PATH);
        LOG.debug("request parameters: {}", searchParameters);
        return service.search(searchParameters);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TournamentDetailDto create(@RequestBody TournamentCreateDto tournamentCreateDto) throws ValidationException, ConflictException, NotFoundException {
        LOG.info("POST " + BASE_PATH + "/{} query parameters: {}", tournamentCreateDto, tournamentCreateDto);
        LOG.debug("Body of tournament:\n{}", tournamentCreateDto);
        return service.create(tournamentCreateDto);
    }

    @GetMapping("{id}")
    public TournamentDetailDto getTournamentById(@PathVariable long id) throws NotFoundException, ValidationException {
        LOG.info("GET " + BASE_PATH + "/{}", id);
        return service.getTournamentDetailById(id);
    }

    @GetMapping("{id}/standings")
    public TournamentStandingsDto getTournamentStandingById(@PathVariable long id) throws NotFoundException, ValidationException {
        LOG.info("GET " + BASE_PATH + "/{}", id);
        return service.getTournamentStandingById(id);
    }
}
