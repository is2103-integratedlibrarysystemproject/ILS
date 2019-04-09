
package ejb.session.stateless;

import util.exception.BookNotFoundException;
import entity.BookEntity;
import java.util.List;


public interface BookEntityControllerLocal {
    public BookEntity createNewBook(BookEntity newBookEntity);
    public List<BookEntity> retrieveAllBooks();
    public BookEntity retrieveBookByBookId(Long bookId) throws BookNotFoundException;
    public BookEntity retrieveBookByIsbn(String isbn) throws BookNotFoundException;
    public List<BookEntity> retrieveBookByTitle(String title) throws BookNotFoundException;
    public void updateBook(BookEntity bookEntity);
    public void deleteBook(Long bookId) throws BookNotFoundException;

}
