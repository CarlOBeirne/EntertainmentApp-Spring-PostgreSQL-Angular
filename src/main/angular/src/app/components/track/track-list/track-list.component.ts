import {Component, OnInit} from '@angular/core';
import {Track} from 'src/app/models/track';
import {TrackService} from 'src/app/services/track.service';
import {ActivatedRoute, Router} from "@angular/router";
import {map} from "rxjs";
import {Artist} from "../../../models/artist";

@Component({
  selector: 'app-track-list',
  templateUrl: './track-list.component.html',
  styleUrls: ['./track-list.component.css']
})
export class TrackListComponent implements OnInit {
  tracks: Track[] = [];
  errorMessage = '';
  artistId?: number;
  searchTrack: string = '';
  selectedArtistId: number | null = null;

  constructor(private trackService: TrackService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      let artistId = params.get('artistId');
      if (artistId) {
        this.artistId = +artistId;
        this.loadArtistTracks(this.artistId);
      } else {
        this.loadTracks();
      }
    });
  }

  loadArtistTracks(artistId: number): void {
    this.trackService.findAll()
      .pipe(
        map(tracks => {
          return tracks.filter(track => {
             return track.artists.some(artist => artist.id === artistId);
          })
        })
      )
      .subscribe({
        next: (t) => this.tracks = t
      })
  }

  loadTracks(): void {
    this.trackService.findAll().subscribe({
      next: (tracks) => (this.tracks = tracks),
      error: (err) => (this.errorMessage = `${err}`),
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

  getArtistNames(artists: Artist[]): string {
    if (!artists || artists.length === 0) {
      return '';
    }
    return Array.from(artists).map(artist => artist.name).join(', ');
  }

  public viewArtistDetails(trackId: number): void {
    this.router.navigate(['/artist/all/', trackId]);
  }

}
