package org.gayan.dls.repository;


import org.gayan.dls.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/19/25
 * Time: 10:29 PM
 */
@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, UUID> {
}
