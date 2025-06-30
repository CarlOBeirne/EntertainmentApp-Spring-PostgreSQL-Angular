import { Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Router} from '@angular/router';
import { PlaylistService} from '../../../services/playlist.service';
import { TrackService} from '../../../services/track.service';
import { Playlist} from '../../../models/playlist';
import { Track} from '../../../models/track';

@Component({
  selector: 'app-playlist-form',
  templateUrl: './playlist-form.component.html',
  styleUrls: ['./playlist-form.component.css']
})
export class PlaylistFormComponent implements OnInit {

  playlistForm!: FormGroup;
  isEditMode = false;
  playlistId?: number;
  errorMessage = '';

  trackList: Track[] = [];

  constructor(
    private fb: FormBuilder,
    private playlistService: PlaylistService,
    private trackService: TrackService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.playlistForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', [Validators.maxLength(255)]],
      tracks: [[], Validators.required]  // multi-select for track ids
    });

    this.loadTracks();

    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.isEditMode = true;
        this.playlistId = +idParam;
        this.loadPlaylist(this.playlistId);
      }
    });
  }

  loadTracks(): void {
    this.trackService.findAll().subscribe({
      next: tracks => this.trackList = tracks,
      error: () => console.error('Error loading tracks')
    });
  }

  loadPlaylist(id: number): void {
    this.playlistService.findPlaylistById(id).subscribe({
      next: playlist => {
        const tracks = playlist.tracks.map(track => {
          return this.trackList.find(track => track.id === id);
        }).filter(Boolean) as Track[];
        this.playlistForm.patchValue({
          name: playlist.name,
          description: playlist.description,
          tracks: tracks
        });
      },
      error: () => console.error('Error loading playlist')
    });
  }

  onSubmit(): void {
    if (this.playlistForm.invalid) {
      return;
    }

    const formValue = this.playlistForm.value;

    const playlist: Playlist = {
      id: this.isEditMode ? this.playlistId : undefined,
      name: formValue.name,
      description: formValue.description,
      tracks: formValue.tracks.map((trackId: number) => this.trackList.find(t => t.id === trackId)!)
    };

    if (this.isEditMode && this.playlistId) {
      this.playlistService.updatePlaylist(this.playlistId, playlist).subscribe({
        next: () => this.router.navigate(['/playlist/all']),
        error: () => this.errorMessage = 'Error updating playlist'
      });
    } else {
      this.playlistService.createPlaylist(playlist).subscribe({
        next: () => this.router.navigate(['/playlist/all']),
        error: () => this.errorMessage = 'Error creating playlist'
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/playlist/all']);
  }
}
