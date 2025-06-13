import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ArtistFormComponent} from "./artist-form/artist-form.component";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    ArtistFormComponent
  ],
  imports: [
    ReactiveFormsModule,
    CommonModule
  ]
})
export class ArtistModule { }
