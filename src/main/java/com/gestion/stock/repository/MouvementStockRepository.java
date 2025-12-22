package com.gestion.stock.repository;


import com.gestion.stock.entity.MouvementStock;
import com.gestion.stock.enums.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock,Long> {

    public List<MouvementStock> findMouvementStockByDateMouvementBetweenAndTypeMouvement(LocalDateTime dateDebut, LocalDateTime dateFin, TypeMouvement type);

}