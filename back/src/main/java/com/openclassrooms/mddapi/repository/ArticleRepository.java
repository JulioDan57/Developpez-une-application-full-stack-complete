package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findAllByOrderByCreatedAtDesc();
    List<Article> findAllByOrderByCreatedAtAsc();
    List<Article> findBySubjectOrderByCreatedAtDesc(Subject subject);
    List<Article> findByUserOrderByCreatedAtDesc(User user);

    List<Article> findBySubjectInOrderByCreatedAtDesc(List<Subject> subjects);
    List<Article> findBySubjectInOrderByCreatedAtAsc(List<Subject> subjects);



}
