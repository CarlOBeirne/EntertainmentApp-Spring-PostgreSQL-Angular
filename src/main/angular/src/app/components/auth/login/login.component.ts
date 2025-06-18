import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  message: string= '';

  constructor(private fb: FormBuilder,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: [null, [Validators.required]],
      password: [null, [Validators.required]]
    })
  }

  onSubmit(): void {
    if (this.loginForm.invalid){
      return;
    }

    const credentials = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    };
    this.login(credentials);
  }

  login(credentials: {username: string, password: string}): void {
    this.authService.login(credentials).subscribe({
      next: () => {
        this.message = 'Login successful';
      },
      error: (err) => {
        this.message= 'Login failed!';
        console.error(err);
      }
    })
  }


}
