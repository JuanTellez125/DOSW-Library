package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.controller.dto.request.BookRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.BookResponseDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookResponseDTO createBook(BookRequestDTO dto){
        Book book = bookMapper.toEntity(dto);

        Book saveBook = bookRepository.save(book);

        return bookMapper.toDto(saveBook);
    }

    @Transactional
    public BookResponseDTO updateBook(String title, BookRequestDTO dto){
        Book book = bookRepository.findByTitle(title);
    }

    @Transactional
    public void deleteBook(String id){
        Book book = bookRepository.findById(id);
        bookRepository.delete(book);
    }

    public List<BookResponseDTO> findAllBooks(){
        return BookRepository.findAll().stream().map(bookMapper::toDto)
                .toList();
    }
}
