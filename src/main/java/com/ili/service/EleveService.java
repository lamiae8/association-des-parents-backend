package com.ili.service;

import com.ili.model.EleveEntity;
import com.ili.repository.EleveRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
public class EleveService {

    private final EleveRepository eleveRepository;

    public Optional<EleveEntity> getById(long id){
        return eleveRepository.findByIdOptional(id);
    }
    public List<EleveEntity> getAll(){
        return eleveRepository.listAll();
    }

    @Transactional
    public EleveEntity create(EleveEntity eleve){
        eleveRepository.persist(eleve);
        return eleve;
    }
}
