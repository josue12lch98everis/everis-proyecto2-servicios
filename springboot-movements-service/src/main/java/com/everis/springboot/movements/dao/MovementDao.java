package com.everis.springboot.movements.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.everis.springboot.movements.documents.MovementDocument;

import reactor.core.publisher.Flux;

public interface MovementDao extends ReactiveMongoRepository<MovementDocument, String> {
public Flux<MovementDocument> findByIdCuenta(String idCuenta);
}
