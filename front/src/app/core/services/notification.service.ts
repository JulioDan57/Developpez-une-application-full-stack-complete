import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class NotificationService {

  private readonly DURATION = 3000;

  constructor(private snackBar: MatSnackBar) {}

  // ✅ Succès
  success(message: string): void {
    this.snackBar.open(
      message,
      'OK',
      {
        duration: this.DURATION,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
        panelClass: ['success-toast']
      }
    );
  }

  // ❌ Erreur
  error(message: string): void {
    this.snackBar.open(
      '❌ ' + message,
      'Fermer',
      {
        duration: this.DURATION,
        panelClass: ['error-toast']
      }
    );
  }

  // ℹ️ Information
  info(message: string): void {
    this.snackBar.open(
      message,
      'OK',
      {
        duration: this.DURATION
      }
    );
  }
}
