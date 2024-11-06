import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormPfsComponent } from './form-pfs.component';

describe('FormPfsComponent', () => {
  let component: FormPfsComponent;
  let fixture: ComponentFixture<FormPfsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormPfsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormPfsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
