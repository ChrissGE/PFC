import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuestionariesComponent } from './cuestionaries.component';

describe('CuestionariesComponent', () => {
  let component: CuestionariesComponent;
  let fixture: ComponentFixture<CuestionariesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CuestionariesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CuestionariesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
