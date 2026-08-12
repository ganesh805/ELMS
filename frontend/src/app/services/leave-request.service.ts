import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LeaveRequest {
  id: number;
  userId: number;
  userName: string;
  userEmail: string;
  department: string;
  leaveTypeId: number;
  leaveTypeName: string;
  startDate: string;
  endDate: string;
  numberOfDays: number;
  reason: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';
  appliedOn: string;
  approverId?: number;
  approverName?: string;
  decisionComment?: string;
  decisionDate?: string;
  attachmentFileName?: string;
}

export interface LeaveCreatePayload {
  leaveTypeId: number;
  startDate: string;
  endDate: string;
  reason: string;
  attachmentFileName?: string;
}

@Injectable({
  providedIn: 'root'
})
export class LeaveRequestService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  createLeaveRequest(payload: LeaveCreatePayload): Observable<LeaveRequest> {
    return this.http.post<LeaveRequest>(`${this.apiUrl}/leaves`, payload);
  }

  getMyLeaveRequests(status?: string): Observable<LeaveRequest[]> {
    let params = new HttpParams();
    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<LeaveRequest[]>(`${this.apiUrl}/leaves/my`, { params });
  }

  getPendingApprovalsForManager(): Observable<LeaveRequest[]> {
    return this.http.get<LeaveRequest[]>(`${this.apiUrl}/leaves/pending`);
  }

  approveLeaveRequest(requestId: number, decisionComment?: string): Observable<LeaveRequest> {
    return this.http.put<LeaveRequest>(`${this.apiUrl}/leaves/${requestId}/approve`, { decisionComment });
  }

  rejectLeaveRequest(requestId: number, decisionComment?: string): Observable<LeaveRequest> {
    return this.http.put<LeaveRequest>(`${this.apiUrl}/leaves/${requestId}/reject`, { decisionComment });
  }

  cancelLeaveRequest(requestId: number): Observable<LeaveRequest> {
    return this.http.put<LeaveRequest>(`${this.apiUrl}/leaves/${requestId}/cancel`, {});
  }

  uploadAttachment(file: File): Observable<{ fileName: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ fileName: string }>(`${this.apiUrl}/leaves/upload-attachment`, formData);
  }
}
