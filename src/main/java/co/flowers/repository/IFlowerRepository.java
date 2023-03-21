package co.flowers.repository;

import co.flowers.domain.collection.Flower;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFlowerRepository extends ReactiveMongoRepository<Flower, String> {
}
