package tn.reboistn.utilisateurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.reboistn.utilisateurs.model.Organisation;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
}
