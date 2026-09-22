package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.CodeQrServiceAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.IntegrationTest;
import sn.orange.conciergerie.domain.CodeQrService;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.enumeration.StatutCodeQr;
import sn.orange.conciergerie.repository.CodeQrServiceRepository;
import sn.orange.conciergerie.service.dto.CodeQrServiceDTO;
import sn.orange.conciergerie.service.mapper.CodeQrServiceMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CodeQrServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodeQrServiceResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_QR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_GENERATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_GENERATION = Instant.ofEpochMilli(1701862474321L);

    private static final Instant DEFAULT_DATE_EXPIRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EXPIRATION = Instant.ofEpochMilli(1701862474321L);

    private static final StatutCodeQr DEFAULT_STATUT = StatutCodeQr.VALIDE;
    private static final StatutCodeQr UPDATED_STATUT = StatutCodeQr.UTILISE;

    private static final String ENTITY_API_URL = "/api/code-qr-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CodeQrServiceRepository codeQrServiceRepository;

    @Autowired
    private CodeQrServiceMapper codeQrServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodeQrServiceMockMvc;

    private CodeQrService codeQrService;

    private CodeQrService insertedCodeQrService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeQrService createEntity(EntityManager em) {
        CodeQrService codeQrService = new CodeQrService()
            .code(DEFAULT_CODE)
            .qrCode(DEFAULT_QR_CODE)
            .dateGeneration(DEFAULT_DATE_GENERATION)
            .dateExpiration(DEFAULT_DATE_EXPIRATION)
            .statut(DEFAULT_STATUT);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        codeQrService.setDemande(demande);
        return codeQrService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodeQrService createUpdatedEntity(EntityManager em) {
        CodeQrService updatedCodeQrService = new CodeQrService()
            .code(UPDATED_CODE)
            .qrCode(UPDATED_QR_CODE)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .statut(UPDATED_STATUT);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createUpdatedEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        updatedCodeQrService.setDemande(demande);
        return updatedCodeQrService;
    }

    @BeforeEach
    void initTest() {
        codeQrService = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCodeQrService != null) {
            codeQrServiceRepository.delete(insertedCodeQrService);
            insertedCodeQrService = null;
        }
    }

    @Test
    @Transactional
    void createCodeQrService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CodeQrService
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);
        var returnedCodeQrServiceDTO = om.readValue(
            restCodeQrServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeQrServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CodeQrServiceDTO.class
        );

        // Validate the CodeQrService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCodeQrService = codeQrServiceMapper.toEntity(returnedCodeQrServiceDTO);
        assertCodeQrServiceUpdatableFieldsEquals(returnedCodeQrService, getPersistedCodeQrService(returnedCodeQrService));

        insertedCodeQrService = returnedCodeQrService;
    }

    @Test
    @Transactional
    void createCodeQrServiceWithExistingId() throws Exception {
        // Create the CodeQrService with an existing ID
        insertedCodeQrService = codeQrServiceRepository.saveAndFlush(codeQrService);
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodeQrServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeQrServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codeQrService.setCode(null);

        // Create the CodeQrService, which fails.
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        restCodeQrServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeQrServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateGenerationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codeQrService.setDateGeneration(null);

        // Create the CodeQrService, which fails.
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        restCodeQrServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeQrServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codeQrService.setStatut(null);

        // Create the CodeQrService, which fails.
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        restCodeQrServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeQrServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCodeQrServices() throws Exception {
        // Initialize the database
        insertedCodeQrService = codeQrServiceRepository.saveAndFlush(codeQrService);

        // Get all the codeQrServiceList
        restCodeQrServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codeQrService.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE)))
            .andExpect(jsonPath("$.[*].dateGeneration").value(hasItem(DEFAULT_DATE_GENERATION.toString())))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    void getCodeQrService() throws Exception {
        // Initialize the database
        insertedCodeQrService = codeQrServiceRepository.saveAndFlush(codeQrService);

        // Get the codeQrService
        restCodeQrServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, codeQrService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codeQrService.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE))
            .andExpect(jsonPath("$.dateGeneration").value(DEFAULT_DATE_GENERATION.toString()))
            .andExpect(jsonPath("$.dateExpiration").value(DEFAULT_DATE_EXPIRATION.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCodeQrService() throws Exception {
        // Get the codeQrService
        restCodeQrServiceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCodeQrService() throws Exception {
        // Initialize the database
        insertedCodeQrService = codeQrServiceRepository.saveAndFlush(codeQrService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeQrService
        CodeQrService updatedCodeQrService = codeQrServiceRepository.findById(codeQrService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCodeQrService are not directly saved in db
        em.detach(updatedCodeQrService);
        updatedCodeQrService
            .code(UPDATED_CODE)
            .qrCode(UPDATED_QR_CODE)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .statut(UPDATED_STATUT);
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(updatedCodeQrService);

        restCodeQrServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeQrServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codeQrServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCodeQrServiceToMatchAllProperties(updatedCodeQrService);
    }

    @Test
    @Transactional
    void putNonExistingCodeQrService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeQrService.setId(UUID.randomUUID());

        // Create the CodeQrService
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeQrServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codeQrServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codeQrServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodeQrService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeQrService.setId(UUID.randomUUID());

        // Create the CodeQrService
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeQrServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codeQrServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodeQrService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeQrService.setId(UUID.randomUUID());

        // Create the CodeQrService
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeQrServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codeQrServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodeQrServiceWithPatch() throws Exception {
        // Initialize the database
        insertedCodeQrService = codeQrServiceRepository.saveAndFlush(codeQrService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeQrService using partial update
        CodeQrService partialUpdatedCodeQrService = new CodeQrService();
        partialUpdatedCodeQrService.setId(codeQrService.getId());

        partialUpdatedCodeQrService.dateGeneration(UPDATED_DATE_GENERATION).dateExpiration(UPDATED_DATE_EXPIRATION).statut(UPDATED_STATUT);

        restCodeQrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeQrService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodeQrService))
            )
            .andExpect(status().isOk());

        // Validate the CodeQrService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodeQrServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCodeQrService, codeQrService),
            getPersistedCodeQrService(codeQrService)
        );
    }

    @Test
    @Transactional
    void fullUpdateCodeQrServiceWithPatch() throws Exception {
        // Initialize the database
        insertedCodeQrService = codeQrServiceRepository.saveAndFlush(codeQrService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codeQrService using partial update
        CodeQrService partialUpdatedCodeQrService = new CodeQrService();
        partialUpdatedCodeQrService.setId(codeQrService.getId());

        partialUpdatedCodeQrService
            .code(UPDATED_CODE)
            .qrCode(UPDATED_QR_CODE)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .statut(UPDATED_STATUT);

        restCodeQrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodeQrService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodeQrService))
            )
            .andExpect(status().isOk());

        // Validate the CodeQrService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodeQrServiceUpdatableFieldsEquals(partialUpdatedCodeQrService, getPersistedCodeQrService(partialUpdatedCodeQrService));
    }

    @Test
    @Transactional
    void patchNonExistingCodeQrService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeQrService.setId(UUID.randomUUID());

        // Create the CodeQrService
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodeQrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codeQrServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codeQrServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodeQrService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeQrService.setId(UUID.randomUUID());

        // Create the CodeQrService
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeQrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codeQrServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodeQrService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codeQrService.setId(UUID.randomUUID());

        // Create the CodeQrService
        CodeQrServiceDTO codeQrServiceDTO = codeQrServiceMapper.toDto(codeQrService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodeQrServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(codeQrServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodeQrService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodeQrService() throws Exception {
        // Initialize the database
        insertedCodeQrService = codeQrServiceRepository.saveAndFlush(codeQrService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the codeQrService
        restCodeQrServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, codeQrService.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return codeQrServiceRepository.count();
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

    protected CodeQrService getPersistedCodeQrService(CodeQrService codeQrService) {
        return codeQrServiceRepository.findById(codeQrService.getId()).orElseThrow();
    }

    protected void assertPersistedCodeQrServiceToMatchAllProperties(CodeQrService expectedCodeQrService) {
        assertCodeQrServiceAllPropertiesEquals(expectedCodeQrService, getPersistedCodeQrService(expectedCodeQrService));
    }

    protected void assertPersistedCodeQrServiceToMatchUpdatableProperties(CodeQrService expectedCodeQrService) {
        assertCodeQrServiceAllUpdatablePropertiesEquals(expectedCodeQrService, getPersistedCodeQrService(expectedCodeQrService));
    }
}
