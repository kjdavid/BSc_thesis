import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StoreProductListComponent } from './store-product-list.component';

describe('OrderComponent', () => {
  let component: StoreProductListComponent;
  let fixture: ComponentFixture<StoreProductListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StoreProductListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StoreProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
