import { Component, OnInit } from '@angular/core';
import { SubjectsApiService } from 'src/app/core/services/subjects-api.service';
import { Subject } from 'src/app/core/models/article.models';

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.scss']
})
export class SubjectComponent implements OnInit {

  subjects: Subject[] = [];
  loading = true;
  error: string | null = null;

  constructor(private subjectsApi: SubjectsApiService) {}

  ngOnInit(): void {
    this.loadSubjects();
  }

  loadSubjects(): void {
    this.loading = true;

    this.subjectsApi.getSubjects().subscribe({
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

  subscribe(subject: Subject): void {
    if (subject.subscribed) return;

    this.subjectsApi.subscribe(subject.subjectId).subscribe({
      next: () => {
        // 🔥 Mise à jour immédiate de l’UI
        subject.subscribed = true;
      },
      error: () => {
        this.error = 'Erreur lors de l’abonnement';
      }
    });
  }
}
