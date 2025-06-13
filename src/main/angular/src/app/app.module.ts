import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from "./app.component";
import {MenuModule} from "./components/menu/menu.module";
import { TrackModule } from './components/track/track.module';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MenuModule,
    HttpClientModule,
    TrackModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
