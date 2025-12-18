import { Component, OnInit } from '@angular/core';
import { ArticlesApiService } from 'src/app/core/services/articles-api.service';
import { Article } from 'src/app/core/models/article.models';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {

  articles: Article[] = [];
  loading = true;
  error: string | null = null;

  selectedArticle: Article | null = null;
  isArticleOpen = false;

  constructor(
    private articlesApi: ArticlesApiService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Header visible + options visibles
    this.authService.showHeader$.next(true);
    this.authService.showHeaderOptions$.next(true);

    this.loadArticles();
  }

  loadArticles(): void {
    this.loading = true;

    this.articlesApi.getArticles().subscribe({
      next: (articles) => {
        this.articles = articles;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger les articles';
        this.loading = false;
      }
    });
  }

  openArticle(article: Article): void {
    this.selectedArticle = article;
    this.isArticleOpen = true;
    this.router.navigate(['/articles', article.articleId]);
  }

  closeArticle(): void {
    this.isArticleOpen = false;
    this.selectedArticle = null;
  }

  sortByDate(order: 'asc' | 'desc'): void {
    this.articles = [...this.articles].sort((a, b) => {
      const d1 = new Date(a.createdAt).getTime();
      const d2 = new Date(b.createdAt).getTime();
      return order === 'asc' ? d1 - d2 : d2 - d1;
    });
  }
  
  createArticle(): void {
    this.router.navigate(['/articles/create']);
  }
}
