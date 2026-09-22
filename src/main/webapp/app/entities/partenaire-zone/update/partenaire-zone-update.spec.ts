import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartenaire } from 'app/entities/partenaire/partenaire.model';
import { PartenaireService } from 'app/entities/partenaire/service/partenaire.service';
import { ZoneService } from 'app/entities/zone/service/zone.service';
import { IZone } from 'app/entities/zone/zone.model';
import { IPartenaireZone } from '../partenaire-zone.model';
import { PartenaireZoneService } from '../service/partenaire-zone.service';

import { PartenaireZoneFormService } from './partenaire-zone-form.service';
import { PartenaireZoneUpdate } from './partenaire-zone-update';

describe('PartenaireZone Management Update Component', () => {
  let comp: PartenaireZoneUpdate;
  let fixture: ComponentFixture<PartenaireZoneUpdate>;
  let activatedRoute: ActivatedRoute;
  let partenaireZoneFormService: PartenaireZoneFormService;
  let partenaireZoneService: PartenaireZoneService;
  let partenaireService: PartenaireService;
  let zoneService: ZoneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PartenaireZoneUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partenaireZoneFormService = TestBed.inject(PartenaireZoneFormService);
    partenaireZoneService = TestBed.inject(PartenaireZoneService);
    partenaireService = TestBed.inject(PartenaireService);
    zoneService = TestBed.inject(ZoneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Partenaire query and add missing value', () => {
      const partenaireZone: IPartenaireZone = { id: '2f4bd363-1ad5-45e4-9e1d-da9594acf205' };
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      partenaireZone.partenaire = partenaire;

      const partenaireCollection: IPartenaire[] = [{ id: 'd330918e-c880-4b4f-a4ba-30a19287c322' }];
      vi.spyOn(partenaireService, 'query').mockReturnValue(of(new HttpResponse({ body: partenaireCollection })));
      const additionalPartenaires = [partenaire];
      const expectedCollection: IPartenaire[] = [...additionalPartenaires, ...partenaireCollection];
      vi.spyOn(partenaireService, 'addPartenaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partenaireZone });
      comp.ngOnInit();

      expect(partenaireService.query).toHaveBeenCalled();
      expect(partenaireService.addPartenaireToCollectionIfMissing).toHaveBeenCalledWith(
        partenaireCollection,
        ...additionalPartenaires.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partenairesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Zone query and add missing value', () => {
      const partenaireZone: IPartenaireZone = { id: '2f4bd363-1ad5-45e4-9e1d-da9594acf205' };
      const zone: IZone = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
      partenaireZone.zone = zone;

      const zoneCollection: IZone[] = [{ id: '876dfa56-710f-4774-ab70-e59edf28e4d6' }];
      vi.spyOn(zoneService, 'query').mockReturnValue(of(new HttpResponse({ body: zoneCollection })));
      const additionalZones = [zone];
      const expectedCollection: IZone[] = [...additionalZones, ...zoneCollection];
      vi.spyOn(zoneService, 'addZoneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partenaireZone });
      comp.ngOnInit();

      expect(zoneService.query).toHaveBeenCalled();
      expect(zoneService.addZoneToCollectionIfMissing).toHaveBeenCalledWith(
        zoneCollection,
        ...additionalZones.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.zonesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const partenaireZone: IPartenaireZone = { id: '2f4bd363-1ad5-45e4-9e1d-da9594acf205' };
      const partenaire: IPartenaire = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
      partenaireZone.partenaire = partenaire;
      const zone: IZone = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
      partenaireZone.zone = zone;

      activatedRoute.data = of({ partenaireZone });
      comp.ngOnInit();

      expect(comp.partenairesSharedCollection()).toContainEqual(partenaire);
      expect(comp.zonesSharedCollection()).toContainEqual(zone);
      expect(comp.partenaireZone).toEqual(partenaireZone);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartenaireZone>();
      const partenaireZone = { id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' };
      vi.spyOn(partenaireZoneFormService, 'getPartenaireZone').mockReturnValue(partenaireZone);
      vi.spyOn(partenaireZoneService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaireZone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partenaireZone);
      saveSubject.complete();

      // THEN
      expect(partenaireZoneFormService.getPartenaireZone).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partenaireZoneService.update).toHaveBeenCalledWith(expect.objectContaining(partenaireZone));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPartenaireZone>();
      const partenaireZone = { id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' };
      vi.spyOn(partenaireZoneFormService, 'getPartenaireZone').mockReturnValue({ id: null });
      vi.spyOn(partenaireZoneService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaireZone: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(partenaireZone);
      saveSubject.complete();

      // THEN
      expect(partenaireZoneFormService.getPartenaireZone).toHaveBeenCalled();
      expect(partenaireZoneService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPartenaireZone>();
      const partenaireZone = { id: 'e788d85e-e935-40a2-842c-c5f66c2028aa' };
      vi.spyOn(partenaireZoneService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partenaireZone });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partenaireZoneService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePartenaire', () => {
      it('should forward to partenaireService', () => {
        const entity = { id: 'd330918e-c880-4b4f-a4ba-30a19287c322' };
        const entity2 = { id: 'b6fc1546-6825-402d-9e00-2c8ab08f6246' };
        vi.spyOn(partenaireService, 'comparePartenaire');
        comp.comparePartenaire(entity, entity2);
        expect(partenaireService.comparePartenaire).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareZone', () => {
      it('should forward to zoneService', () => {
        const entity = { id: '876dfa56-710f-4774-ab70-e59edf28e4d6' };
        const entity2 = { id: '49d119f6-ada1-4ddf-abe4-39f8231db1c3' };
        vi.spyOn(zoneService, 'compareZone');
        comp.compareZone(entity, entity2);
        expect(zoneService.compareZone).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
