import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrackListComponent } from './track-list/track-list.component';
import { TrackFormComponent } from './track-form/track-form.component';
import {ReactiveFormsModule} from "@angular/forms";



@NgModule({
  declarations: [TrackListComponent, TrackFormComponent],
  imports: [CommonModule, ReactiveFormsModule]
})
export class TrackModule { }
