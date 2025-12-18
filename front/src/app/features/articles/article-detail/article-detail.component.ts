import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticlesApiService } from 'src/app/core/services/articles-api.service';
import { Article } from 'src/app/core/models/article.models';
import { NotificationService } from 'src/app/core/services/notification.service';
import { finalize } from 'rxjs';

interface Comment {
  author: string;
  content: string;
}

@Component({
  selector: 'app-article-detail',
  templateUrl: './article-detail.component.html',
  styleUrls: ['./article-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit {

  isSubmitting = false;
  minCommentLength = 5;

  article!: Article;
  comments: Comment[] = [];

  currentCommentIndex = 0;
  newComment = '';

  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private articlesApi: ArticlesApiService,
    private notification: NotificationService 
  ) {}

  ngOnInit(): void {
    const articleId = Number(this.route.snapshot.paramMap.get('id'));

    if (!articleId) {
      this.router.navigate(['/articles']);
      return;
    }

    this.articlesApi.getArticleById(articleId).subscribe({
      next: (article) => {
        this.article = article;

        // 🔥 IMPORTANT : on alimente les commentaires
        this.comments = article.comments.map(c => ({
          author: c.author,
          content: c.content
        }));

        this.currentCommentIndex = 0;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger l’article';
        this.loading = false;
      }
    });
  }

  // 👇 NAVIGATION COMMENTAIRES (inchangé)
  get currentComment(): Comment | null {
    return this.comments.length
      ? this.comments[this.currentCommentIndex]
      : null;
  }

  previousComment(): void {
    if (this.currentCommentIndex > 0) {
      this.currentCommentIndex--;
    }
  }

  nextComment(): void {
    if (this.currentCommentIndex < this.comments.length - 1) {
      this.currentCommentIndex++;
    }
  }


addComment(): void {
  const content = this.newComment.trim();

  if (content.length <= this.minCommentLength || this.isSubmitting) return;

  this.isSubmitting = true;

  this.articlesApi
    .addComment(this.article.articleId, content)
    .pipe(
      finalize(() => {
        this.isSubmitting = false;
        (document.activeElement as HTMLElement)?.blur(); // ✅ accessibilité
      })
    )
    .subscribe({
      next: () => {
        this.newComment = '';

        // 🔄 Rafraîchissement automatique
        this.loadComments(this.article.articleId);

        this.notification.success('Commentaire ajouté avec succès');
      },
      error: () => {
        this.notification.error('Impossible d’ajouter le commentaire');
      }
    });
}

  goBack(): void {
    this.router.navigate(['/articles']);
  }

  private loadComments(articleId: number): void {
    this.articlesApi.getArticleById(articleId).subscribe({
      next: (article) => {
        this.comments = article.comments.map(c => ({
          author: c.author,
          content: c.content
        }));
        this.currentCommentIndex = this.comments.length - 1;
      }
    });
  }
}
