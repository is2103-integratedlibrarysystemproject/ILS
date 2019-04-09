
package ejb.session.stateless;

import util.exception.BookNotFoundException;
import entity.BookEntity;
import entity.LendingEntity;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
@Local(BookEntityControllerLocal.class)
@Remote(BookEntityControllerRemote.class)
public class BookEntityController implements BookEntityControllerRemote, BookEntityControllerLocal {

    @PersistenceContext(unitName = "ILS-ejbPU")
    private javax.persistence.EntityManager entityManager;

    public BookEntityController() {
    }
  
    @Override
    public BookEntity createNewBook(BookEntity newBookEntity)
    {
        entityManager.persist(newBookEntity);
        entityManager.flush();
        
        return newBookEntity;
    }
    
    
    
    @Override
    public List<BookEntity> retrieveAllBooks()
    {
        Query query = entityManager.createQuery("SELECT s FROM BookEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public BookEntity retrieveBookByBookId(Long bookId) throws BookNotFoundException
    {
        BookEntity bookEntity = entityManager.find(BookEntity.class, bookId);
        
        if(bookEntity != null)
        {
            return bookEntity;
        }
        else
        {
            throw new BookNotFoundException("Book ID " + bookId + " does not exist!");
        }
    }
    
    @Override
    public BookEntity retrieveBookByIsbn(String isbn) throws BookNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM BookEntity s WHERE s.isbn = :inIsbn");
        query.setParameter("inIsbn", isbn);
        
        try
        {
            return (BookEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BookNotFoundException("Book Title " + isbn + " does not exist!");
        }
    }
    
    @Override
    public List<BookEntity> retrieveBookByTitle(String title) throws BookNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM BookEntity s WHERE s.title LIKE :inTitle");
        query.setParameter("inTitle", "%"+title+"%");
        
        try
        {
            return query.getResultList();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BookNotFoundException("Book Title " + title + " does not exist!");
        }
    }
    
    @Override
    public void updateBook(BookEntity bookEntity)
    {
        BookEntity book = entityManager.find(BookEntity.class, bookEntity.getBookId());
        book.setTitle(bookEntity.getTitle());
        book.setIsbn(bookEntity.getIsbn());
        book.setYear(bookEntity.getYear());
        book.setAvailable(bookEntity.getAvailable());
    }
    
    
    
    @Override
    public void deleteBook(Long bookId) throws BookNotFoundException
    {
        BookEntity bookEntityToRemove = entityManager.find(BookEntity.class, bookId);
        
        if (!bookEntityToRemove.getLendings().isEmpty() && !bookEntityToRemove.getReservations().isEmpty()) {
            return;
        }
        
        entityManager.refresh(bookEntityToRemove);
        entityManager.remove(bookEntityToRemove);
    }
}
