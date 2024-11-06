import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormSedentarismoComponent } from './form-sedentarismo.component';

describe('FormSedentarismoComponent', () => {
  let component: FormSedentarismoComponent;
  let fixture: ComponentFixture<FormSedentarismoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormSedentarismoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormSedentarismoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
