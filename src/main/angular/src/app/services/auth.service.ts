import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Credentials} from "../models/credentials";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = `${environment.apiUrl}/auth`

  constructor(private http: HttpClient) { }

  login(credentials: Credentials) {
    return this.http.post(`${this.baseUrl}/login`, credentials, {
      withCredentials: true,
      responseType: 'text'
    });
  }

  register(credentials: Credentials): Observable<String> {
    return this.http.post(`${this.baseUrl}/register`, credentials, {
      withCredentials: true,
      responseType: 'text'
    });
  }
}
