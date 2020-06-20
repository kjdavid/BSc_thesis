import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyProductListComponent } from './company-product-list.component';

describe('ListComponent', () => {
  let component: CompanyProductListComponent;
  let fixture: ComponentFixture<CompanyProductListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanyProductListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
