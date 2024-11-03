import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SedentarismoComponent } from './sedentarismo.component';

describe('SedentarismoComponent', () => {
  let component: SedentarismoComponent;
  let fixture: ComponentFixture<SedentarismoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SedentarismoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SedentarismoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
