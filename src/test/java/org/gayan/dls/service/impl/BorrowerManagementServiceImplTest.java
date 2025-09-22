package org.gayan.dls.service.impl;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 9/22/25
 * Time: 11:39 PM
 */

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gayan.dls.dto.BorrowerRequestDto;
import org.gayan.dls.dto.BorrowerResponseDto;
import org.gayan.dls.dto.generic.ApiResponse;
import org.gayan.dls.entity.Borrower;
import org.gayan.dls.exception.BorrowerException;
import org.gayan.dls.exception.NoContentFoundException;
import org.gayan.dls.mapper.BorrowerMapper;
import org.gayan.dls.repository.BorrowerRepository;
import org.gayan.dls.util.DlsResponseBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BorrowerManagementServiceImplTest {

    @Mock private BorrowerRepository borrowerRepository;
    @Mock private BorrowerMapper borrowerMapper;
    @Mock private DlsResponseBuilder responseBuilder;

    @InjectMocks private BorrowerManagementServiceImpl borrowerService;

    // Reusable test objects
    private BorrowerRequestDto requestDtoMock;
    private Borrower borrowerEntity;
    private Borrower savedBorrower;
    private BorrowerResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // BorrowerRequestDto is a record in your codebase; we don't know its constructor here,
        // so use a Mockito mock for the request DTO to avoid depending on the record shape.
        requestDtoMock = mock(BorrowerRequestDto.class);

        borrowerEntity = mock(Borrower.class);
        savedBorrower = mock(Borrower.class);
        responseDto = new BorrowerResponseDto(); // assuming this class has a no-arg ctor; if not, mock it:
        // responseDto = mock(BorrowerResponseDto.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void persistBorrower_whenEmailAlreadyExists_shouldThrowBorrowerException() {
        // Arrange
        when(borrowerRepository.findBorrowerByEmail(any())).thenReturn(Optional.of(borrowerEntity));

        // Act & Assert
        BorrowerException ex =
                assertThrows(
                        BorrowerException.class,
                        () -> borrowerService.persistBorrower(requestDtoMock));
        // basic assertion about conflict status message presence (optional)
        assertNotNull(ex.getMessage());
        verify(borrowerRepository).findBorrowerByEmail(any());
        verifyNoMoreInteractions(borrowerRepository, borrowerMapper, responseBuilder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void persistBorrower_whenNewBorrower_shouldSaveAndReturnResponse() throws Exception {
        // Arrange
        when(borrowerRepository.findBorrowerByEmail(any())).thenReturn(Optional.empty());
        when(borrowerMapper.mapBorrowerRequestDtoToBorrowerEntity(requestDtoMock))
                .thenReturn(borrowerEntity);
        when(borrowerRepository.save(borrowerEntity)).thenReturn(savedBorrower);
        when(borrowerMapper.mapBorrowerEntityToBorrowerResponseDto(savedBorrower))
                .thenReturn(responseDto);

        ResponseEntity<ApiResponse<BorrowerResponseDto>> returned = (ResponseEntity) ResponseEntity.ok().build();
        when(responseBuilder.success(responseDto, "Borrower registration success !")).thenReturn(returned);

        // Act
        ResponseEntity<ApiResponse<BorrowerResponseDto>> result =
                borrowerService.persistBorrower(requestDtoMock);

        // Assert
        assertSame(returned, result);
        verify(borrowerRepository).findBorrowerByEmail(any());
        verify(borrowerMapper).mapBorrowerRequestDtoToBorrowerEntity(requestDtoMock);
        verify(borrowerRepository).save(borrowerEntity);
        verify(borrowerMapper).mapBorrowerEntityToBorrowerResponseDto(savedBorrower);
        verify(responseBuilder).success(responseDto, "Borrower registration success !");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void getBorrowerById_whenFound_shouldReturnDto() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        when(borrowerRepository.findById(id)).thenReturn(Optional.of(borrowerEntity));
        when(borrowerMapper.mapBorrowerEntityToBorrowerResponseDto(borrowerEntity))
                .thenReturn(responseDto);

        ResponseEntity<ApiResponse<BorrowerResponseDto>> returned = (ResponseEntity) ResponseEntity.ok().build();
        when(responseBuilder.success(responseDto, "Borrower found successfully !")).thenReturn(returned);

        // Act
        ResponseEntity<ApiResponse<BorrowerResponseDto>> result =
                borrowerService.getBorrowerById(idString);

        // Assert
        assertSame(returned, result);
        verify(borrowerRepository).findById(id);
        verify(borrowerMapper).mapBorrowerEntityToBorrowerResponseDto(borrowerEntity);
        verify(responseBuilder).success(responseDto, "Borrower found successfully !");
    }

    @Test
    void getBorrowerById_whenNotFound_shouldThrowNoContentFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        when(borrowerRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoContentFoundException.class, () -> borrowerService.getBorrowerById(idString));
        verify(borrowerRepository).findById(id);
        verifyNoMoreInteractions(borrowerMapper, responseBuilder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void getAllBorrowersWithPagination_whenPageHasContent_shouldReturnPagedDto() throws Exception {
        // Arrange
        Borrower b1 = mock(Borrower.class);
        Borrower b2 = mock(Borrower.class);
        List<Borrower> content = List.of(b1, b2);
        Page<Borrower> page = new PageImpl<>(content);

        Pageable pageable = mock(Pageable.class);
        when(borrowerRepository.findAll(pageable)).thenReturn(page);

        BorrowerResponseDto dto1 = new BorrowerResponseDto();
        when(borrowerMapper.mapBorrowerEntityToBorrowerResponseDto(b1)).thenReturn(dto1);

        Page<BorrowerResponseDto> expectedPage = page.map(borrowerMapper::mapBorrowerEntityToBorrowerResponseDto);

        ResponseEntity<ApiResponse<Page<BorrowerResponseDto>>> returned = (ResponseEntity) ResponseEntity.ok().build();
        when(responseBuilder.success(expectedPage, "Borrower find all success !")).thenReturn(returned);

        // Act
        ResponseEntity<ApiResponse<Page<BorrowerResponseDto>>> result =
                borrowerService.getAllBorrowersWithPagination(pageable);

        // Assert
        assertSame(returned, result);
        verify(borrowerRepository).findAll(pageable);
        // mapping may be invoked more than once due to Page.map laziness — use atLeastOnce to avoid flakiness
        verify(borrowerMapper, atLeastOnce()).mapBorrowerEntityToBorrowerResponseDto(any(Borrower.class));
        verify(responseBuilder).success(expectedPage, "Borrower find all success !");
    }

    @Test
    void getAllBorrowersWithPagination_whenEmpty_shouldThrowNoContentFoundException() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<Borrower> empty = Page.empty();
        when(borrowerRepository.findAll(pageable)).thenReturn(empty);

        // Act & Assert
        assertThrows(NoContentFoundException.class, () -> borrowerService.getAllBorrowersWithPagination(pageable));
        verify(borrowerRepository).findAll(pageable);
        verifyNoMoreInteractions(borrowerMapper, responseBuilder);
    }
}