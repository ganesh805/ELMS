import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LeaveType {
  id: number;
  name: string;
  defaultAnnualQuota: number;
  description?: string;
  active: boolean;
  requiresApproval: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class LeaveTypeService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getActiveLeaveTypes(): Observable<LeaveType[]> {
    return this.http.get<LeaveType[]>(`${this.apiUrl}/leave-types`);
  }

  getAllLeaveTypes(): Observable<LeaveType[]> {
    return this.http.get<LeaveType[]>(`${this.apiUrl}/admin/leave-types`);
  }

  createLeaveType(payload: Partial<LeaveType>): Observable<LeaveType> {
    return this.http.post<LeaveType>(`${this.apiUrl}/admin/leave-types`, payload);
  }

  updateLeaveType(id: number, payload: Partial<LeaveType>): Observable<LeaveType> {
    return this.http.put<LeaveType>(`${this.apiUrl}/admin/leave-types/${id}`, payload);
  }

  deleteLeaveType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/leave-types/${id}`);
  }
}
