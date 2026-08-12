import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LeaveTypeService, LeaveType } from '../../services/leave-type.service';
import { HolidayService, Holiday } from '../../services/holiday.service';

@Component({
  selector: 'app-admin-leave-types',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-leave-types.component.html',
  styleUrl: './admin-leave-types.component.css'
})
export class AdminLeaveTypesComponent implements OnInit {
  leaveTypes: LeaveType[] = [];
  holidays: Holiday[] = [];
  
  // Leave Type Modal State
  showLeaveTypeModal: boolean = false;
  newLeaveType = {
    name: '',
    defaultAnnualQuota: 12,
    description: '',
    requiresApproval: true
  };

  // Holiday Modal State
  showHolidayModal: boolean = false;
  newHoliday = {
    date: new Date().toISOString().split('T')[0],
    name: '',
    description: ''
  };

  isLoading: boolean = true;
  isSaving: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private leaveTypeService: LeaveTypeService,
    private holidayService: HolidayService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.leaveTypeService.getAllLeaveTypes().subscribe({
      next: (types) => (this.leaveTypes = types),
      error: (err) => console.error('Error loading leave types:', err)
    });

    this.holidayService.getAllHolidays().subscribe({
      next: (hols) => {
        this.holidays = hols;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error loading holidays:', err);
      }
    });
  }

  openCreateLeaveTypeModal(): void {
    this.newLeaveType = {
      name: '',
      defaultAnnualQuota: 12,
      description: '',
      requiresApproval: true
    };
    this.showLeaveTypeModal = true;
  }

  closeLeaveTypeModal(): void {
    this.showLeaveTypeModal = false;
  }

  saveLeaveType(): void {
    if (!this.newLeaveType.name) {
      alert('Please enter leave category name');
      return;
    }

    this.isSaving = true;
    this.leaveTypeService.createLeaveType(this.newLeaveType).subscribe({
      next: () => {
        this.isSaving = false;
        this.successMessage = 'Leave type category created successfully!';
        this.closeLeaveTypeModal();
        this.loadData();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err.error?.message || 'Failed to create leave type';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }

  deleteLeaveType(id: number): void {
    if (!confirm('Are you sure you want to deactivate/soft-delete this leave category?')) {
      return;
    }

    this.leaveTypeService.deleteLeaveType(id).subscribe({
      next: () => {
        this.successMessage = 'Leave type category deactivated successfully';
        this.loadData();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to delete leave type';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }

  openCreateHolidayModal(): void {
    this.newHoliday = {
      date: new Date().toISOString().split('T')[0],
      name: '',
      description: ''
    };
    this.showHolidayModal = true;
  }

  closeHolidayModal(): void {
    this.showHolidayModal = false;
  }

  saveHoliday(): void {
    if (!this.newHoliday.name || !this.newHoliday.date) {
      alert('Please fill in Holiday Name and Date');
      return;
    }

    this.isSaving = true;
    this.holidayService.createHoliday(this.newHoliday).subscribe({
      next: () => {
        this.isSaving = false;
        this.successMessage = 'Public holiday registered successfully!';
        this.closeHolidayModal();
        this.loadData();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err.error?.message || 'Failed to register holiday';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }

  deleteHoliday(id: number): void {
    if (!confirm('Delete this public holiday from system records?')) {
      return;
    }

    this.holidayService.deleteHoliday(id).subscribe({
      next: () => {
        this.successMessage = 'Public holiday removed';
        this.loadData();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to delete holiday';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }
}
