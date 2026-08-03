package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.KnowledgeDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocument, String> {

    @Query("""
        SELECT kd
        FROM KnowledgeDocument kd
        WHERE (:search = '' OR kd.title ILIKE CONCAT('%', :search, '%')
        OR kd.category ILIKE CONCAT('%', :search, '%'))
    """)
    Page<KnowledgeDocument> findAllDocuments(Pageable pageable, @Param("search") String search);


}
