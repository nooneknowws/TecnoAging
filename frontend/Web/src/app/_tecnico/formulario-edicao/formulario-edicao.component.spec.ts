import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioEdicaoComponent } from './formulario-edicao.component';

describe('FormularioEdicaoComponent', () => {
  let component: FormularioEdicaoComponent;
  let fixture: ComponentFixture<FormularioEdicaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormularioEdicaoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioEdicaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
