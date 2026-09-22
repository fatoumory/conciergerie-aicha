package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.TypeDemandeAsserts.*;
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
import sn.orange.conciergerie.domain.TypeDemande;
import sn.orange.conciergerie.repository.TypeDemandeRepository;
import sn.orange.conciergerie.service.dto.TypeDemandeDTO;
import sn.orange.conciergerie.service.mapper.TypeDemandeMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TypeDemandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeDemandeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeDemandeRepository typeDemandeRepository;

    @Autowired
    private TypeDemandeMapper typeDemandeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeDemandeMockMvc;

    private TypeDemande typeDemande;

    private TypeDemande insertedTypeDemande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDemande createEntity() {
        return new TypeDemande().code(DEFAULT_CODE).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDemande createUpdatedEntity() {
        return new TypeDemande().code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        typeDemande = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTypeDemande != null) {
            typeDemandeRepository.delete(insertedTypeDemande);
            insertedTypeDemande = null;
        }
    }

    @Test
    @Transactional
    void createTypeDemande() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeDemande
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);
        var returnedTypeDemandeDTO = om.readValue(
            restTypeDemandeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDemandeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeDemandeDTO.class
        );

        // Validate the TypeDemande in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeDemande = typeDemandeMapper.toEntity(returnedTypeDemandeDTO);
        assertTypeDemandeUpdatableFieldsEquals(returnedTypeDemande, getPersistedTypeDemande(returnedTypeDemande));

        insertedTypeDemande = returnedTypeDemande;
    }

    @Test
    @Transactional
    void createTypeDemandeWithExistingId() throws Exception {
        // Create the TypeDemande with an existing ID
        insertedTypeDemande = typeDemandeRepository.saveAndFlush(typeDemande);
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDemandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeDemande.setCode(null);

        // Create the TypeDemande, which fails.
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        restTypeDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDemandeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeDemande.setLibelle(null);

        // Create the TypeDemande, which fails.
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        restTypeDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDemandeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeDemandes() throws Exception {
        // Initialize the database
        insertedTypeDemande = typeDemandeRepository.saveAndFlush(typeDemande);

        // Get all the typeDemandeList
        restTypeDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDemande.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeDemande() throws Exception {
        // Initialize the database
        insertedTypeDemande = typeDemandeRepository.saveAndFlush(typeDemande);

        // Get the typeDemande
        restTypeDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, typeDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeDemande.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeDemande() throws Exception {
        // Get the typeDemande
        restTypeDemandeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeDemande() throws Exception {
        // Initialize the database
        insertedTypeDemande = typeDemandeRepository.saveAndFlush(typeDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDemande
        TypeDemande updatedTypeDemande = typeDemandeRepository.findById(typeDemande.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeDemande are not directly saved in db
        em.detach(updatedTypeDemande);
        updatedTypeDemande.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(updatedTypeDemande);

        restTypeDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDemandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeDemandeToMatchAllProperties(updatedTypeDemande);
    }

    @Test
    @Transactional
    void putNonExistingTypeDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDemande.setId(UUID.randomUUID());

        // Create the TypeDemande
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDemande.setId(UUID.randomUUID());

        // Create the TypeDemande
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDemande.setId(UUID.randomUUID());

        // Create the TypeDemande
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeDemandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedTypeDemande = typeDemandeRepository.saveAndFlush(typeDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDemande using partial update
        TypeDemande partialUpdatedTypeDemande = new TypeDemande();
        partialUpdatedTypeDemande.setId(typeDemande.getId());

        partialUpdatedTypeDemande.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restTypeDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeDemande))
            )
            .andExpect(status().isOk());

        // Validate the TypeDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeDemandeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeDemande, typeDemande),
            getPersistedTypeDemande(typeDemande)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedTypeDemande = typeDemandeRepository.saveAndFlush(typeDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeDemande using partial update
        TypeDemande partialUpdatedTypeDemande = new TypeDemande();
        partialUpdatedTypeDemande.setId(typeDemande.getId());

        partialUpdatedTypeDemande.code(UPDATED_CODE).libelle(UPDATED_LIBELLE);

        restTypeDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeDemande))
            )
            .andExpect(status().isOk());

        // Validate the TypeDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeDemandeUpdatableFieldsEquals(partialUpdatedTypeDemande, getPersistedTypeDemande(partialUpdatedTypeDemande));
    }

    @Test
    @Transactional
    void patchNonExistingTypeDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDemande.setId(UUID.randomUUID());

        // Create the TypeDemande
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeDemandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDemande.setId(UUID.randomUUID());

        // Create the TypeDemande
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeDemande.setId(UUID.randomUUID());

        // Create the TypeDemande
        TypeDemandeDTO typeDemandeDTO = typeDemandeMapper.toDto(typeDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDemandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeDemandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeDemande() throws Exception {
        // Initialize the database
        insertedTypeDemande = typeDemandeRepository.saveAndFlush(typeDemande);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeDemande
        restTypeDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeDemande.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeDemandeRepository.count();
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

    protected TypeDemande getPersistedTypeDemande(TypeDemande typeDemande) {
        return typeDemandeRepository.findById(typeDemande.getId()).orElseThrow();
    }

    protected void assertPersistedTypeDemandeToMatchAllProperties(TypeDemande expectedTypeDemande) {
        assertTypeDemandeAllPropertiesEquals(expectedTypeDemande, getPersistedTypeDemande(expectedTypeDemande));
    }

    protected void assertPersistedTypeDemandeToMatchUpdatableProperties(TypeDemande expectedTypeDemande) {
        assertTypeDemandeAllUpdatablePropertiesEquals(expectedTypeDemande, getPersistedTypeDemande(expectedTypeDemande));
    }
}
