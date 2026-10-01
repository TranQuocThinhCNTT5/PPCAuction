package org.example.ppcauction.repository;

import org.example.ppcauction.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByPhraseContainingIgnoreCaseAndStatusContainingIgnoreCaseOrderByPhrase(String phrase, String status);
    boolean existsByPhraseIgnoreCase(String phrase);
}
