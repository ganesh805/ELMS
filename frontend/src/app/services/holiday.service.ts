import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Holiday {
  id: number;
  date: string;
  name: string;
  description?: string;
}

@Injectable({
  providedIn: 'root'
})
export class HolidayService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllHolidays(year?: number): Observable<Holiday[]> {
    let params = new HttpParams();
    if (year) {
      params = params.set('year', year.toString());
    }
    return this.http.get<Holiday[]>(`${this.apiUrl}/holidays`, { params });
  }

  getUpcomingHolidays(): Observable<Holiday[]> {
    return this.http.get<Holiday[]>(`${this.apiUrl}/holidays/upcoming`);
  }

  createHoliday(payload: { date: string; name: string; description?: string }): Observable<Holiday> {
    return this.http.post<Holiday>(`${this.apiUrl}/admin/holidays`, payload);
  }

  deleteHoliday(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/admin/holidays/${id}`);
  }
}
