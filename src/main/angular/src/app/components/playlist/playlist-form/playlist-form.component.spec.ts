import {ComponentFixture, TestBed} from '@angular/core/testing';
import {PlaylistFormComponent} from './playlist-form.component';
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {PlaylistService} from '../../../services/playlist.service';

describe('PlaylistFormComponent', () => {
  let component: PlaylistFormComponent;
  let fixture: ComponentFixture<PlaylistFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlaylistFormComponent],
      imports: [
        ReactiveFormsModule,
        FormsModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [PlaylistService]
    }).compileComponents();

    fixture = TestBed.createComponent(PlaylistFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
