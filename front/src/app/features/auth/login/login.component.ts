import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiErrorResponse } from 'src/app/core/models/auth.models';
import { AuthApiService } from 'src/app/core/services/auth-api.service';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  profileForm!: FormGroup;
  apiError: string | null = null;

  constructor(
    private fb: FormBuilder, 
    private authApi: AuthApiService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authService.showHeader$.next(true);
    this.authService.showHeaderOptions$.next(false);

    this.profileForm = this.fb.group({
      usernameOrEmail: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, 
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).+$/)
      ]]
    });       
  }

  signInProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    this.apiError = null;

    this.authApi.login(this.profileForm.value).subscribe({
      next: (res) => {
        this.authService.login(res.token);
        this.router.navigate(['/articles']);
      },
    error: (err) => {
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
    }
    });
  }  

  goBack(): void {
    history.back(); // ou router.navigate(...)
  }

}
