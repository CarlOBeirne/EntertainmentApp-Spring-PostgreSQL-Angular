import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TrackListComponent} from './track-list/track-list.component';
import {TrackFormComponent} from './track-form/track-form.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ArtistModule} from "../artist/artist.module";


@NgModule({
  declarations: [TrackListComponent, TrackFormComponent],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, ArtistModule]
})
export class TrackModule { }
