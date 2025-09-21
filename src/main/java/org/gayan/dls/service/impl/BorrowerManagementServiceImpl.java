package org.gayan.dls.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gayan.dls.dto.BookRequestDto;
import org.gayan.dls.dto.BookResponseDto;
import org.gayan.dls.dto.BorrowerRequestDto;
import org.gayan.dls.dto.BorrowerResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.entity.Book;
import org.gayan.dls.entity.Borrower;
import org.gayan.dls.exception.BookException;
import org.gayan.dls.exception.BorrowerException;
import org.gayan.dls.exception.NoContentFoundException;
import org.gayan.dls.mapper.BookMapper;
import org.gayan.dls.mapper.BorrowerMapper;
import org.gayan.dls.repository.BookRepository;
import org.gayan.dls.repository.BorrowerRepository;
import org.gayan.dls.service.BookManagementService;
import org.gayan.dls.service.BorrowerManagementService;
import org.gayan.dls.util.ResponseBuilder;
import org.gayan.dls.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.gayan.dls.constant.ResponseMessage.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/20/25
 * Time: 12:29 AM
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowerManagementServiceImpl implements BorrowerManagementService {

    private final BorrowerRepository borrowerRepository;
    private final BorrowerMapper borrowerMapper;
    private final ResponseBuilder responseBuilder;

    @Override
    public ResponseEntity<ApiResponse<BorrowerResponseDto>> persistBorrower(BorrowerRequestDto borrowerRequestDto) throws BookException {

        Optional<Borrower> borrowerByEmail = borrowerRepository.findBorrowerByEmail(borrowerRequestDto.getEmail());
        if(borrowerByEmail.isPresent()){
            throw new BorrowerException("Borrower already exist in the system for the given email address" , HttpStatus.CONFLICT);
        }
        Borrower savedBorrower = borrowerRepository.save(borrowerMapper.mapBorrowerRequestDtoToBorrowerEntity(borrowerRequestDto));
        return responseBuilder.success(borrowerMapper.mapBorrowerEntityToBorrowerResponseDto(savedBorrower) , "Borrower registration success !");
    }

    @Override
    public ResponseEntity<ApiResponse<BorrowerResponseDto>> getBorrowerById(String borrowerId) throws BookException {
        Borrower borrower = borrowerRepository.findById(UUID.fromString(borrowerId)).orElseThrow(NoContentFoundException::new);
        return responseBuilder.success(borrowerMapper.mapBorrowerEntityToBorrowerResponseDto(borrower) , "Borrower found successfully !");
    }

    @Override
    public ResponseEntity<ApiResponse<Page<BorrowerResponseDto>>> getAllBorrowersWithPagination(Pageable pageable) throws BookException {
        Page<Borrower> findAllBorrowers = borrowerRepository.findAll(pageable);
        if(findAllBorrowers.isEmpty()){
            throw new NoContentFoundException();
        }
        Page<BorrowerResponseDto> pagedResponse = findAllBorrowers.map(borrowerMapper::mapBorrowerEntityToBorrowerResponseDto);
        return responseBuilder.success(pagedResponse , "Borrower find all success !");
    }
}
