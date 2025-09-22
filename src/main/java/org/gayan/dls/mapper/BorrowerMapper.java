package org.gayan.dls.mapper;

import java.time.LocalDateTime;
import org.gayan.dls.dto.*;
import org.gayan.dls.entity.Borrower;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Author: Gayan Sanjeewa User: gayan Date: 9/20/25 Time: 10:07 PM */
@Mapper(
    componentModel = "spring",
    imports = {LocalDateTime.class})
public interface BorrowerMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "name", target = "name")
  @Mapping(source = "email", target = "email")
  @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
  @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
  Borrower mapBorrowerRequestDtoToBorrowerEntity(BorrowerRequestDto borrowerRequestDto);

  // --------- Book mapping without copies ----------
  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "updatedAt", target = "updatedAt")
  BorrowerResponseDto mapBorrowerEntityToBorrowerResponseDto(Borrower borrower);
}
