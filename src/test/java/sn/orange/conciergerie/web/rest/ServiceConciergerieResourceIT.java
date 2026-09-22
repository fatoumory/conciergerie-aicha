package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.ServiceConciergerieAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
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
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.domain.TypeService;
import sn.orange.conciergerie.repository.ServiceConciergerieRepository;
import sn.orange.conciergerie.service.ServiceConciergerieService;
import sn.orange.conciergerie.service.dto.ServiceConciergerieDTO;
import sn.orange.conciergerie.service.mapper.ServiceConciergerieMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ServiceConciergerieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceConciergerieResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-conciergeries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceConciergerieRepository serviceConciergerieRepository;

    @Mock
    private ServiceConciergerieRepository serviceConciergerieRepositoryMock;

    @Autowired
    private ServiceConciergerieMapper serviceConciergerieMapper;

    @Mock
    private ServiceConciergerieService serviceConciergerieServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceConciergerieMockMvc;

    private ServiceConciergerie serviceConciergerie;

    private ServiceConciergerie insertedServiceConciergerie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceConciergerie createEntity(EntityManager em) {
        ServiceConciergerie serviceConciergerie = new ServiceConciergerie()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        TypeService typeService;
        if (TestUtil.findAll(em, TypeService.class).isEmpty()) {
            typeService = TypeServiceResourceIT.createEntity();
            em.persist(typeService);
            em.flush();
        } else {
            typeService = TestUtil.findAll(em, TypeService.class).getFirst();
        }
        serviceConciergerie.setTypeService(typeService);
        return serviceConciergerie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceConciergerie createUpdatedEntity(EntityManager em) {
        ServiceConciergerie updatedServiceConciergerie = new ServiceConciergerie()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        TypeService typeService;
        if (TestUtil.findAll(em, TypeService.class).isEmpty()) {
            typeService = TypeServiceResourceIT.createUpdatedEntity();
            em.persist(typeService);
            em.flush();
        } else {
            typeService = TestUtil.findAll(em, TypeService.class).getFirst();
        }
        updatedServiceConciergerie.setTypeService(typeService);
        return updatedServiceConciergerie;
    }

    @BeforeEach
    void initTest() {
        serviceConciergerie = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceConciergerie != null) {
            serviceConciergerieRepository.delete(insertedServiceConciergerie);
            insertedServiceConciergerie = null;
        }
    }

    @Test
    @Transactional
    void createServiceConciergerie() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceConciergerie
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);
        var returnedServiceConciergerieDTO = om.readValue(
            restServiceConciergerieMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceConciergerieDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceConciergerieDTO.class
        );

        // Validate the ServiceConciergerie in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceConciergerie = serviceConciergerieMapper.toEntity(returnedServiceConciergerieDTO);
        assertServiceConciergerieUpdatableFieldsEquals(
            returnedServiceConciergerie,
            getPersistedServiceConciergerie(returnedServiceConciergerie)
        );

        insertedServiceConciergerie = returnedServiceConciergerie;
    }

    @Test
    @Transactional
    void createServiceConciergerieWithExistingId() throws Exception {
        // Create the ServiceConciergerie with an existing ID
        insertedServiceConciergerie = serviceConciergerieRepository.saveAndFlush(serviceConciergerie);
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceConciergerieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceConciergerieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceConciergerie.setCode(null);

        // Create the ServiceConciergerie, which fails.
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        restServiceConciergerieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceConciergerieDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceConciergerie.setLibelle(null);

        // Create the ServiceConciergerie, which fails.
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        restServiceConciergerieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceConciergerieDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceConciergeries() throws Exception {
        // Initialize the database
        insertedServiceConciergerie = serviceConciergerieRepository.saveAndFlush(serviceConciergerie);

        // Get all the serviceConciergerieList
        restServiceConciergerieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceConciergerie.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceConciergeriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceConciergerieServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restServiceConciergerieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceConciergerieServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceConciergeriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceConciergerieServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restServiceConciergerieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(serviceConciergerieRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServiceConciergerie() throws Exception {
        // Initialize the database
        insertedServiceConciergerie = serviceConciergerieRepository.saveAndFlush(serviceConciergerie);

        // Get the serviceConciergerie
        restServiceConciergerieMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceConciergerie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceConciergerie.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingServiceConciergerie() throws Exception {
        // Get the serviceConciergerie
        restServiceConciergerieMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceConciergerie() throws Exception {
        // Initialize the database
        insertedServiceConciergerie = serviceConciergerieRepository.saveAndFlush(serviceConciergerie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceConciergerie
        ServiceConciergerie updatedServiceConciergerie = serviceConciergerieRepository.findById(serviceConciergerie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceConciergerie are not directly saved in db
        em.detach(updatedServiceConciergerie);
        updatedServiceConciergerie.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(updatedServiceConciergerie);

        restServiceConciergerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceConciergerieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceConciergerieDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceConciergerieToMatchAllProperties(updatedServiceConciergerie);
    }

    @Test
    @Transactional
    void putNonExistingServiceConciergerie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceConciergerie.setId(UUID.randomUUID());

        // Create the ServiceConciergerie
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceConciergerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceConciergerieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceConciergerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceConciergerie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceConciergerie.setId(UUID.randomUUID());

        // Create the ServiceConciergerie
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceConciergerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceConciergerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceConciergerie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceConciergerie.setId(UUID.randomUUID());

        // Create the ServiceConciergerie
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceConciergerieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceConciergerieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceConciergerieWithPatch() throws Exception {
        // Initialize the database
        insertedServiceConciergerie = serviceConciergerieRepository.saveAndFlush(serviceConciergerie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceConciergerie using partial update
        ServiceConciergerie partialUpdatedServiceConciergerie = new ServiceConciergerie();
        partialUpdatedServiceConciergerie.setId(serviceConciergerie.getId());

        partialUpdatedServiceConciergerie.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

        restServiceConciergerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceConciergerie.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceConciergerie))
            )
            .andExpect(status().isOk());

        // Validate the ServiceConciergerie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceConciergerieUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceConciergerie, serviceConciergerie),
            getPersistedServiceConciergerie(serviceConciergerie)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceConciergerieWithPatch() throws Exception {
        // Initialize the database
        insertedServiceConciergerie = serviceConciergerieRepository.saveAndFlush(serviceConciergerie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceConciergerie using partial update
        ServiceConciergerie partialUpdatedServiceConciergerie = new ServiceConciergerie();
        partialUpdatedServiceConciergerie.setId(serviceConciergerie.getId());

        partialUpdatedServiceConciergerie.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

        restServiceConciergerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceConciergerie.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceConciergerie))
            )
            .andExpect(status().isOk());

        // Validate the ServiceConciergerie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceConciergerieUpdatableFieldsEquals(
            partialUpdatedServiceConciergerie,
            getPersistedServiceConciergerie(partialUpdatedServiceConciergerie)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceConciergerie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceConciergerie.setId(UUID.randomUUID());

        // Create the ServiceConciergerie
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceConciergerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceConciergerieDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceConciergerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceConciergerie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceConciergerie.setId(UUID.randomUUID());

        // Create the ServiceConciergerie
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceConciergerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceConciergerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceConciergerie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceConciergerie.setId(UUID.randomUUID());

        // Create the ServiceConciergerie
        ServiceConciergerieDTO serviceConciergerieDTO = serviceConciergerieMapper.toDto(serviceConciergerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceConciergerieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceConciergerieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceConciergerie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceConciergerie() throws Exception {
        // Initialize the database
        insertedServiceConciergerie = serviceConciergerieRepository.saveAndFlush(serviceConciergerie);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceConciergerie
        restServiceConciergerieMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceConciergerie.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceConciergerieRepository.count();
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

    protected ServiceConciergerie getPersistedServiceConciergerie(ServiceConciergerie serviceConciergerie) {
        return serviceConciergerieRepository.findById(serviceConciergerie.getId()).orElseThrow();
    }

    protected void assertPersistedServiceConciergerieToMatchAllProperties(ServiceConciergerie expectedServiceConciergerie) {
        assertServiceConciergerieAllPropertiesEquals(
            expectedServiceConciergerie,
            getPersistedServiceConciergerie(expectedServiceConciergerie)
        );
    }

    protected void assertPersistedServiceConciergerieToMatchUpdatableProperties(ServiceConciergerie expectedServiceConciergerie) {
        assertServiceConciergerieAllUpdatablePropertiesEquals(
            expectedServiceConciergerie,
            getPersistedServiceConciergerie(expectedServiceConciergerie)
        );
    }
}
