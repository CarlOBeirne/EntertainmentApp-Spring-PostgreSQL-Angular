import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TrackService} from "../../../services/track.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Track} from "../../../models/track";

@Component({
  selector: 'app-track-form',
  templateUrl: './track-form.component.html',
  styleUrls: ['./track-form.component.css']
})
export class TrackFormComponent implements OnInit {
  trackForm!: FormGroup;
  isEditMode = false;
  trackId?: number;
  errorMessage = '';
  years: number[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private trackService: TrackService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.trackForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      durationSeconds: ['', [Validators.required, Validators.min(1)]],
      yearReleased: ['', [Validators.required]],
      beatsPerMinute: ['', [Validators.required, Validators.min(1)]]
    });

    // Check if route has an id
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if(idParam) {
        this.isEditMode = true;
        this.trackId = +idParam;
        this.loadTrack(this.trackId);
      }
    });

    const currentYear = new Date().getFullYear();
    this.years = Array.from({length: 101}, (_, i) => currentYear - i).sort((a, b) => b - a);
  }

  loadTrack(id: number): void {
    this.trackService.findById(id).subscribe({
      next: (track: Track) => this.trackForm.patchValue(track),
      error: () => this.errorMessage = 'Error loading track'
    });
  }

  onSubmit(): void {
    if(this.trackForm.invalid) {
      return;
    }

    const trackData: Track = {
      title: this.trackForm.value.title,
      durationSeconds: this.trackForm.value.durationSeconds,
      yearReleased: this.trackForm.value.yearReleased,
      beatsPerMinute: this.trackForm.value.beatsPerMinute,
    };

    if(this.isEditMode && this.trackId != null) {
      // Update
      trackData.id = this.trackId;
      this.trackService.update(this.trackId, trackData).subscribe({
        next: () => this.router.navigate(['track/all']),
        error: () => this.errorMessage = 'Error updating track'
      });
    } else {
      // Create new
      this.trackService.create(trackData).subscribe({
        next: () => this.router.navigate(['track/all']),
        error: () => this.errorMessage = 'Error creating track'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['track/all']);
  }

}
