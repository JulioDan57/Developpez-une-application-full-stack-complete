import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

    @Input() showOptions = false;
    @Output() toggleMenu = new EventEmitter<void>();

  constructor(private authService: AuthService, private router: Router, private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  logout() {
    this.dialog
      .open(ConfirmDialogComponent, {
        width: '320px',
        data: {
          title: 'Déconnexion',
          message: `Voulez-vous vous déconnecter ?`
        }
      })
      .afterClosed()
      .subscribe((confirmed: boolean) => {
        if (confirmed === true) {
          this.authService.logout();       // supprime token + met à jour observables
          this.router.navigate(['/']);     // redirige vers la Home
        }
      });
  }

}
