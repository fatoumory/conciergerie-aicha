package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.CouverturePartenaireAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.IntegrationTest;
import sn.orange.conciergerie.domain.CouverturePartenaire;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.repository.CouverturePartenaireRepository;
import sn.orange.conciergerie.service.CouverturePartenaireService;
import sn.orange.conciergerie.service.dto.CouverturePartenaireDTO;
import sn.orange.conciergerie.service.mapper.CouverturePartenaireMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CouverturePartenaireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CouverturePartenaireResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.parse("2023-12-06");

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.parse("2023-12-06");

    private static final String ENTITY_API_URL = "/api/couverture-partenaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CouverturePartenaireRepository couverturePartenaireRepository;

    @Mock
    private CouverturePartenaireRepository couverturePartenaireRepositoryMock;

    @Autowired
    private CouverturePartenaireMapper couverturePartenaireMapper;

    @Mock
    private CouverturePartenaireService couverturePartenaireServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCouverturePartenaireMockMvc;

    private CouverturePartenaire couverturePartenaire;

    private CouverturePartenaire insertedCouverturePartenaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouverturePartenaire createEntity(EntityManager em) {
        CouverturePartenaire couverturePartenaire = new CouverturePartenaire().dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        couverturePartenaire.setPartenaire(partenaire);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        couverturePartenaire.setService(serviceConciergerie);
        return couverturePartenaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CouverturePartenaire createUpdatedEntity(EntityManager em) {
        CouverturePartenaire updatedCouverturePartenaire = new CouverturePartenaire()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createUpdatedEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        updatedCouverturePartenaire.setPartenaire(partenaire);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createUpdatedEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        updatedCouverturePartenaire.setService(serviceConciergerie);
        return updatedCouverturePartenaire;
    }

    @BeforeEach
    void initTest() {
        couverturePartenaire = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCouverturePartenaire != null) {
            couverturePartenaireRepository.delete(insertedCouverturePartenaire);
            insertedCouverturePartenaire = null;
        }
    }

    @Test
    @Transactional
    void createCouverturePartenaire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CouverturePartenaire
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);
        var returnedCouverturePartenaireDTO = om.readValue(
            restCouverturePartenaireMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couverturePartenaireDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CouverturePartenaireDTO.class
        );

        // Validate the CouverturePartenaire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCouverturePartenaire = couverturePartenaireMapper.toEntity(returnedCouverturePartenaireDTO);
        assertCouverturePartenaireUpdatableFieldsEquals(
            returnedCouverturePartenaire,
            getPersistedCouverturePartenaire(returnedCouverturePartenaire)
        );

        insertedCouverturePartenaire = returnedCouverturePartenaire;
    }

    @Test
    @Transactional
    void createCouverturePartenaireWithExistingId() throws Exception {
        // Create the CouverturePartenaire with an existing ID
        insertedCouverturePartenaire = couverturePartenaireRepository.saveAndFlush(couverturePartenaire);
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouverturePartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couverturePartenaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        couverturePartenaire.setDateDebut(null);

        // Create the CouverturePartenaire, which fails.
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        restCouverturePartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couverturePartenaireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCouverturePartenaires() throws Exception {
        // Initialize the database
        insertedCouverturePartenaire = couverturePartenaireRepository.saveAndFlush(couverturePartenaire);

        // Get all the couverturePartenaireList
        restCouverturePartenaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(couverturePartenaire.getId().toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCouverturePartenairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(couverturePartenaireServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCouverturePartenaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(couverturePartenaireServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCouverturePartenairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(couverturePartenaireServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCouverturePartenaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(couverturePartenaireRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCouverturePartenaire() throws Exception {
        // Initialize the database
        insertedCouverturePartenaire = couverturePartenaireRepository.saveAndFlush(couverturePartenaire);

        // Get the couverturePartenaire
        restCouverturePartenaireMockMvc
            .perform(get(ENTITY_API_URL_ID, couverturePartenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(couverturePartenaire.getId().toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCouverturePartenaire() throws Exception {
        // Get the couverturePartenaire
        restCouverturePartenaireMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCouverturePartenaire() throws Exception {
        // Initialize the database
        insertedCouverturePartenaire = couverturePartenaireRepository.saveAndFlush(couverturePartenaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the couverturePartenaire
        CouverturePartenaire updatedCouverturePartenaire = couverturePartenaireRepository
            .findById(couverturePartenaire.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCouverturePartenaire are not directly saved in db
        em.detach(updatedCouverturePartenaire);
        updatedCouverturePartenaire.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(updatedCouverturePartenaire);

        restCouverturePartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couverturePartenaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couverturePartenaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCouverturePartenaireToMatchAllProperties(updatedCouverturePartenaire);
    }

    @Test
    @Transactional
    void putNonExistingCouverturePartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couverturePartenaire.setId(UUID.randomUUID());

        // Create the CouverturePartenaire
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouverturePartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, couverturePartenaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couverturePartenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCouverturePartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couverturePartenaire.setId(UUID.randomUUID());

        // Create the CouverturePartenaire
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouverturePartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(couverturePartenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCouverturePartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couverturePartenaire.setId(UUID.randomUUID());

        // Create the CouverturePartenaire
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouverturePartenaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(couverturePartenaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCouverturePartenaireWithPatch() throws Exception {
        // Initialize the database
        insertedCouverturePartenaire = couverturePartenaireRepository.saveAndFlush(couverturePartenaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the couverturePartenaire using partial update
        CouverturePartenaire partialUpdatedCouverturePartenaire = new CouverturePartenaire();
        partialUpdatedCouverturePartenaire.setId(couverturePartenaire.getId());

        partialUpdatedCouverturePartenaire.dateDebut(UPDATED_DATE_DEBUT);

        restCouverturePartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouverturePartenaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCouverturePartenaire))
            )
            .andExpect(status().isOk());

        // Validate the CouverturePartenaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouverturePartenaireUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCouverturePartenaire, couverturePartenaire),
            getPersistedCouverturePartenaire(couverturePartenaire)
        );
    }

    @Test
    @Transactional
    void fullUpdateCouverturePartenaireWithPatch() throws Exception {
        // Initialize the database
        insertedCouverturePartenaire = couverturePartenaireRepository.saveAndFlush(couverturePartenaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the couverturePartenaire using partial update
        CouverturePartenaire partialUpdatedCouverturePartenaire = new CouverturePartenaire();
        partialUpdatedCouverturePartenaire.setId(couverturePartenaire.getId());

        partialUpdatedCouverturePartenaire.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restCouverturePartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCouverturePartenaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCouverturePartenaire))
            )
            .andExpect(status().isOk());

        // Validate the CouverturePartenaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCouverturePartenaireUpdatableFieldsEquals(
            partialUpdatedCouverturePartenaire,
            getPersistedCouverturePartenaire(partialUpdatedCouverturePartenaire)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCouverturePartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couverturePartenaire.setId(UUID.randomUUID());

        // Create the CouverturePartenaire
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouverturePartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, couverturePartenaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couverturePartenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCouverturePartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couverturePartenaire.setId(UUID.randomUUID());

        // Create the CouverturePartenaire
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouverturePartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(couverturePartenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCouverturePartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        couverturePartenaire.setId(UUID.randomUUID());

        // Create the CouverturePartenaire
        CouverturePartenaireDTO couverturePartenaireDTO = couverturePartenaireMapper.toDto(couverturePartenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCouverturePartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(couverturePartenaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CouverturePartenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCouverturePartenaire() throws Exception {
        // Initialize the database
        insertedCouverturePartenaire = couverturePartenaireRepository.saveAndFlush(couverturePartenaire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the couverturePartenaire
        restCouverturePartenaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, couverturePartenaire.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return couverturePartenaireRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CouverturePartenaire getPersistedCouverturePartenaire(CouverturePartenaire couverturePartenaire) {
        return couverturePartenaireRepository.findById(couverturePartenaire.getId()).orElseThrow();
    }

    protected void assertPersistedCouverturePartenaireToMatchAllProperties(CouverturePartenaire expectedCouverturePartenaire) {
        assertCouverturePartenaireAllPropertiesEquals(
            expectedCouverturePartenaire,
            getPersistedCouverturePartenaire(expectedCouverturePartenaire)
        );
    }

    protected void assertPersistedCouverturePartenaireToMatchUpdatableProperties(CouverturePartenaire expectedCouverturePartenaire) {
        assertCouverturePartenaireAllUpdatablePropertiesEquals(
            expectedCouverturePartenaire,
            getPersistedCouverturePartenaire(expectedCouverturePartenaire)
        );
    }
}
