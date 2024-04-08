package at.ac.tuwien.sepr.assignment.individual.persistence;

import at.ac.tuwien.sepr.assignment.individual.dto.BreedSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Breed;
import java.util.Collection;
import java.util.Set;

/**
 * Data Access Object for breeds.
 * Implements access functionality to the application's persistent data store regarding breeds.
 */
public interface BreedDao {
  /**
   * Return all the breeds in the persistent data store
   *
   * @return a collection containing all breeds
   */
  Collection<Breed> allBreeds();

  /**
   * Get breeds by the given id
   *
   * @param breedIds given ID matching the breed(s)
   * @return a collection of all breeds matching the given ID
   */
  Collection<Breed> findBreedsById(Set<Long> breedIds);

  /**
   * Search for breed(s) through the inputted search parameters
   *
   * @param searchParams search parameters for looking for the special breed
   * @return a collection of matching breed(s)
   */
  Collection<Breed> search(BreedSearchDto searchParams);
}
