package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mapper Tests")
class MapperTest {

    private final BookMapper bookMapper = new BookMapper();
    private final UserMapper userMapper = new UserMapper();
    private final LoanMapper loanMapper = new LoanMapper();

    // ─── BookMapper ───

    @Test
    @DisplayName("[SUCCESS] BookMapper toDTO maps all fields")
    void bookMapperToDTO() {
        Book book = new Book("B001", "Clean Code", "R. Martin", "ISBN");
        BookDTO dto = bookMapper.toDTO(book, 3);

        assertEquals("B001", dto.getId());
        assertEquals("Clean Code", dto.getTitle());
        assertEquals("R. Martin", dto.getAuthor());
        assertEquals("ISBN", dto.getIsbn());
        assertEquals(3, dto.getCopies());
        assertTrue(dto.isAvailable());
    }

    @Test
    @DisplayName("[SUCCESS] BookMapper toModel maps all fields")
    void bookMapperToModel() {
        BookDTO dto = new BookDTO("B001", "Clean Code", "R. Martin", "ISBN", true, 2);
        Book book = bookMapper.toModel(dto);

        assertEquals("B001", book.getId());
        assertEquals("Clean Code", book.getTitle());
    }

    @Test
    @DisplayName("[SUCCESS] BookMapper toDTO returns null for null book")
    void bookMapperToDTOHandlesNull() {
        assertNull(bookMapper.toDTO(null, 0));
    }

    @Test
    @DisplayName("[SUCCESS] BookMapper toModel returns null for null DTO")
    void bookMapperToModelHandlesNull() {
        assertNull(bookMapper.toModel(null));
    }

    // ─── UserMapper ───

    @Test
    @DisplayName("[SUCCESS] UserMapper toDTO maps all fields")
    void userMapperToDTO() {
        User user = new User("U001", "Alice", "alice@dosw.edu");
        user.incrementLoanCount();
        UserDTO dto = userMapper.toDTO(user);

        assertEquals("U001", dto.getId());
        assertEquals("Alice", dto.getName());
        assertEquals("alice@dosw.edu", dto.getEmail());
        assertEquals(1, dto.getLoanCount());
    }

    @Test
    @DisplayName("[SUCCESS] UserMapper toModel maps all fields")
    void userMapperToModel() {
        UserDTO dto = new UserDTO("U001", "Alice", "alice@dosw.edu", 0);
        User user = userMapper.toModel(dto);

        assertEquals("U001", user.getId());
        assertEquals("Alice", user.getName());
    }

    @Test
    @DisplayName("[SUCCESS] UserMapper handles null gracefully")
    void userMapperHandlesNull() {
        assertNull(userMapper.toDTO(null));
        assertNull(userMapper.toModel(null));
    }

    // ─── LoanMapper ───

    @Test
    @DisplayName("[SUCCESS] LoanMapper toDTO maps all fields")
    void loanMapperToDTO() {
        Loan loan = new Loan("L001", "U001", "B001");
        LoanDTO dto = loanMapper.toDTO(loan);

        assertEquals("L001", dto.getId());
        assertEquals("U001", dto.getUserId());
        assertEquals("B001", dto.getBookId());
        assertFalse(dto.isReturned());
        assertNotNull(dto.getLoanDate());
        assertNull(dto.getReturnDate());
    }

    @Test
    @DisplayName("[SUCCESS] LoanMapper toDTO handles null")
    void loanMapperHandlesNull() {
        assertNull(loanMapper.toDTO(null));
    }
}
