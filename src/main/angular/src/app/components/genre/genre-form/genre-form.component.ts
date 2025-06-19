import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {GenreService} from "../../../services/genre.service";
import {Genre} from "../../../models/genre";

@Component({
  selector: 'app-genre-form',
  templateUrl: './genre-form.component.html',
  styleUrls: ['./genre-form.component.css']
})
export class GenreFormComponent implements OnInit {
  genreForm!: FormGroup;
  isEditMode: boolean = false;
  genreId?: number;
  errorMessage: string = '';

  constructor(private fb: FormBuilder,
              private genreService: GenreService,
              private route: ActivatedRoute,
              private router: Router) {}

  ngOnInit(): void {
    this.genreForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]]
    })

    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if(idParam) {
        this.isEditMode = true;
        this.genreId = +idParam;
        this.loadGenre(this.genreId);
      }
    })
  }

  loadGenre(id: number): void {
    this.genreService.findById(id).subscribe({
      next: (genre) => this.genreForm.patchValue(genre),
      error: () => this.errorMessage = "Error fetching genre"
    })
  }

  onSubmit(): void {
    if(this.genreForm.invalid) {
      return;
    }
    const genreData: Genre = {
      name: this.genreForm.value.name,
      description: this.genreForm.value.description
    }
    if(this.isEditMode && this.genreId != null) {
      genreData.id = this.genreId;
      this.genreService.update(this.genreId!, genreData).subscribe({
        next: () => this.router.navigate(['/genre/all']),
        error: () => this.errorMessage = 'Error updating genre'
      });
    } else {
      this.genreService.create(genreData).subscribe({
        next: () => this.router.navigate(['genre/all']),
        error: () => this.errorMessage = 'Error creating genre'
      });
    }
  }
}
