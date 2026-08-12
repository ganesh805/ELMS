import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { LeaveBalanceService } from '../../services/leave-balance.service';

export interface AdminUser {
  id: number;
  fullName: string;
  email: string;
  role: 'EMPLOYEE' | 'MANAGER' | 'HR_ADMIN';
  department?: string;
  dateOfJoining?: string;
  managerId?: number;
  managerName?: string;
}

@Component({
  selector: 'app-admin-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-users.component.html',
  styleUrl: './admin-users.component.css'
})
export class AdminUsersComponent implements OnInit {
  users: AdminUser[] = [];
  managers: AdminUser[] = [];
  
  // User Modal State
  showUserModal: boolean = false;
  editingUser: AdminUser | null = null;
  newUser: {
    fullName: string;
    email: string;
    password?: string;
    role: 'EMPLOYEE' | 'MANAGER' | 'HR_ADMIN';
    department: string;
    managerId: number | null;
  } = {
    fullName: '',
    email: '',
    password: '',
    role: 'EMPLOYEE',
    department: 'Engineering',
    managerId: null
  };

  // Balance Adjust Modal State
  showBalanceModal: boolean = false;
  adjustTargetUser: AdminUser | null = null;
  adjustData = {
    leaveTypeId: 1,
    year: 2026,
    newAllocatedQuota: 18,
    reason: 'HR Annual Quota Adjustment'
  };

  isLoading: boolean = true;
  isSaving: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  private apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private leaveBalanceService: LeaveBalanceService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.http.get<AdminUser[]>(`${this.apiUrl}/admin/users`).subscribe({
      next: (data) => {
        this.users = data;
        this.managers = data.filter((u) => u.role === 'MANAGER' || u.role === 'HR_ADMIN');
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Failed to load user accounts';
      }
    });
  }

  openCreateUserModal(): void {
    this.editingUser = null;
    this.newUser = {
      fullName: '',
      email: '',
      password: '',
      role: 'EMPLOYEE',
      department: 'Engineering',
      managerId: this.managers.length > 0 ? this.managers[0].id : null
    };
    this.showUserModal = true;
  }

  openEditUserModal(user: AdminUser): void {
    this.editingUser = user;
    this.newUser = {
      fullName: user.fullName,
      email: user.email,
      password: '',
      role: user.role,
      department: user.department || 'Engineering',
      managerId: user.managerId || null
    };
    this.showUserModal = true;
  }

  closeUserModal(): void {
    this.showUserModal = false;
    this.editingUser = null;
  }

  saveUser(): void {
    if (!this.newUser.fullName || !this.newUser.email) {
      alert('Please fill in Full Name and Email');
      return;
    }

    this.isSaving = true;

    if (this.editingUser) {
      // Update User
      this.http.put<AdminUser>(`${this.apiUrl}/admin/users/${this.editingUser.id}`, this.newUser).subscribe({
        next: () => {
          this.isSaving = false;
          this.successMessage = 'User updated successfully!';
          this.closeUserModal();
          this.loadUsers();
          setTimeout(() => (this.successMessage = ''), 3000);
        },
        error: (err) => {
          this.isSaving = false;
          this.errorMessage = err.error?.message || 'Failed to update user';
          setTimeout(() => (this.errorMessage = ''), 3000);
        }
      });
    } else {
      // Create User
      this.http.post<AdminUser>(`${this.apiUrl}/admin/users`, this.newUser).subscribe({
        next: () => {
          this.isSaving = false;
          this.successMessage = 'New user created successfully!';
          this.closeUserModal();
          this.loadUsers();
          setTimeout(() => (this.successMessage = ''), 3000);
        },
        error: (err) => {
          this.isSaving = false;
          this.errorMessage = err.error?.message || 'Failed to create user';
          setTimeout(() => (this.errorMessage = ''), 3000);
        }
      });
    }
  }

  openAdjustBalanceModal(user: AdminUser): void {
    this.adjustTargetUser = user;
    this.adjustData = {
      leaveTypeId: 1,
      year: 2026,
      newAllocatedQuota: 20,
      reason: 'Annual Quota Update'
    };
    this.showBalanceModal = true;
  }

  closeBalanceModal(): void {
    this.showBalanceModal = false;
    this.adjustTargetUser = null;
  }

  saveBalanceAdjustment(): void {
    if (!this.adjustTargetUser) return;

    this.isSaving = true;
    const payload = {
      userId: this.adjustTargetUser.id,
      leaveTypeId: this.adjustData.leaveTypeId,
      year: this.adjustData.year,
      newAllocatedQuota: this.adjustData.newAllocatedQuota,
      reason: this.adjustData.reason
    };

    this.http.post(`${this.apiUrl}/admin/leave-balances/adjust`, payload).subscribe({
      next: () => {
        this.isSaving = false;
        this.successMessage = `Leave balance adjusted for ${this.adjustTargetUser!.fullName}`;
        this.closeBalanceModal();
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.isSaving = false;
        this.errorMessage = err.error?.message || 'Failed to adjust balance';
        setTimeout(() => (this.errorMessage = ''), 3000);
      }
    });
  }
}
