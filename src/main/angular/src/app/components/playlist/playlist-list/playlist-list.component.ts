import {Component, OnInit} from '@angular/core';
import {Playlist} from '../../../models/playlist';
import {PlaylistService} from '../../../services/playlist.service';
import {Router} from '@angular/router';
import {Artist} from "../../../models/artist";

@Component({
  selector: 'app-playlist-list',
  templateUrl: './playlist-list.component.html',
  styleUrls: ['./playlist-list.component.css']
})
export class PlaylistListComponent implements OnInit {

  playlists: Playlist[] = [];
  errorMessage: string = '';
  searchPlaylist: string = '';

  constructor(
    private playlistService: PlaylistService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPlaylists();
  }

  loadPlaylists(): void {
    this.playlistService.findAllPlaylist().subscribe({
      next: (data) => this.playlists = data,
      error: (err) => this.errorMessage = 'Error loading playlists'
    });
  }

  deletePlaylist(id: number | undefined): void {
    if (!id) return;
    if (!confirm('Are you sure you want to delete this playlist?')) return;

    this.playlistService.deletePlaylist(id).subscribe({
      next: () => this.loadPlaylists(),
      error: () => this.errorMessage = 'Error deleting playlist'
    });
  }

  editPlaylist(id: number | undefined): void {
    if (!id) return;
    this.router.navigate([`/playlist/update/${id}`]);
  }

  createNewPlaylist(): void {
    this.router.navigate(['/playlist/new']);
  }
}
