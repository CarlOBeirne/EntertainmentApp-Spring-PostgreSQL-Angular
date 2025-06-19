import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Genre} from "../models/genre";

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private baseUrl = `${environment.apiUrl}/genre`

  constructor(private http: HttpClient) { }

  findAll(): Observable<Genre[]> {
    return this.http.get<Genre[]>(`${this.baseUrl}/all`);
  }

  findById(id: number): Observable<Genre> {
    return this.http.get<Genre>(`${this.baseUrl}/${id}`)
  }

  create(genre: Genre): Observable<Genre> {
    return this.http.post<Genre>(`${this.baseUrl}/new`, genre);
  }

  update(id: number, genre: Genre): Observable<Genre> {
    return this.http.put<Genre>(`${this.baseUrl}/update/${id}`, genre);
  }

  delete(id: number | undefined): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }

}
