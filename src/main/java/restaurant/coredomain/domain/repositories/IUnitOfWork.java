package restaurant.coredomain.domain.repositories;

public interface IUnitOfWork {
    void startTransaction();
    void rollback();
    void commit();
    void close();
}
