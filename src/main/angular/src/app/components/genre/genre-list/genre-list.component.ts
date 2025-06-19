import {Component, OnInit} from '@angular/core';
import {Genre} from "../../../models/genre";
import {ActivatedRoute, Router} from "@angular/router";
import {GenreService} from "../../../services/genre.service";

@Component({
  selector: 'app-genre-list',
  templateUrl: './genre-list.component.html',
  styleUrls: ['./genre-list.component.css']
})
export class GenreListComponent implements OnInit {
  genres: Genre[] = [];
  errorMessage: string = '';
  searchGenre: string  = '';

  constructor(private genreService: GenreService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.loadGenres();
  }

  loadGenres(): void {
    this.genreService.findAll().subscribe({
      next: (genres) => (this.genres = genres),
      error: (err) => (this.errorMessage = `${err}`),
    })
  }

  addGenre(): void {
    this.router.navigate(['/genre/new']);
  }

  editGenre(genre: Genre): void {
    this.router.navigate(['genre/update/', genre.id]);
  }

  deleteGenre(genre: Genre): void {
    if(genre.id=== null) {
      return;
    }
    if(confirm(`Are you sure you want to delete "${genre.name}"?`)) {
      this.genreService.delete(genre.id).subscribe({
        next: () => this.loadGenres(),
        error: () => (this.errorMessage = "Error deleting genre")
      })
    }
  }

}
