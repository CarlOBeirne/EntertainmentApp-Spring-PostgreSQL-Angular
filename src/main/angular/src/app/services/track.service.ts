import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Track } from '../models/track';

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  private baseUrl = `${environment.apiUrl}/tracks`

  constructor(private http: HttpClient) {}

  // GET /api/tracks
  findAll(): Observable<Track[]> {
    return this.http.get<Track[]>(this.baseUrl);
  }
}
