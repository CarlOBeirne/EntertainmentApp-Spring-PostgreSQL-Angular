import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TrackService} from "../../../services/track.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Track} from "../../../models/track";
import {ArtistService} from "../../../services/artist.service";
import {Artist} from "../../../models/artist";
import {ArtistType} from "../../../models/artist-type";

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
  allArtists: Artist[] = [];
  registeredArtist: Artist[] = [];
  artistType = ArtistType;
  artistTypeEnumKeys = Object.keys(this.artistType) as Array<keyof typeof ArtistType>;

  constructor(
    private formBuilder: FormBuilder,
    private trackService: TrackService,
    private artistService: ArtistService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.artistService.findAllArtists().subscribe({
      next: (artists) => this.allArtists = artists,
      error: () => this.errorMessage = 'Error loading artists'
    });

    this.trackForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      durationSeconds: ['', [Validators.required, Validators.min(1)]],
      yearReleased: ['', [Validators.required]],
      beatsPerMinute: ['', [Validators.required, Validators.min(1)]],
      artists: [[null], [Validators.required]],
    });

    // Check if route has an id
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
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
      next: (track: Track) => {
        this.trackForm.patchValue({
          title: track.title,
          durationSeconds: track.durationSeconds,
          yearReleased: track.yearReleased,
          beatsPerMinute: track.beatsPerMinute,
          artists: Array.of(track.artists)
        });
        this.registeredArtist = track.artists || [];
        },
      error: () => this.errorMessage = 'Error loading track'
    });
  }

  onSubmit(): void {
    if (this.trackForm.invalid) {
      console.log("dfdfd")
      return;
    }

    const formValues = this.trackForm.value;
    const trackData: Track = {
      title: formValues.title,
      durationSeconds: formValues.durationSeconds,
      yearReleased: formValues.yearReleased,
      beatsPerMinute: formValues.beatsPerMinute,
      artists: Array.of(formValues.artists),
    };

    if (this.isEditMode && this.trackId != null) {
      // Update
      trackData.id = this.trackId;
      this.trackService.update(this.trackId!, trackData).subscribe({
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

  // Add an artist to a track
  addArtist(artistId: number): void {
    if(!this.trackId) return;
    this.trackService.addArtist(this.trackId, artistId).subscribe({
      next: (updatedTrack) => {
        this.registeredArtist = updatedTrack.artists || [];
      },
      error: () => this.errorMessage = 'Error adding artist'
    });
  }

  // Remove an artist from a track
  removeArtist(artistId: number): void {
    if(!this.trackId) return;
    this.trackService.removeArtist(this.trackId, artistId).subscribe({
      next: (updatedTrack) => {
      this.registeredArtist = updatedTrack.artists || [];
    },
      error: () => this.errorMessage = 'Error removing artist'
    });
  }

  isRegistered(artist: Artist): boolean {
    return this.registeredArtist.some(s => s.id === artist.id);
  }
}
