import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ApplyLeaveComponent } from './components/apply-leave/apply-leave.component';
import { MyLeavesComponent } from './components/my-leaves/my-leaves.component';
import { PendingApprovalsComponent } from './components/pending-approvals/pending-approvals.component';
import { AdminUsersComponent } from './components/admin-users/admin-users.component';
import { AdminLeaveTypesComponent } from './components/admin-leave-types/admin-leave-types.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'apply-leave', component: ApplyLeaveComponent, canActivate: [authGuard] },
  { path: 'my-leaves', component: MyLeavesComponent, canActivate: [authGuard] },
  { path: 'pending-approvals', component: PendingApprovalsComponent, canActivate: [authGuard] },
  { path: 'admin/users', component: AdminUsersComponent, canActivate: [authGuard] },
  { path: 'admin/leave-types', component: AdminLeaveTypesComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' }
];
