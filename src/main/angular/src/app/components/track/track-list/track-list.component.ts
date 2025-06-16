import {Component, OnInit} from '@angular/core';
import {Track} from 'src/app/models/track';
import {TrackService} from 'src/app/services/track.service';
import {Router} from "@angular/router";
import {map, Observable, of} from "rxjs";
import {Artist} from "../../../models/artist";


@Component({
  selector: 'app-track-list',
  templateUrl: './track-list.component.html',
  styleUrls: ['./track-list.component.css']
})
export class TrackListComponent implements OnInit {
  tracks: Track[] = [];
  errorMessage = '';

  constructor(private trackService: TrackService, private router: Router) {
  }

  ngOnInit(): void {
    this.loadTracks();
  }

  loadTracks(): void {
    this.trackService.findAll().subscribe({
      next: (tracks) => (this.tracks = tracks),
      error: (err) => (this.errorMessage = `${err}`), // I think this allows the exact backend message to be displayed.
    })
  }

  addTrack(): void {
    this.router.navigate(['/track/new']);
  }

  editTrack(track: Track): void {
    this.router.navigate([`/track/update`, track.id]);
  }

  deleteTrack(track: Track): void {
    if (track.id === null) {
      return;
    }
    if (confirm(`Are you sure you want to delete "${track.title}"?`)) {
      this.trackService.delete(track.id).subscribe({
        next: () => this.loadTracks(),
        error: () => (this.errorMessage = "Error deleting track")
      })
    }
  }

  trackByTrackId(index: number, track: Track): number {
    return track.id ? track.id : index
  }

  getArtistNames(artists: Set<Artist>): string {
    if (!artists || artists.size === 0) {
      return '';
    }
    return Array.from(artists).map(artist => artist.name).join(', ');
  }
}
