import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackFormComponent } from './track-form.component';

describe('TrackFormComponent', () => {
  let component: TrackFormComponent;
  let fixture: ComponentFixture<TrackFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrackFormComponent]
    });
    fixture = TestBed.createComponent(TrackFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
