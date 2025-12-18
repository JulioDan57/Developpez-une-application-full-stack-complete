import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LoginComponent } from './features/auth/login/login.component';
import { FeedComponent } from './features/articles/feed/feed.component';
import { SubjectComponent } from './features/subjects/subjects.component';
import { ProfileComponent } from './features/auth/profile/profile.component';
import { ArticleCreationComponent } from './features/articles/article-creation/article-creation.component';
import { ArticleDetailComponent } from './features/articles/article-detail/article-detail.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'articles', component: FeedComponent, canActivate: [AuthGuard] },

  { path: 'articles/create', component: ArticleCreationComponent, canActivate: [AuthGuard] },
  { path: 'articles/:id', component: ArticleDetailComponent, canActivate: [AuthGuard] },
  
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'themes', component: SubjectComponent, canActivate: [AuthGuard] },

  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
