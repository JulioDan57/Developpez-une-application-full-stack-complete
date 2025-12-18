import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  RegisterRequest,
  LoginRequest,
  AuthSuccessResponse,
  UserProfileResponse
} from '../models/auth.models';

@Injectable({ providedIn: 'root' })
export class AuthApiService {

  private readonly API_URL = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(data: RegisterRequest): Observable<AuthSuccessResponse> {
    return this.http.post<AuthSuccessResponse>(
      `${this.API_URL}/register`,
      data
    );
  }

  login(data: LoginRequest): Observable<AuthSuccessResponse> {
    return this.http.post<AuthSuccessResponse>(
      `${this.API_URL}/login`,
      data
    );
  }

  // ✅ GET PROFIL
  getMe(): Observable<UserProfileResponse> {
    return this.http.get<UserProfileResponse>(`${this.API_URL}/me`);
  }

  // ✅ UPDATE PROFIL
  updateMe(data: {
    email: string;
    username: string;
    password: string;
  }): Observable<AuthSuccessResponse> {
    return this.http.put<AuthSuccessResponse>(
      `${this.API_URL}/me`,
      data
    );
  }

}
