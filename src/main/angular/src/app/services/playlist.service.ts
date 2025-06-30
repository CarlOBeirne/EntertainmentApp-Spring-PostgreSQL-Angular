import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Playlist} from '../models/playlist';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {

  private baseUrl = `${environment.apiUrl}/playlist`;

  constructor(private http: HttpClient) {}

  //GetAll
  findAllPlaylist(): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(`${this.baseUrl}/all`);
  }

  //FindbyId 
  findPlaylistById(id: number): Observable<Playlist> {
    return this.http.get<Playlist>(`${this.baseUrl}/${id}`);
  }

  //Create
  createPlaylist(playlist: Playlist): Observable<Playlist> {
    return this.http.post<Playlist>(`${this.baseUrl}/new`, playlist);
  }

  //Update
  updatePlaylist(id: number, playlist: Playlist): Observable<Playlist> {
    return this.http.put<Playlist>(`${this.baseUrl}/update/${id}`, playlist);
  }

  //Delete
  deletePlaylist(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
