import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormFactfComponent } from './form-factf.component';

describe('FormFactfComponent', () => {
  let component: FormFactfComponent;
  let fixture: ComponentFixture<FormFactfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormFactfComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormFactfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
