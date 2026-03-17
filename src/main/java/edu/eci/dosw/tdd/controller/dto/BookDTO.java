package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for Book — uses Bean Validation (@NotBlank, @Min) so Spring
 * automatically validates incoming request bodies via @Valid.
 */
public class BookDTO {

    @NotBlank(message = "Book ID must not be blank")
    private String id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotBlank(message = "ISBN must not be blank")
    private String isbn;

    private boolean available;

    @NotNull(message = "Copies must not be null")
    @Min(value = 1, message = "Copies must be at least 1")
    private Integer copies;

    public BookDTO() {}

    public BookDTO(String id, String title, String author, String isbn, boolean available, Integer copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
        this.copies = copies;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public Integer getCopies() { return copies; }
    public void setCopies(Integer copies) { this.copies = copies; }
}
