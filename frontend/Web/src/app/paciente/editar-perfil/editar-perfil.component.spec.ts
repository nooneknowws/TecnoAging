import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarPerfilComponentPaciente } from './editar-perfil.component';

describe('EditarPerfilComponent', () => {
  let component: EditarPerfilComponentPaciente;
  let fixture: ComponentFixture<EditarPerfilComponentPaciente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditarPerfilComponentPaciente]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditarPerfilComponentPaciente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
