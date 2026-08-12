import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService, User } from '../../services/auth.service';
import { LeaveBalanceService, LeaveBalance } from '../../services/leave-balance.service';
import { LeaveRequestService, LeaveRequest } from '../../services/leave-request.service';
import { HolidayService, Holiday } from '../../services/holiday.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  leaveBalances: LeaveBalance[] = [];
  recentRequests: LeaveRequest[] = [];
  upcomingHolidays: Holiday[] = [];
  isLoading: boolean = true;

  constructor(
    private authService: AuthService,
    private leaveBalanceService: LeaveBalanceService,
    private leaveRequestService: LeaveRequestService,
    private holidayService: HolidayService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.isLoading = true;

    this.leaveBalanceService.getMyLeaveBalances().subscribe({
      next: (data) => (this.leaveBalances = data),
      error: (err) => console.error('Error loading balances:', err)
    });

    this.leaveRequestService.getMyLeaveRequests().subscribe({
      next: (data) => {
        this.recentRequests = data.slice(0, 5); // Show latest 5
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading requests:', err);
        this.isLoading = false;
      }
    });

    this.holidayService.getUpcomingHolidays().subscribe({
      next: (data) => (this.upcomingHolidays = data.slice(0, 4)),
      error: (err) => console.error('Error loading holidays:', err)
    });
  }

  getPercentage(used: number, allocated: number): number {
    if (!allocated || allocated === 0) return 0;
    return Math.round((used / allocated) * 100);
  }
}
