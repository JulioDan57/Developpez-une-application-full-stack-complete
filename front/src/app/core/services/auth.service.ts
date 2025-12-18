import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({ providedIn: 'root' })
export class AuthService {

  private loggedIn$ = new BehaviorSubject<boolean>(this.hasToken());

  isLoggedIn$ = this.loggedIn$.asObservable();

  // Header affiché ou non
  showHeader$ = new BehaviorSubject<boolean>(false);

    // Options du header affichées ou non
  showHeaderOptions$ = new BehaviorSubject<boolean>(false);

  login(token: string) {
    localStorage.setItem('token', token);
    this.loggedIn$.next(true);
    this.showHeaderOptions$.next(true);    
  }

  logout() {
    localStorage.removeItem('token');
    this.loggedIn$.next(false);
    this.showHeaderOptions$.next(false);    
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }
}
