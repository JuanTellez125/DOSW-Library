package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.request.BookRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.BookResponseDTO;
import edu.eci.dosw.tdd.core.model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toEntity(BookRequestDTO dto);

    BookResponseDTO toDto(Book book);

}
