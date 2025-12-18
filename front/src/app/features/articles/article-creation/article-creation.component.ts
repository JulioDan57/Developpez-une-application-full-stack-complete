/*
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-article-creation',
  templateUrl: './article-creation.component.html',
  styleUrls: ['./article-creation.component.scss']
})
export class ArticleCreationComponent implements OnInit {

  profileForm!: FormGroup;

  subjects = [
    { id: 1, title: 'Thème 1', content: "Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled..." , subscribed: true},
    { id: 2, title: 'Thème 2', content: "Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled..." , subscribed: true},
    { id: 3, title: 'Thème 3', content: "Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled..." , subscribed: false},
    { id: 4, title: 'Thème 4', content: "Content: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled..." , subscribed: false}
  ];  

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      articleSubject: [null, Validators.required],
      articleTitle: ['', [Validators.required, Validators.minLength(10)]],
      articleContent: ['', [Validators.required, Validators.minLength(20) ]]
    });      
  }

  registerArticle(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const articleData = {
      subjectId: this.profileForm.value.articleSubject.id,
      subjectTitle: this.profileForm.value.articleSubject.title,
      title: this.profileForm.value.articleTitle,
      content: this.profileForm.value.articleContent
    };

    console.log('Données à envoyer à l’API :', articleData);

    // 👉 future API
    // this.userService.updateProfile(userData)
  }

}
*/

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArticlesApiService } from 'src/app/core/services/articles-api.service';
import { SubjectsApiService } from 'src/app/core/services/subjects-api.service';
import { Router } from '@angular/router';
import { Subject } from 'src/app/core/models/article.models';

@Component({
  selector: 'app-article-creation',
  templateUrl: './article-creation.component.html',
  styleUrls: ['./article-creation.component.scss']
})
export class ArticleCreationComponent implements OnInit {

  profileForm!: FormGroup;

  // 🔥 Remplace le mock par l’API
  subjects: Subject[] = [];

  loading = true;
  isSubmitting = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private articlesApi: ArticlesApiService,
    private subjectsApi: SubjectsApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      articleSubject: [null, Validators.required],
      articleTitle: ['', [Validators.required, Validators.minLength(10)]],
      articleContent: ['', [Validators.required, Validators.minLength(20)]]
    });

    this.loadSubjects();
  }

  // 🔥 GET /api/subjects
  loadSubjects(): void {
    this.subjectsApi.getSubjects(true).subscribe({
      next: (subjects) => {
        this.subjects = subjects;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger les thèmes';
        this.loading = false;
      }
    });
  }

  // 🔥 POST /api/articles
  registerArticle(): void {
    if (this.profileForm.invalid || this.isSubmitting) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const formValue = this.profileForm.value;

    const articleData = {
      title: formValue.articleTitle,
      content: formValue.articleContent,
      subjectId: formValue.articleSubject.subjectId
    };

    this.isSubmitting = true;

    this.articlesApi.createArticle(articleData).subscribe({
      next: (article) => {
        // 👉 redirection vers l’article créé
        this.router.navigate(['/articles', article.articleId]);
      },
      error: () => {
        this.error = 'Erreur lors de la création de l’article';
        this.isSubmitting = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/articles']);
  }
}
