import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LeaveRequestService, LeaveRequest } from '../../services/leave-request.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-pending-approvals',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pending-approvals.component.html',
  styleUrl: './pending-approvals.component.css'
})
export class PendingApprovalsComponent implements OnInit {
  pendingRequests: LeaveRequest[] = [];
  selectedRequest: LeaveRequest | null = null;
  decisionComment: string = '';
  
  isLoading: boolean = true;
  isProcessing: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  apiUrl: string = environment.apiUrl;

  constructor(private leaveRequestService: LeaveRequestService) {}

  ngOnInit(): void {
    this.loadPendingApprovals();
  }

  loadPendingApprovals(): void {
    this.isLoading = true;
    this.leaveRequestService.getPendingApprovalsForManager().subscribe({
      next: (requests) => {
        this.pendingRequests = requests;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Failed to load pending approvals';
      }
    });
  }

  openDecisionModal(request: LeaveRequest): void {
    this.selectedRequest = request;
    this.decisionComment = '';
  }

  closeDecisionModal(): void {
    this.selectedRequest = null;
    this.decisionComment = '';
  }

  approveRequest(): void {
    if (!this.selectedRequest) return;

    this.isProcessing = true;
    this.leaveRequestService.approveLeaveRequest(this.selectedRequest.id, this.decisionComment).subscribe({
      next: () => {
        this.isProcessing = false;
        this.successMessage = `Leave request #${this.selectedRequest!.id} approved successfully!`;
        this.closeDecisionModal();
        this.loadPendingApprovals();
        setTimeout(() => (this.successMessage = ''), 3500);
      },
      error: (err) => {
        this.isProcessing = false;
        this.errorMessage = err.error?.message || 'Failed to approve leave request';
        setTimeout(() => (this.errorMessage = ''), 3500);
      }
    });
  }

  rejectRequest(): void {
    if (!this.selectedRequest) return;
    if (!this.decisionComment.trim()) {
      alert('Please provide a reason/comment for rejecting the leave application.');
      return;
    }

    this.isProcessing = true;
    this.leaveRequestService.rejectLeaveRequest(this.selectedRequest.id, this.decisionComment).subscribe({
      next: () => {
        this.isProcessing = false;
        this.successMessage = `Leave request #${this.selectedRequest!.id} rejected.`;
        this.closeDecisionModal();
        this.loadPendingApprovals();
        setTimeout(() => (this.successMessage = ''), 3500);
      },
      error: (err) => {
        this.isProcessing = false;
        this.errorMessage = err.error?.message || 'Failed to reject leave request';
        setTimeout(() => (this.errorMessage = ''), 3500);
      }
    });
  }

  getAttachmentUrl(fileName: string): string {
    return `${this.apiUrl}/leaves/attachments/${fileName}`;
  }
}
