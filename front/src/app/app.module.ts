import { NgModule } from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LoginComponent } from './features/auth/login/login.component';
import { FeedComponent } from './features/articles/feed/feed.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from './core/header/header.component';
import { SubjectComponent } from './features/subjects/subjects.component';
import { ProfileComponent } from './features/auth/profile/profile.component';
import { ConfirmDialogComponent } from './shared/components/confirm-dialog/confirm-dialog.component';
import { ArticleCreationComponent } from './features/articles/article-creation/article-creation.component';
import { ArticleDetailComponent } from './features/articles/article-detail/article-detail.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { JwtInterceptor } from './core/interceptors/jwt.interceptor';
import { MatSnackBarModule } from '@angular/material/snack-bar';



@NgModule({
  declarations: [ 
                  AppComponent, 
                  HomeComponent, 
                  RegisterComponent, 
                  LoginComponent, 
                  FeedComponent, 
                  HeaderComponent, 
                  SubjectComponent, 
                  ProfileComponent, 
                  ConfirmDialogComponent, 
                  ArticleCreationComponent, 
                  ArticleDetailComponent
                ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule, 
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSidenavModule, 
    ReactiveFormsModule,
    MatMenuModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatSelectModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    MatSnackBarModule 

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }    
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
