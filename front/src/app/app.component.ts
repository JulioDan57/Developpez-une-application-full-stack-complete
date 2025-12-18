import { Component, ViewChild } from '@angular/core';
import { AuthService } from './core/services/auth.service';
import { NavigationEnd, Router, Event } from '@angular/router';
import { filter } from 'rxjs';
import { MatSidenav } from '@angular/material/sidenav';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from './shared/components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  title = 'front';

  constructor(
    public authService: AuthService,
    private router: Router,
    private dialog: MatDialog
  ) {
    // Surveille les changements de route
    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe(event => {
        const url = event.urlAfterRedirects;

        // HOME
        if (url === '/') {
          this.authService.showHeader$.next(false);
          this.authService.showHeaderOptions$.next(false);
          return;
        }

        // LOGIN / REGISTER
        if (url.startsWith('/login') || url.startsWith('/register')) {
          this.authService.showHeader$.next(true);
          this.authService.showHeaderOptions$.next(false);
          return;
        }

        // PAGES CONNECTÉES
        this.authService.showHeader$.next(true);
        this.authService.showHeaderOptions$.next(true);
      });
  } 

  toggleSidenav() {
    this.sidenav.toggle();
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
          this.sidenav?.close();           // ferme le menu mobile si ouvert
        }
      });
  }  
}
