import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinimentalComponent } from './minimental.component';

describe('MinimentalComponent', () => {
  let component: MinimentalComponent;
  let fixture: ComponentFixture<MinimentalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MinimentalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MinimentalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
