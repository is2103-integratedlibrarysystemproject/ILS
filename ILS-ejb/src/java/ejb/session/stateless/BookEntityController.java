
package ejb.session.stateless;

import util.exception.BookNotFoundException;
import entity.BookEntity;
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
    public BookEntity retrieveBookBytitle(String title) throws BookNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM BookEntity s WHERE s.title = :inTitle");
        query.setParameter("inTitle", title);
        
        try
        {
            return (BookEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BookNotFoundException("Book Title " + title + " does not exist!");
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
    public void updateBook(BookEntity bookEntity)
    {
        entityManager.merge(bookEntity);
    }
    
    
    
    @Override
    public void deleteBook(Long bookId) throws BookNotFoundException
    {
        BookEntity bookEntityToRemove = retrieveBookByBookId(bookId);
        entityManager.remove(bookEntityToRemove);
    }
    
    
    
    
    
    
}
