import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl = `${environment.apiUrl}`

  constructor(private http: HttpClient) { }

  login(credentials: {username: string, password: string}) {
    console.log(credentials)
    return this.http.post(`${this.baseUrl}/auth/login`, credentials, {
      withCredentials: true,
      responseType: 'text'
    });
  }
}
