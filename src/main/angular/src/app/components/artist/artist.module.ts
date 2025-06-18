import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ArtistFormComponent} from "./artist-form/artist-form.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ArtistListComponent} from "./artist-list/artist-list.component";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {SearchPipe} from './search.pipe';


@NgModule({
  declarations: [
    ArtistFormComponent,
    ArtistListComponent,
    SearchPipe
  ],
  exports: [
    SearchPipe
  ],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    RouterLink,
    RouterLinkActive,
    FormsModule
  ]
})
export class ArtistModule { }
