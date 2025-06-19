import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GenreListComponent} from "./genre-list/genre-list.component";
import {GenreFormComponent} from "./genre-form/genre-form.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ArtistModule} from "../artist/artist.module";


@NgModule({
  declarations: [GenreListComponent, GenreFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    ArtistModule
  ]
})
export class GenreModule { }
