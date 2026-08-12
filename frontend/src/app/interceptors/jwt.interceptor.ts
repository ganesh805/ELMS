import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export class JwtInterceptorHolder {}

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  const currentUser = authService.getCurrentUser();

  let headers = req.headers;

  if (token) {
    headers = headers.set('Authorization', `Bearer ${token}`);
  }
  if (currentUser) {
    headers = headers.set('X-User-Id', currentUser.id.toString());
  }

  const authReq = req.clone({ headers });
  return next(authReq);
};
