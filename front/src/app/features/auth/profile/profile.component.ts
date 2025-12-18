import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { AuthApiService } from 'src/app/core/services/auth-api.service';
import { SubjectsApiService } from 'src/app/core/services/subjects-api.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { ApiErrorResponse, UserSubscription } from 'src/app/core/models/auth.models';
import { NotificationService } from 'src/app/core/services/notification.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  profileForm!: FormGroup;
  subjects: UserSubscription[] = [];
  apiError: string | null = null;  

  loading = true;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private dialog: MatDialog,
    private authApi: AuthApiService,
    private subjectsApi: SubjectsApiService,
    private authService: AuthService,
    private notification: NotificationService 
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadProfile();
  }

  private initForm(): void {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).+$/)
      ]]
    });
  }

  // 🔥 GET /api/auth/me
  loadProfile(): void {
    this.loading = true;

    this.authApi.getMe().subscribe({
      next: (profile) => {
        this.profileForm.patchValue({
          username: profile.username,
          email: profile.email,
          password: '' // jamais préremplir
        });

        this.subjects = profile.subscriptions;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger le profil';
        this.loading = false;
      }
    });
  }

  // 🔥 PUT /api/auth/me
  saveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }
    
    this.apiError = null;

    this.authApi.updateMe(this.profileForm.value).subscribe({
      next: (res) => {
        // 🔐 Token renouvelé
        this.authService.login(res.token);
        this.notification.success('Profil mis à jour avec succès');
      },
      error: (err) =>{
        // err.error contient normalement ton ApiError côté backend
        if (err.error && typeof err.error === 'object') {
          const error = err.error as ApiErrorResponse;
  
          // Si le backend fournit des erreurs de champs
          if (error.errors && Object.keys(error.errors).length > 0) {
            // Exemple pour afficher le message du champ password
            this.apiError = error.errors['password'] ?? error.message;
          } else {
            this.apiError = error.message || 'Erreur de connexion';
          }
        } else {
          // Cas générique
          this.apiError = 'Erreur de connexion';
        }
        this.notification.error(this.apiError);
      }

    });
  }

  // 🔥 POST /api/subjects/{id}/unsubscribe
  unsubscribe(subject: UserSubscription): void {
    this.dialog
      .open(ConfirmDialogComponent, {
        width: '320px',
        data: {
          title: 'Se désabonner',
          message: `Voulez-vous vous désabonner de "${subject.subjectName}" ?`
        }
      })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.subjectsApi.unsubscribe(subject.subjectId).subscribe({
            next: () => {
              // 🔄 Suppression immédiate
              this.subjects = this.subjects.filter(
                s => s.subjectId !== subject.subjectId
              );
            }
          });
        }
      });
  }
}
