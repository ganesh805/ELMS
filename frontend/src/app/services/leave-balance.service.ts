import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LeaveBalance {
  id: number;
  userId: number;
  userName: string;
  leaveTypeId: number;
  leaveTypeName: string;
  year: number;
  allocated: number;
  used: number;
  remaining: number;
}

@Injectable({
  providedIn: 'root'
})
export class LeaveBalanceService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMyLeaveBalances(): Observable<LeaveBalance[]> {
    return this.http.get<LeaveBalance[]>(`${this.apiUrl}/leave-balances/my`);
  }
}
