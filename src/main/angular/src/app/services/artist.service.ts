import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Artist} from "../models/artist";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ArtistService {

  private baseUrl: string = `${environment.apiUrl}/artist`;

  constructor(private httpClient: HttpClient) { }

  createArtist(artist: Artist): Observable<Artist> {
    return this.httpClient.post<Artist>(`${this.baseUrl}/new`, artist);
  }

  updateArtist(id: number, artist: Artist): Observable<Artist> {
    return this.httpClient.put<Artist>(`${this.baseUrl}/update/${id}`, artist);
  }

  findArtistById(id: number): Observable<Artist> {
    return this.httpClient.get<Artist>(`${this.baseUrl}/${id}`);
  }

  findAllArtists(): Observable<Artist[]> {
    return this.httpClient.get<Artist[]>(`${this.baseUrl}/all`);
  }

  deleteArtist(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
