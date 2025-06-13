import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from "./app.component";
import {MenuModule} from "./components/menu/menu.module";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TrackListComponent } from './components/track/track-list/track-list.component';
import { TrackModule } from './components/track/track.module';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MenuModule
    HttpClientModule,
    TrackModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
