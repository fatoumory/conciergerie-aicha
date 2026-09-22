package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.StatutDemandeAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
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
import sn.orange.conciergerie.domain.StatutDemande;
import sn.orange.conciergerie.repository.StatutDemandeRepository;
import sn.orange.conciergerie.service.dto.StatutDemandeDTO;
import sn.orange.conciergerie.service.mapper.StatutDemandeMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link StatutDemandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatutDemandeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/statut-demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatutDemandeRepository statutDemandeRepository;

    @Autowired
    private StatutDemandeMapper statutDemandeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatutDemandeMockMvc;

    private StatutDemande statutDemande;

    private StatutDemande insertedStatutDemande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatutDemande createEntity() {
        return new StatutDemande().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatutDemande createUpdatedEntity() {
        return new StatutDemande().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        statutDemande = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStatutDemande != null) {
            statutDemandeRepository.delete(insertedStatutDemande);
            insertedStatutDemande = null;
        }
    }

    @Test
    @Transactional
    void createStatutDemande() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StatutDemande
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);
        var returnedStatutDemandeDTO = om.readValue(
            restStatutDemandeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statutDemandeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StatutDemandeDTO.class
        );

        // Validate the StatutDemande in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStatutDemande = statutDemandeMapper.toEntity(returnedStatutDemandeDTO);
        assertStatutDemandeUpdatableFieldsEquals(returnedStatutDemande, getPersistedStatutDemande(returnedStatutDemande));

        insertedStatutDemande = returnedStatutDemande;
    }

    @Test
    @Transactional
    void createStatutDemandeWithExistingId() throws Exception {
        // Create the StatutDemande with an existing ID
        insertedStatutDemande = statutDemandeRepository.saveAndFlush(statutDemande);
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatutDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statutDemandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        statutDemande.setCode(null);

        // Create the StatutDemande, which fails.
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        restStatutDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statutDemandeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        statutDemande.setLibelle(null);

        // Create the StatutDemande, which fails.
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        restStatutDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statutDemandeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStatutDemandes() throws Exception {
        // Initialize the database
        insertedStatutDemande = statutDemandeRepository.saveAndFlush(statutDemande);

        // Get all the statutDemandeList
        restStatutDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statutDemande.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getStatutDemande() throws Exception {
        // Initialize the database
        insertedStatutDemande = statutDemandeRepository.saveAndFlush(statutDemande);

        // Get the statutDemande
        restStatutDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, statutDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statutDemande.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingStatutDemande() throws Exception {
        // Get the statutDemande
        restStatutDemandeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStatutDemande() throws Exception {
        // Initialize the database
        insertedStatutDemande = statutDemandeRepository.saveAndFlush(statutDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statutDemande
        StatutDemande updatedStatutDemande = statutDemandeRepository.findById(statutDemande.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStatutDemande are not directly saved in db
        em.detach(updatedStatutDemande);
        updatedStatutDemande.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(updatedStatutDemande);

        restStatutDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statutDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statutDemandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStatutDemandeToMatchAllProperties(updatedStatutDemande);
    }

    @Test
    @Transactional
    void putNonExistingStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statutDemande.setId(UUID.randomUUID());

        // Create the StatutDemande
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatutDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statutDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statutDemande.setId(UUID.randomUUID());

        // Create the StatutDemande
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatutDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statutDemande.setId(UUID.randomUUID());

        // Create the StatutDemande
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatutDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statutDemandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatutDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedStatutDemande = statutDemandeRepository.saveAndFlush(statutDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statutDemande using partial update
        StatutDemande partialUpdatedStatutDemande = new StatutDemande();
        partialUpdatedStatutDemande.setId(statutDemande.getId());

        restStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatutDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatutDemande))
            )
            .andExpect(status().isOk());

        // Validate the StatutDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatutDemandeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStatutDemande, statutDemande),
            getPersistedStatutDemande(statutDemande)
        );
    }

    @Test
    @Transactional
    void fullUpdateStatutDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedStatutDemande = statutDemandeRepository.saveAndFlush(statutDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statutDemande using partial update
        StatutDemande partialUpdatedStatutDemande = new StatutDemande();
        partialUpdatedStatutDemande.setId(statutDemande.getId());

        partialUpdatedStatutDemande.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatutDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatutDemande))
            )
            .andExpect(status().isOk());

        // Validate the StatutDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatutDemandeUpdatableFieldsEquals(partialUpdatedStatutDemande, getPersistedStatutDemande(partialUpdatedStatutDemande));
    }

    @Test
    @Transactional
    void patchNonExistingStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statutDemande.setId(UUID.randomUUID());

        // Create the StatutDemande
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statutDemandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statutDemande.setId(UUID.randomUUID());

        // Create the StatutDemande
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statutDemande.setId(UUID.randomUUID());

        // Create the StatutDemande
        StatutDemandeDTO statutDemandeDTO = statutDemandeMapper.toDto(statutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatutDemandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(statutDemandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatutDemande() throws Exception {
        // Initialize the database
        insertedStatutDemande = statutDemandeRepository.saveAndFlush(statutDemande);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the statutDemande
        restStatutDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, statutDemande.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return statutDemandeRepository.count();
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

    protected StatutDemande getPersistedStatutDemande(StatutDemande statutDemande) {
        return statutDemandeRepository.findById(statutDemande.getId()).orElseThrow();
    }

    protected void assertPersistedStatutDemandeToMatchAllProperties(StatutDemande expectedStatutDemande) {
        assertStatutDemandeAllPropertiesEquals(expectedStatutDemande, getPersistedStatutDemande(expectedStatutDemande));
    }

    protected void assertPersistedStatutDemandeToMatchUpdatableProperties(StatutDemande expectedStatutDemande) {
        assertStatutDemandeAllUpdatablePropertiesEquals(expectedStatutDemande, getPersistedStatutDemande(expectedStatutDemande));
    }
}
