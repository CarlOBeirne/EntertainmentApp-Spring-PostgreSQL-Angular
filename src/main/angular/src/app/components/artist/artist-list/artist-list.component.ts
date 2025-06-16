import {Component, OnInit} from '@angular/core';
import {Artist} from "../../../models/artist";
import {ArtistService} from "../../../services/artist.service";
import {ArtistType} from "../../../models/artist-type";
import {Router} from "@angular/router";

@Component({
  selector: 'app-artist-list',
  templateUrl: './artist-list.component.html',
  styleUrls: ['./artist-list.component.css']
})
export class ArtistListComponent implements OnInit {
  artistList: Artist[] = [];
  errorMessage: string = '';
  artistType = ArtistType;

  constructor(private artistService: ArtistService,
              private router: Router) {  }

  loadArtists(): void {
    this.artistService.findAllArtists().subscribe({
      next: (artistList) => this.artistList = artistList,
      error: () => this.errorMessage = 'Error loading artists'
    })
  }

  createArtist(): void {
    this.router.navigate([`/artist/new`]);
  }

  updateArtist(artist: Artist): void {
    this.router.navigate([`/artist/update/${artist.id}`]);
  }

  deleteArtist(artist: Artist): void {
    if (artist.id == null) {
      return;
    }
    if (confirm(`Are you sure you want to delete artist ${artist.name}`)) {
      this.artistService.deleteArtist(artist.id).subscribe({
        next: () => this.loadArtists(),
        error: (err) => this.errorMessage = 'Error deleting artist'
      })
    }
  }

  getArtistType(type: string): string {
    return this.artistType[type as keyof typeof ArtistType]
  }

  ngOnInit(): void {
    this.loadArtists();
  }

}
