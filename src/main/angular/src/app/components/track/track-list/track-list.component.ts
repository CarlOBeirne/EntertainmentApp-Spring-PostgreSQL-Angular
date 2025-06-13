import { Component, OnInit } from '@angular/core';
import { Track } from 'src/app/models/track';
import { TrackService } from 'src/app/services/track.service';

@Component({
  selector: 'app-track-list',
  templateUrl: './track-list.component.html',
  styleUrls: ['./track-list.component.css']
})
export class TrackListComponent implements OnInit {
  tracks: Track[] = [];
  errorMessage = '';

  constructor(private trackService: TrackService) {}

  ngOnInit(): void {
    this.loadTracks();
  }

  loadTracks(): void {
    this.trackService.findAll().subscribe({
      next: (tracks) => (this.tracks = tracks),
      error: (err) => (this.errorMessage = `${err}`), // I think this allows the exact backend message to be displayed.
    })
  }

}
