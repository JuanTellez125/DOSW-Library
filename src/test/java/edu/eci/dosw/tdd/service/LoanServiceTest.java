package edu.eci.dosw.tdd.service;

import edu.eci.dosw.tdd.controller.dto.request.LoanRequestDTO;
import edu.eci.dosw.tdd.controller.dto.response.LoanResponseDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.enums.Status;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.relational.repository.BookRepository;
import edu.eci.dosw.tdd.persistence.relational.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.relational.repository.UserRepository;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private User user;
    private Loan loan;
    private LoanRequestDTO requestDTO;
    private LoanResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id("book1")
                .title("Clean Code")
                .author("Robert Martin")
                .build();

        user = User.builder()
                .id("user1")
                .name("Juan")
                .build();

        loan = Loan.builder()
                .book(book)
                .user(user)
                .loanDate(LocalDate.now())
                .status(Status.ACTIVE)
                .build();

        requestDTO = LoanRequestDTO.builder()
                .bookId("book1")
                .userId("user1")
                .build();

        responseDTO = LoanResponseDTO.builder()
                .bookTitle("Clean Code")
                .userName("Juan")
                .status(Status.ACTIVE)
                .loanDate(LocalDate.now())
                .build();
    }

    @Test
    void shouldCreateLoanSuccessfully() {
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(bookRepository.findById("book1")).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(loanMapper.toDto(loan)).thenReturn(responseDTO);

        LoanResponseDTO result = loanService.createLoan(requestDTO);

        assertNotNull(result);
        assertEquals("Clean Code", result.getBookTitle());
        assertEquals("Juan", result.getUserName());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnCreate() {
        when(userRepository.findById("user1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> loanService.createLoan(requestDTO));

        verify(loanRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnCreate() {
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(bookRepository.findById("book1")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> loanService.createLoan(requestDTO));

        verify(loanRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyHasActiveLoan() {
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(bookRepository.findById("book1")).thenReturn(Optional.of(book));
        when(loanRepository.existsByUserIdAndStatus("user1", Status.ACTIVE)).thenReturn(true);

        assertThrows(LoanLimitExceededException.class,
                () -> loanService.createLoan(requestDTO));

        verify(loanRepository, never()).save(any());
    }

    @Test
    void shouldFindActiveLoansSuccessfully() {
        when(loanRepository.findByStatus(Status.ACTIVE)).thenReturn(List.of(loan));
        when(loanMapper.toDto(loan)).thenReturn(responseDTO);

        List<LoanResponseDTO> result = loanService.findActiveLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void shouldFindLoanHistorySuccessfully() {
        when(loanRepository.findAll()).thenReturn(List.of(loan));
        when(loanMapper.toDto(loan)).thenReturn(responseDTO);

        List<LoanResponseDTO> result = loanService.findLoanHistory();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

}