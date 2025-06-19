import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TrackListComponent} from "./components/track/track-list/track-list.component";
import {ArtistFormComponent} from "./components/artist/artist-form/artist-form.component";
import {TrackFormComponent} from "./components/track/track-form/track-form.component";
import {ArtistListComponent} from "./components/artist/artist-list/artist-list.component";
import {LoginComponent} from "./components/auth/login/login.component";
import {RegistrationComponent} from "./components/auth/registration/registration.component";
import {HomeComponent} from "./components/home/home.component";
import {GenreListComponent} from "./components/genre/genre-list/genre-list.component";
import {GenreFormComponent} from "./components/genre/genre-form/genre-form.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegistrationComponent},
  {path: 'genre/new', component: GenreFormComponent},
  {path: 'genre/all', component: GenreListComponent},
  {path: 'genre/update/:id', component: GenreFormComponent},
  {path: 'artist/new', component: ArtistFormComponent},
  {path: 'artist/all', component: ArtistListComponent},
  {path: 'artist/all/:id', component: ArtistListComponent},
  {path: 'artist/update/:id', component: ArtistFormComponent},
  {path: 'artist/:artistId/tracks', component: TrackListComponent},
  {path: 'track/new', component: TrackFormComponent},
  {path: 'track/all', component: TrackListComponent},
  {path: 'track/update/:id', component: TrackFormComponent},
  {path: '', component: HomeComponent},
  {path: '', redirectTo: '/', pathMatch: 'full'},
  {path: '**', redirectTo: '/'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
