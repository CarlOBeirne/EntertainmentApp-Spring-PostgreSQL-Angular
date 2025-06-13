import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Track } from '../models/track';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  private baseUrl = `${environment.apiUrl}/track`

  constructor(private http: HttpClient) {}

  // GET /api/tracks
  findAll(): Observable<Track[]> {
    return this.http.get<Track[]>(`${this.baseUrl}/all`);
  }
}
