import {Component, OnInit} from '@angular/core';
import {Artist} from "../../../models/artist";
import {ArtistService} from "../../../services/artist.service";
import {ArtistType} from "../../../models/artist-type";
import {ActivatedRoute, Router} from "@angular/router";
import {TrackService} from "../../../services/track.service";
import {Track} from "../../../models/track";
import {Observable, tap} from "rxjs";

@Component({
  selector: 'app-artist-list',
  templateUrl: './artist-list.component.html',
  styleUrls: ['./artist-list.component.css']
})
export class ArtistListComponent implements OnInit {
  artistList: Artist[] = [];
  errorMessage: string = '';
  artistType = ArtistType;
  searchArtist: string = '';
  filteredArtists: Artist[] = [];
  currentTrackId: number | null | undefined;
  isFilteredByTrack: boolean = false;

  constructor(private artistService: ArtistService,
              private route: ActivatedRoute,
              private router: Router,
              private trackService: TrackService) {  }

  loadArtists(): Observable<Artist[]> {
    return this.artistService.findAllArtists().pipe(
      tap(artists => {
        this.artistList = artists;
      }))
  }

  createArtist(): void {
    this.router.navigate([`/artist/new`]);
  }

  updateArtist(artist: Artist): void {
    this.router.navigate([`/artist/update/${artist.id}`]);
  }

  viewTracks(artist: Artist): void {
    this.router.navigate([`/artist/${artist.id}/tracks`]);
  }

  deleteArtist(artist: Artist): void {
    if (artist.id == null) {
      return;
    }
    if (confirm(`Are you sure you want to delete artist ${artist.name}`)) {
      this.artistService.deleteArtist(artist.id).subscribe({
        next: () => this.loadArtists(),
        error: () => this.errorMessage = 'Error deleting artist'
      })
    }
  }

  getArtistType(type: string): string {
    return this.artistType[type as keyof typeof ArtistType]
  }

  ngOnInit(): void {
    this.loadArtists().subscribe(() =>
      this.route.paramMap.subscribe(params => {
        const trackIdParam = params.get('id');
        if (trackIdParam) {
          this.currentTrackId = +trackIdParam;
          this.isFilteredByTrack = true;
          this.filterTrackByArtist(this.currentTrackId);
        } else {
          this.isFilteredByTrack = false;
          this.filteredArtists = [...this.artistList];
        }
    }));
  }

  private filterTrackByArtist(trackId: number): void {
    this.trackService.findById(trackId).subscribe({
      next: (track: Track) => {
        if(track) {
          this.filteredArtists = track.artists.map((trackedArtist => {
            return this.artistList.find(persistedArtist => persistedArtist.id === trackedArtist.id )
          }))
            .filter(Boolean) as Artist[];
        } else {
          this.filteredArtists = [];
        }
      }
    })
  }

}
