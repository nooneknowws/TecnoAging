import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormMinimentalComponent } from './form-minimental.component';

describe('FormMinimentalComponent', () => {
  let component: FormMinimentalComponent;
  let fixture: ComponentFixture<FormMinimentalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormMinimentalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormMinimentalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
