import { TestBed } from '@angular/core/testing';

import { SaleService } from './sale.service';

describe('SalesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SaleService = TestBed.get(SaleService);
    expect(service).toBeTruthy();
  });
});
