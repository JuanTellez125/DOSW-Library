package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.controller.dto.request.LoanRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.LoanResponseDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.exception.*;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.enums.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.repository.LoanRepository;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;

    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO dto) {

        // Verificar que el usuario existe
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + dto.getUserId()));

        // Verificar que el libro existe
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con id: " + dto.getBookId()));

        // Verificar que el usuario no tenga un préstamo activo
        boolean tienePrestamoActivo = loanRepository.existsByUserIdAndStatus(dto.getUserId(), Status.ACTIVE);
        if (tienePrestamoActivo) {
            throw new LoanLimitExceededException("El usuario ya tiene un préstamo activo");
        }

        // Crear el préstamo
        Loan loan = Loan.builder()
                .book(book)
                .user(user)
                .loanDate(LocalDate.now())
                .status(Status.ACTIVE)
                .build();

        Loan savedLoan = loanRepository.save(loan);
        return loanMapper.toDto(savedLoan);
    }

    @Transactional
    public LoanResponseDTO updateLoan(String userId, LoanRequestDTO dto) {
        Loan loan = loanRepository.findByUserId(userId)
                .orElseThrow(() -> new LoanNotFoundException("Préstamo no encontrado para el usuario: " + userId));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado con id: " + dto.getBookId()));

        loan.setBook(book);

        Loan updatedLoan = loanRepository.save(loan);
        return loanMapper.toDto(updatedLoan);
    }

    public List<LoanResponseDTO> findActiveLoans() {
        return loanRepository.findByStatus(Status.ACTIVE).stream()
                .map(loanMapper::toDto)
                .toList();
    }

    public List<LoanResponseDTO> findLoanHistory() {
        return loanRepository.findAll().stream()
                .map(loanMapper::toDto)
                .toList();
    }

}