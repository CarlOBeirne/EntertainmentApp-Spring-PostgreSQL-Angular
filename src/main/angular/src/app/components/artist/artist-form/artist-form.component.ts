import {Component, OnInit} from '@angular/core';
import {ArtistType} from "../../../models/artist-type";
import {Country} from "../../../models/country";
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {ArtistService} from "../../../services/artist.service";
import {Artist} from "../../../models/artist";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-artist-form',
  templateUrl: './artist-form.component.html',
  styleUrls: ['./artist-form.component.css']
})
export class ArtistFormComponent implements OnInit {
  artistType = ArtistType;
  artistTypeEnumKeys= Object.keys(this.artistType) as Array<keyof typeof ArtistType>;
  countryListApiUrl = 'https://restcountries.com/v3.1/all?fields=name'
  countryList: String[] = [];
  years: number[] = [];

  artistForm!: FormGroup;
  isEditMode: boolean = false;
  artistId?: number;
  errorMessage: string = '';



  constructor(private http: HttpClient,
              private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private artistService: ArtistService) {  }

  getCountries(): Observable<String[]> {
    return this.http.get<Country[]>(`${this.countryListApiUrl}`).pipe(
      map(countries => countries.map(country => country.name.common))
    );
  }

  cancel(): void {
    this.router.navigate(['/artist/all']);
  }

  onSubmit(): void {
    if (this.artistForm.invalid) {
      return;
    }
    const artist: Artist = {
      name: this.artistForm.value.name,
      artistType: this.artistForm.value.artistType,
      biography: this.artistForm.value.biography,
      nationality: this.artistForm.value.nationality,
      yearFounded: this.artistForm.value.yearFounded
    }
    if (this.isEditMode && this.artistId !== null) {
      artist.id = this.artistId;
      this.artistService.updateArtist(this.artistId!, artist).subscribe({
        next: () => this.router.navigate(['/artist/all']),
        error: () => this.errorMessage = 'Error updating artist'
      });
    } else {
      this.artistService.createArtist(artist).subscribe({
        next: () => this.router.navigate(['/artist/all']),
        error: (err) => this.errorMessage = err.value
      })
    }
  }

  loadArtist(id: number): void {
    this.artistService.findArtistById(id).subscribe({
      next: (artist) => this.artistForm.patchValue(artist),
      error: () => console.log("Error fetching artist")
    });
  }

  ngOnInit(): void {
    this.artistForm = this.fb.group({
      name: ['', [Validators.required, ]],
      artistType: [null, [Validators.required]],
      nationality: [null, [Validators.required]],
      yearFounded: [null, [Validators.required]],
      biography: ['', [Validators.maxLength(255)]],
    })

    if (this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.isEditMode = true;
        this.artistId = +idParam;
        this.loadArtist(this.artistId)
      }
    }))

    this.getCountries().subscribe({
      next: (countries) => this.countryList = countries.sort(),
      error: () => console.log("Error getting countries")
    })
    const currentYear = new Date().getFullYear();
    this.years = Array.from({length: 101}, (_, i) => currentYear - i).sort((a, b) => b - a);

  }
}
