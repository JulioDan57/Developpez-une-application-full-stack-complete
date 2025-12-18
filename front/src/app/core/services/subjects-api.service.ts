import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from '../models/article.models';

@Injectable({ providedIn: 'root' })
export class SubjectsApiService {

  private readonly API_URL = 'http://localhost:8080/api/subjects';

  constructor(private http: HttpClient) {}

  // SUBJECTS
  getSubjects(subscribed?: boolean) {
    let url = this.API_URL;
    if (subscribed === true) url += '?subscribed=true';
    if (subscribed === false) url += '?subscribed=false';

    return this.http.get<Subject[]>(url);
  }
  
  subscribe(subjectId: number) {
    return this.http.post<void>(`${this.API_URL}/${subjectId}/subscribe`, {});
  }  

  unsubscribe(subjectId: number) {
    return this.http.post<void>(`${this.API_URL}/${subjectId}/unsubscribe`, {});
  }    
}
