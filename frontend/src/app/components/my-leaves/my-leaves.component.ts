import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LeaveRequestService, LeaveRequest } from '../../services/leave-request.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-my-leaves',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-leaves.component.html',
  styleUrl: './my-leaves.component.css'
})
export class MyLeavesComponent implements OnInit {
  leaveRequests: LeaveRequest[] = [];
  filteredRequests: LeaveRequest[] = [];
  selectedFilter: string = 'ALL';
  isLoading: boolean = true;
  errorMessage: string = '';
  successMessage: string = '';
  apiUrl: string = environment.apiUrl;

  constructor(private leaveRequestService: LeaveRequestService) {}

  ngOnInit(): void {
    this.loadMyLeaves();
  }

  loadMyLeaves(): void {
    this.isLoading = true;
    this.leaveRequestService.getMyLeaveRequests().subscribe({
      next: (requests) => {
        this.leaveRequests = requests;
        this.applyFilter(this.selectedFilter);
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Failed to load leave history';
      }
    });
  }

  applyFilter(filter: string): void {
    this.selectedFilter = filter;
    if (filter === 'ALL') {
      this.filteredRequests = [...this.leaveRequests];
    } else {
      this.filteredRequests = this.leaveRequests.filter((r) => r.status === filter);
    }
  }

  cancelRequest(requestId: number): void {
    if (!confirm('Are you sure you want to cancel this pending leave request?')) {
      return;
    }

    this.leaveRequestService.cancelLeaveRequest(requestId).subscribe({
      next: () => {
        this.successMessage = 'Leave request cancelled successfully';
        this.loadMyLeaves();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to cancel leave request';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }

  getAttachmentUrl(fileName: string): string {
    return `${this.apiUrl}/leaves/attachments/${fileName}`;
  }
}
