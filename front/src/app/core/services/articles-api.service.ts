import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../models/article.models';

@Injectable({ providedIn: 'root' })
export class ArticlesApiService {

  private readonly API_URL = 'http://localhost:8080/api/articles';

  constructor(private http: HttpClient) {}

  getArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(this.API_URL);
  }

  getArticleById(id: number) {
    return this.http.get<Article>(`${this.API_URL}/${id}`);
  }

  addComment(articleId: number, content: string) {
    return this.http.post<{
      commentId: number;
      content: string;
      createdAt: string;
      author: string;
    }>(
      `${this.API_URL}/${articleId}/comments`,
      { content }
    );
  }

  // CREATE ARTICLE
  createArticle(data: {
    title: string;
    content: string;
    subjectId: number;
  }) {
    return this.http.post<Article>(
      `${this.API_URL}`,
      data
    );
  }  

}
