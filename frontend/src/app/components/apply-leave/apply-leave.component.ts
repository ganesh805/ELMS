import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LeaveTypeService, LeaveType } from '../../services/leave-type.service';
import { LeaveRequestService } from '../../services/leave-request.service';
import { HolidayService, Holiday } from '../../services/holiday.service';

@Component({
  selector: 'app-apply-leave',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './apply-leave.component.html',
  styleUrl: './apply-leave.component.css'
})
export class ApplyLeaveComponent implements OnInit {
  leaveTypes: LeaveType[] = [];
  holidays: Holiday[] = [];
  
  selectedLeaveTypeId: number | null = null;
  startDate: string = new Date().toISOString().split('T')[0];
  endDate: string = new Date().toISOString().split('T')[0];
  reason: string = '';
  selectedFile: File | null = null;
  
  calculatedWorkingDays: number = 0;
  isSubmitting: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private leaveTypeService: LeaveTypeService,
    private leaveRequestService: LeaveRequestService,
    private holidayService: HolidayService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadLeaveTypes();
    this.loadHolidays();
  }

  loadLeaveTypes(): void {
    this.leaveTypeService.getActiveLeaveTypes().subscribe({
      next: (types) => {
        this.leaveTypes = types;
        if (types.length > 0) {
          this.selectedLeaveTypeId = types[0].id;
        }
        this.recalculateWorkingDays();
      },
      error: (err) => console.error('Error loading leave types:', err)
    });
  }

  loadHolidays(): void {
    this.holidayService.getAllHolidays().subscribe({
      next: (data) => {
        this.holidays = data;
        this.recalculateWorkingDays();
      },
      error: (err) => console.error('Error loading holidays:', err)
    });
  }

  onDateChange(): void {
    this.recalculateWorkingDays();
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  recalculateWorkingDays(): void {
    if (!this.startDate || !this.endDate) {
      this.calculatedWorkingDays = 0;
      return;
    }

    const start = new Date(this.startDate);
    const end = new Date(this.endDate);

    if (end < start) {
      this.calculatedWorkingDays = 0;
      return;
    }

    let workingDays = 0;
    const current = new Date(start);

    const holidayDates = new Set(this.holidays.map((h) => h.date));

    while (current <= end) {
      const dayOfWeek = current.getDay();
      const dateString = current.toISOString().split('T')[0];

      // Skip Saturdays (6), Sundays (0), and holidays
      if (dayOfWeek !== 0 && dayOfWeek !== 6 && !holidayDates.has(dateString)) {
        workingDays++;
      }
      current.setDate(current.getDate() + 1);
    }

    this.calculatedWorkingDays = workingDays;
  }

  onSubmit(): void {
    if (!this.selectedLeaveTypeId) {
      this.errorMessage = 'Please select a leave type';
      return;
    }
    if (!this.startDate || !this.endDate) {
      this.errorMessage = 'Please select valid start and end dates';
      return;
    }
    if (this.calculatedWorkingDays === 0) {
      this.errorMessage = 'Selected date range contains 0 working days';
      return;
    }
    if (!this.reason.trim()) {
      this.errorMessage = 'Please provide a reason for your leave request';
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.selectedFile) {
      this.leaveRequestService.uploadAttachment(this.selectedFile).subscribe({
        next: (res) => {
          this.submitLeaveRequest(res.fileName);
        },
        error: (err) => {
          this.isSubmitting = false;
          this.errorMessage = err.error?.message || 'Failed to upload attachment';
        }
      });
    } else {
      this.submitLeaveRequest();
    }
  }

  private submitLeaveRequest(attachmentFileName?: string): void {
    const payload = {
      leaveTypeId: this.selectedLeaveTypeId!,
      startDate: this.startDate,
      endDate: this.endDate,
      reason: this.reason,
      attachmentFileName
    };

    this.leaveRequestService.createLeaveRequest(payload).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = 'Leave application submitted successfully!';
        setTimeout(() => {
          this.router.navigate(['/my-leaves']);
        }, 1500);
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err.error?.message || 'Failed to submit leave request';
      }
    });
  }
}
