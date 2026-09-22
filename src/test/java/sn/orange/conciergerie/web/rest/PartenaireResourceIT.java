package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.PartenaireAsserts.*;
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
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.repository.PartenaireRepository;
import sn.orange.conciergerie.service.dto.PartenaireDTO;
import sn.orange.conciergerie.service.mapper.PartenaireMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PartenaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartenaireResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/partenaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartenaireRepository partenaireRepository;

    @Autowired
    private PartenaireMapper partenaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartenaireMockMvc;

    private Partenaire partenaire;

    private Partenaire insertedPartenaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaire createEntity() {
        return new Partenaire().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaire createUpdatedEntity() {
        return new Partenaire().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        partenaire = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPartenaire != null) {
            partenaireRepository.delete(insertedPartenaire);
            insertedPartenaire = null;
        }
    }

    @Test
    @Transactional
    void createPartenaire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);
        var returnedPartenaireDTO = om.readValue(
            restPartenaireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartenaireDTO.class
        );

        // Validate the Partenaire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPartenaire = partenaireMapper.toEntity(returnedPartenaireDTO);
        assertPartenaireUpdatableFieldsEquals(returnedPartenaire, getPersistedPartenaire(returnedPartenaire));

        insertedPartenaire = returnedPartenaire;
    }

    @Test
    @Transactional
    void createPartenaireWithExistingId() throws Exception {
        // Create the Partenaire with an existing ID
        insertedPartenaire = partenaireRepository.saveAndFlush(partenaire);
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partenaire.setCode(null);

        // Create the Partenaire, which fails.
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partenaire.setLibelle(null);

        // Create the Partenaire, which fails.
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        restPartenaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartenaires() throws Exception {
        // Initialize the database
        insertedPartenaire = partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getPartenaire() throws Exception {
        // Initialize the database
        insertedPartenaire = partenaireRepository.saveAndFlush(partenaire);

        // Get the partenaire
        restPartenaireMockMvc
            .perform(get(ENTITY_API_URL_ID, partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partenaire.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingPartenaire() throws Exception {
        // Get the partenaire
        restPartenaireMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartenaire() throws Exception {
        // Initialize the database
        insertedPartenaire = partenaireRepository.saveAndFlush(partenaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partenaire
        Partenaire updatedPartenaire = partenaireRepository.findById(partenaire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartenaire are not directly saved in db
        em.detach(updatedPartenaire);
        updatedPartenaire.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(updatedPartenaire);

        restPartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partenaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partenaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartenaireToMatchAllProperties(updatedPartenaire);
    }

    @Test
    @Transactional
    void putNonExistingPartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaire.setId(UUID.randomUUID());

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partenaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaire.setId(UUID.randomUUID());

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaire.setId(UUID.randomUUID());

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartenaireWithPatch() throws Exception {
        // Initialize the database
        insertedPartenaire = partenaireRepository.saveAndFlush(partenaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partenaire using partial update
        Partenaire partialUpdatedPartenaire = new Partenaire();
        partialUpdatedPartenaire.setId(partenaire.getId());

        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartenaire))
            )
            .andExpect(status().isOk());

        // Validate the Partenaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartenaireUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartenaire, partenaire),
            getPersistedPartenaire(partenaire)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartenaireWithPatch() throws Exception {
        // Initialize the database
        insertedPartenaire = partenaireRepository.saveAndFlush(partenaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partenaire using partial update
        Partenaire partialUpdatedPartenaire = new Partenaire();
        partialUpdatedPartenaire.setId(partenaire.getId());

        partialUpdatedPartenaire.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartenaire))
            )
            .andExpect(status().isOk());

        // Validate the Partenaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartenaireUpdatableFieldsEquals(partialUpdatedPartenaire, getPersistedPartenaire(partialUpdatedPartenaire));
    }

    @Test
    @Transactional
    void patchNonExistingPartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaire.setId(UUID.randomUUID());

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partenaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaire.setId(UUID.randomUUID());

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partenaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartenaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaire.setId(UUID.randomUUID());

        // Create the Partenaire
        PartenaireDTO partenaireDTO = partenaireMapper.toDto(partenaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partenaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partenaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartenaire() throws Exception {
        // Initialize the database
        insertedPartenaire = partenaireRepository.saveAndFlush(partenaire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partenaire
        restPartenaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, partenaire.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partenaireRepository.count();
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

    protected Partenaire getPersistedPartenaire(Partenaire partenaire) {
        return partenaireRepository.findById(partenaire.getId()).orElseThrow();
    }

    protected void assertPersistedPartenaireToMatchAllProperties(Partenaire expectedPartenaire) {
        assertPartenaireAllPropertiesEquals(expectedPartenaire, getPersistedPartenaire(expectedPartenaire));
    }

    protected void assertPersistedPartenaireToMatchUpdatableProperties(Partenaire expectedPartenaire) {
        assertPartenaireAllUpdatablePropertiesEquals(expectedPartenaire, getPersistedPartenaire(expectedPartenaire));
    }
}
