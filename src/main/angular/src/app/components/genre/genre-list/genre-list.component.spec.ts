import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenreListComponent} from './genre-list.component';

describe('GenreListComponent', () => {
  let component: GenreListComponent;
  let fixture: ComponentFixture<GenreListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GenreListComponent]
    });
    fixture = TestBed.createComponent(GenreListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
