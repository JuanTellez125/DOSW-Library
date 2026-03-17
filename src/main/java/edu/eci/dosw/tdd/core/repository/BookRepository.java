package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.Book;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * In-memory repository for books.
 * Stores each book alongside its available copy count.
 */
@Repository
public class BookRepository {

    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Integer> copies = new HashMap<>();

    public void save(Book book, int numCopies) {
        if (books.containsKey(book.getId())) {
            copies.merge(book.getId(), numCopies, Integer::sum);
        } else {
            books.put(book.getId(), book);
            copies.put(book.getId(), numCopies);
        }
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public boolean existsById(String id) {
        return books.containsKey(id);
    }

    public int getCopies(String id) {
        return copies.getOrDefault(id, 0);
    }

    public void decrementCopies(String id) {
        copies.computeIfPresent(id, (k, v) -> v - 1);
    }

    public void incrementCopies(String id) {
        copies.computeIfPresent(id, (k, v) -> v + 1);
    }

    public void clear() {
        books.clear();
        copies.clear();
    }
}
