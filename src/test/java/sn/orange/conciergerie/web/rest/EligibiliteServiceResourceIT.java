package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.EligibiliteServiceAsserts.*;
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
import sn.orange.conciergerie.domain.EligibiliteService;
import sn.orange.conciergerie.domain.SegmentClient;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.domain.TypeClient;
import sn.orange.conciergerie.repository.EligibiliteServiceRepository;
import sn.orange.conciergerie.service.EligibiliteServiceService;
import sn.orange.conciergerie.service.dto.EligibiliteServiceDTO;
import sn.orange.conciergerie.service.mapper.EligibiliteServiceMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link EligibiliteServiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EligibiliteServiceResourceIT {

    private static final Boolean DEFAULT_AUTORISE = false;
    private static final Boolean UPDATED_AUTORISE = true;

    private static final Boolean DEFAULT_GRATUIT = false;
    private static final Boolean UPDATED_GRATUIT = true;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.parse("2023-12-06");

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.parse("2023-12-06");

    private static final String ENTITY_API_URL = "/api/eligibilite-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EligibiliteServiceRepository eligibiliteServiceRepository;

    @Mock
    private EligibiliteServiceRepository eligibiliteServiceRepositoryMock;

    @Autowired
    private EligibiliteServiceMapper eligibiliteServiceMapper;

    @Mock
    private EligibiliteServiceService eligibiliteServiceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEligibiliteServiceMockMvc;

    private EligibiliteService eligibiliteService;

    private EligibiliteService insertedEligibiliteService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EligibiliteService createEntity(EntityManager em) {
        EligibiliteService eligibiliteService = new EligibiliteService()
            .autorise(DEFAULT_AUTORISE)
            .gratuit(DEFAULT_GRATUIT)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        eligibiliteService.setService(serviceConciergerie);
        // Add required entity
        SegmentClient segmentClient;
        if (TestUtil.findAll(em, SegmentClient.class).isEmpty()) {
            segmentClient = SegmentClientResourceIT.createEntity();
            em.persist(segmentClient);
            em.flush();
        } else {
            segmentClient = TestUtil.findAll(em, SegmentClient.class).getFirst();
        }
        eligibiliteService.setSegmentClient(segmentClient);
        // Add required entity
        TypeClient typeClient;
        if (TestUtil.findAll(em, TypeClient.class).isEmpty()) {
            typeClient = TypeClientResourceIT.createEntity();
            em.persist(typeClient);
            em.flush();
        } else {
            typeClient = TestUtil.findAll(em, TypeClient.class).getFirst();
        }
        eligibiliteService.setTypeClient(typeClient);
        return eligibiliteService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EligibiliteService createUpdatedEntity(EntityManager em) {
        EligibiliteService updatedEligibiliteService = new EligibiliteService()
            .autorise(UPDATED_AUTORISE)
            .gratuit(UPDATED_GRATUIT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createUpdatedEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        updatedEligibiliteService.setService(serviceConciergerie);
        // Add required entity
        SegmentClient segmentClient;
        if (TestUtil.findAll(em, SegmentClient.class).isEmpty()) {
            segmentClient = SegmentClientResourceIT.createUpdatedEntity();
            em.persist(segmentClient);
            em.flush();
        } else {
            segmentClient = TestUtil.findAll(em, SegmentClient.class).getFirst();
        }
        updatedEligibiliteService.setSegmentClient(segmentClient);
        // Add required entity
        TypeClient typeClient;
        if (TestUtil.findAll(em, TypeClient.class).isEmpty()) {
            typeClient = TypeClientResourceIT.createUpdatedEntity();
            em.persist(typeClient);
            em.flush();
        } else {
            typeClient = TestUtil.findAll(em, TypeClient.class).getFirst();
        }
        updatedEligibiliteService.setTypeClient(typeClient);
        return updatedEligibiliteService;
    }

    @BeforeEach
    void initTest() {
        eligibiliteService = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEligibiliteService != null) {
            eligibiliteServiceRepository.delete(insertedEligibiliteService);
            insertedEligibiliteService = null;
        }
    }

    @Test
    @Transactional
    void createEligibiliteService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EligibiliteService
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);
        var returnedEligibiliteServiceDTO = om.readValue(
            restEligibiliteServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eligibiliteServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EligibiliteServiceDTO.class
        );

        // Validate the EligibiliteService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEligibiliteService = eligibiliteServiceMapper.toEntity(returnedEligibiliteServiceDTO);
        assertEligibiliteServiceUpdatableFieldsEquals(
            returnedEligibiliteService,
            getPersistedEligibiliteService(returnedEligibiliteService)
        );

        insertedEligibiliteService = returnedEligibiliteService;
    }

    @Test
    @Transactional
    void createEligibiliteServiceWithExistingId() throws Exception {
        // Create the EligibiliteService with an existing ID
        insertedEligibiliteService = eligibiliteServiceRepository.saveAndFlush(eligibiliteService);
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEligibiliteServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eligibiliteServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAutoriseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eligibiliteService.setAutorise(null);

        // Create the EligibiliteService, which fails.
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        restEligibiliteServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eligibiliteServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGratuitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eligibiliteService.setGratuit(null);

        // Create the EligibiliteService, which fails.
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        restEligibiliteServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eligibiliteServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        eligibiliteService.setDateDebut(null);

        // Create the EligibiliteService, which fails.
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        restEligibiliteServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eligibiliteServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEligibiliteServices() throws Exception {
        // Initialize the database
        insertedEligibiliteService = eligibiliteServiceRepository.saveAndFlush(eligibiliteService);

        // Get all the eligibiliteServiceList
        restEligibiliteServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibiliteService.getId().toString())))
            .andExpect(jsonPath("$.[*].autorise").value(hasItem(DEFAULT_AUTORISE)))
            .andExpect(jsonPath("$.[*].gratuit").value(hasItem(DEFAULT_GRATUIT)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEligibiliteServicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(eligibiliteServiceServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restEligibiliteServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eligibiliteServiceServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEligibiliteServicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eligibiliteServiceServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restEligibiliteServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(eligibiliteServiceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEligibiliteService() throws Exception {
        // Initialize the database
        insertedEligibiliteService = eligibiliteServiceRepository.saveAndFlush(eligibiliteService);

        // Get the eligibiliteService
        restEligibiliteServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, eligibiliteService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eligibiliteService.getId().toString()))
            .andExpect(jsonPath("$.autorise").value(DEFAULT_AUTORISE))
            .andExpect(jsonPath("$.gratuit").value(DEFAULT_GRATUIT))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEligibiliteService() throws Exception {
        // Get the eligibiliteService
        restEligibiliteServiceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEligibiliteService() throws Exception {
        // Initialize the database
        insertedEligibiliteService = eligibiliteServiceRepository.saveAndFlush(eligibiliteService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eligibiliteService
        EligibiliteService updatedEligibiliteService = eligibiliteServiceRepository.findById(eligibiliteService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEligibiliteService are not directly saved in db
        em.detach(updatedEligibiliteService);
        updatedEligibiliteService
            .autorise(UPDATED_AUTORISE)
            .gratuit(UPDATED_GRATUIT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(updatedEligibiliteService);

        restEligibiliteServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eligibiliteServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eligibiliteServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEligibiliteServiceToMatchAllProperties(updatedEligibiliteService);
    }

    @Test
    @Transactional
    void putNonExistingEligibiliteService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eligibiliteService.setId(UUID.randomUUID());

        // Create the EligibiliteService
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEligibiliteServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eligibiliteServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eligibiliteServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEligibiliteService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eligibiliteService.setId(UUID.randomUUID());

        // Create the EligibiliteService
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEligibiliteServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(eligibiliteServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEligibiliteService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eligibiliteService.setId(UUID.randomUUID());

        // Create the EligibiliteService
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEligibiliteServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(eligibiliteServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEligibiliteServiceWithPatch() throws Exception {
        // Initialize the database
        insertedEligibiliteService = eligibiliteServiceRepository.saveAndFlush(eligibiliteService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eligibiliteService using partial update
        EligibiliteService partialUpdatedEligibiliteService = new EligibiliteService();
        partialUpdatedEligibiliteService.setId(eligibiliteService.getId());

        partialUpdatedEligibiliteService.autorise(UPDATED_AUTORISE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restEligibiliteServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEligibiliteService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEligibiliteService))
            )
            .andExpect(status().isOk());

        // Validate the EligibiliteService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEligibiliteServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEligibiliteService, eligibiliteService),
            getPersistedEligibiliteService(eligibiliteService)
        );
    }

    @Test
    @Transactional
    void fullUpdateEligibiliteServiceWithPatch() throws Exception {
        // Initialize the database
        insertedEligibiliteService = eligibiliteServiceRepository.saveAndFlush(eligibiliteService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the eligibiliteService using partial update
        EligibiliteService partialUpdatedEligibiliteService = new EligibiliteService();
        partialUpdatedEligibiliteService.setId(eligibiliteService.getId());

        partialUpdatedEligibiliteService
            .autorise(UPDATED_AUTORISE)
            .gratuit(UPDATED_GRATUIT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restEligibiliteServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEligibiliteService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEligibiliteService))
            )
            .andExpect(status().isOk());

        // Validate the EligibiliteService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEligibiliteServiceUpdatableFieldsEquals(
            partialUpdatedEligibiliteService,
            getPersistedEligibiliteService(partialUpdatedEligibiliteService)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEligibiliteService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eligibiliteService.setId(UUID.randomUUID());

        // Create the EligibiliteService
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEligibiliteServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eligibiliteServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eligibiliteServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEligibiliteService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eligibiliteService.setId(UUID.randomUUID());

        // Create the EligibiliteService
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEligibiliteServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(eligibiliteServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEligibiliteService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        eligibiliteService.setId(UUID.randomUUID());

        // Create the EligibiliteService
        EligibiliteServiceDTO eligibiliteServiceDTO = eligibiliteServiceMapper.toDto(eligibiliteService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEligibiliteServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(eligibiliteServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EligibiliteService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEligibiliteService() throws Exception {
        // Initialize the database
        insertedEligibiliteService = eligibiliteServiceRepository.saveAndFlush(eligibiliteService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the eligibiliteService
        restEligibiliteServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, eligibiliteService.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eligibiliteServiceRepository.count();
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

    protected EligibiliteService getPersistedEligibiliteService(EligibiliteService eligibiliteService) {
        return eligibiliteServiceRepository.findById(eligibiliteService.getId()).orElseThrow();
    }

    protected void assertPersistedEligibiliteServiceToMatchAllProperties(EligibiliteService expectedEligibiliteService) {
        assertEligibiliteServiceAllPropertiesEquals(expectedEligibiliteService, getPersistedEligibiliteService(expectedEligibiliteService));
    }

    protected void assertPersistedEligibiliteServiceToMatchUpdatableProperties(EligibiliteService expectedEligibiliteService) {
        assertEligibiliteServiceAllUpdatablePropertiesEquals(
            expectedEligibiliteService,
            getPersistedEligibiliteService(expectedEligibiliteService)
        );
    }
}
