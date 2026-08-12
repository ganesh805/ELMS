import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface User {
  id: number;
  fullName: string;
  email: string;
  role: 'EMPLOYEE' | 'MANAGER' | 'HR_ADMIN';
  department?: string;
  managerId?: number;
  managerName?: string;
}

export interface JwtResponse {
  token: string;
  tokenType: string;
  user: User;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password?: string }): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap((response) => {
        if (response && response.token && response.user) {
          localStorage.setItem('elms_token', response.token);
          localStorage.setItem('elms_user', JSON.stringify(response.user));
          this.currentUserSubject.next(response.user);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('elms_token');
    localStorage.removeItem('elms_user');
    this.currentUserSubject.next(null);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem('elms_token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken() && !!this.getCurrentUser();
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user ? user.role === role : false;
  }

  private getUserFromStorage(): User | null {
    const stored = localStorage.getItem('elms_user');
    return stored ? JSON.parse(stored) : null;
  }
}
