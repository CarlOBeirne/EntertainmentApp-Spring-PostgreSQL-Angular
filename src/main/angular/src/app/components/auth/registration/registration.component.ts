import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";
import {Credentials} from "../../../models/credentials";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm!: FormGroup;
  errorMessage: string = '';

  constructor(private authService: AuthService,
              private fb: FormBuilder,
              private router: Router) {
  }

  onSubmit(): void {
    if (this.registrationForm.invalid) {
      return;
    }
    const credentials: Credentials = {
      username: this.registrationForm.value.username,
      password: this.registrationForm.value.password
    };
    this.register(credentials);
  }

  register(credentials: Credentials): void {
    this.authService.register(credentials).subscribe({
      next: () => {
        // Success
        this.router.navigate(['/'])
      },
      error: () => {
        this.errorMessage = 'Error registering user. Please try again'
      }
    });
  }

  ngOnInit(): void {
    this.registrationForm = this.fb.group({
      username: [null, [Validators.required]],
      password: [null, [Validators.required]]
    });
  }

}
