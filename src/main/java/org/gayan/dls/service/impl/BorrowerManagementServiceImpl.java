package org.gayan.dls.service.impl;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gayan.dls.dto.BorrowerRequestDto;
import org.gayan.dls.dto.BorrowerResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.entity.Borrower;
import org.gayan.dls.exception.BookException;
import org.gayan.dls.exception.BorrowerException;
import org.gayan.dls.exception.NoContentFoundException;
import org.gayan.dls.mapper.BorrowerMapper;
import org.gayan.dls.repository.BorrowerRepository;
import org.gayan.dls.service.BorrowerManagementService;
import org.gayan.dls.util.DlsResponseBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing borrower-related operations. Responsibilities: - Registering
 * new borrowers with email uniqueness enforcement. - Fetching borrower details by ID. - Listing
 * borrowers with pagination support. Transaction boundaries are managed at the repository level, as
 * these are simple CRUD operations. Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 12:29 AM
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowerManagementServiceImpl implements BorrowerManagementService {

  private final BorrowerRepository borrowerRepository;
  private final BorrowerMapper borrowerMapper;
  private final DlsResponseBuilder responseBuilder;

  /**
   * Registers a new borrower in the system.
   *
   * <p>Flow: 1. Validate uniqueness of borrower by email (enforces system-wide unique email
   * constraint). 2. Map request DTO to entity and persist in the database. 3. Return borrower
   * details in response.
   *
   * @param borrowerRequestDto borrower registration details
   * @return ApiResponse with borrower details
   * @throws BorrowerException if a borrower already exists with the given email
   */
  @Override
  public ResponseEntity<ApiResponse<BorrowerResponseDto>> persistBorrower(
      BorrowerRequestDto borrowerRequestDto) throws BookException {

    Optional<Borrower> borrowerByEmail =
        borrowerRepository.findBorrowerByEmail(borrowerRequestDto.email());
    if (borrowerByEmail.isPresent()) {
      throw new BorrowerException(
          "Borrower already exist in the system for the given email address", HttpStatus.CONFLICT);
    }
    Borrower savedBorrower =
        borrowerRepository.save(
            borrowerMapper.mapBorrowerRequestDtoToBorrowerEntity(borrowerRequestDto));
    return responseBuilder.success(
        borrowerMapper.mapBorrowerEntityToBorrowerResponseDto(savedBorrower),
        "Borrower registration success !");
  }

  /**
   * Retrieves borrower details by ID.
   *
   * <p>Flow: 1. Validate borrower existence by ID. 2. Map entity to response DTO.
   *
   * @param borrowerId borrower UUID as String
   * @return ApiResponse with borrower details
   * @throws NoContentFoundException if borrower not found
   */
  @Override
  public ResponseEntity<ApiResponse<BorrowerResponseDto>> getBorrowerById(String borrowerId)
      throws BookException {
    Borrower borrower =
        borrowerRepository
            .findById(UUID.fromString(borrowerId))
            .orElseThrow(NoContentFoundException::new);
    return responseBuilder.success(
        borrowerMapper.mapBorrowerEntityToBorrowerResponseDto(borrower),
        "Borrower found successfully !");
  }

  /**
   * Retrieves all borrowers in a paginated format.
   *
   * <p>Flow: 1. Query borrowers with pagination. 2. If no borrowers found, throw a
   * NoContentFoundException. 3. Map entities to response DTOs.
   *
   * @param pageable pagination configuration (page, size, sort)
   * @return ApiResponse containing paged borrower list
   * @throws NoContentFoundException if no borrowers exist
   */
  @Override
  public ResponseEntity<ApiResponse<Page<BorrowerResponseDto>>> getAllBorrowersWithPagination(
      Pageable pageable) throws BookException {
    Page<Borrower> findAllBorrowers = borrowerRepository.findAll(pageable);
    if (findAllBorrowers.isEmpty()) {
      throw new NoContentFoundException();
    }
    Page<BorrowerResponseDto> pagedResponse =
        findAllBorrowers.map(borrowerMapper::mapBorrowerEntityToBorrowerResponseDto);
    return responseBuilder.success(pagedResponse, "Borrower find all success !");
  }
}
