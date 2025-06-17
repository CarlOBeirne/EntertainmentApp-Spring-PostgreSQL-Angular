import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Track} from '../models/track';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  private baseUrl = `${environment.apiUrl}/track`

  constructor(private http: HttpClient) {}

  // GET ALL TRACKS
  findAll(): Observable<Track[]> {
    return this.http.get<Track[]>(`${this.baseUrl}/all`);
  }

  // GET TRACK BY ID
  findById(id: number): Observable<Track> {
    return this.http.get<Track>(`${this.baseUrl}/${id}`);
  }

  // POST TRACK
  create(track: Track): Observable<Track> {
    return this.http.post<Track>(`${this.baseUrl}/new`, track);
  }

  // PUT TRACK
  update(id: number, track: Track): Observable<Track> {
    return this.http.put<Track>(`${this.baseUrl}/update/${id}`, track);
  }

  // DELETE TRACK
  delete(id: number | undefined): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }

  // Add artist
  addArtist(trackId: number, artistId: number): Observable<Track> {
    return this.http.post<Track>(`${this.baseUrl}/${trackId}/add-artist/${artistId}`, {});
  }

  // Remove artist
  removeArtist(trackId: number, artistId: number): Observable<Track> {
    return this.http.post<Track>(`${this.baseUrl}/${trackId}/remove-artist/${artistId}`, {});
  }
}
