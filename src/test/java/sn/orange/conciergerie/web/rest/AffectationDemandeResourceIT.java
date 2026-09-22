package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.AffectationDemandeAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.Instant;
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
import sn.orange.conciergerie.domain.AffectationDemande;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.repository.AffectationDemandeRepository;
import sn.orange.conciergerie.service.AffectationDemandeService;
import sn.orange.conciergerie.service.dto.AffectationDemandeDTO;
import sn.orange.conciergerie.service.mapper.AffectationDemandeMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link AffectationDemandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AffectationDemandeResourceIT {

    private static final Instant DEFAULT_DATE_AFFECTATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_AFFECTATION = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/affectation-demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AffectationDemandeRepository affectationDemandeRepository;

    @Mock
    private AffectationDemandeRepository affectationDemandeRepositoryMock;

    @Autowired
    private AffectationDemandeMapper affectationDemandeMapper;

    @Mock
    private AffectationDemandeService affectationDemandeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffectationDemandeMockMvc;

    private AffectationDemande affectationDemande;

    private AffectationDemande insertedAffectationDemande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationDemande createEntity(EntityManager em) {
        AffectationDemande affectationDemande = new AffectationDemande().dateAffectation(DEFAULT_DATE_AFFECTATION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        affectationDemande.setDemande(demande);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        affectationDemande.setPartenaire(partenaire);
        return affectationDemande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationDemande createUpdatedEntity(EntityManager em) {
        AffectationDemande updatedAffectationDemande = new AffectationDemande().dateAffectation(UPDATED_DATE_AFFECTATION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createUpdatedEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        updatedAffectationDemande.setDemande(demande);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createUpdatedEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        updatedAffectationDemande.setPartenaire(partenaire);
        return updatedAffectationDemande;
    }

    @BeforeEach
    void initTest() {
        affectationDemande = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAffectationDemande != null) {
            affectationDemandeRepository.delete(insertedAffectationDemande);
            insertedAffectationDemande = null;
        }
    }

    @Test
    @Transactional
    void createAffectationDemande() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AffectationDemande
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);
        var returnedAffectationDemandeDTO = om.readValue(
            restAffectationDemandeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationDemandeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AffectationDemandeDTO.class
        );

        // Validate the AffectationDemande in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAffectationDemande = affectationDemandeMapper.toEntity(returnedAffectationDemandeDTO);
        assertAffectationDemandeUpdatableFieldsEquals(
            returnedAffectationDemande,
            getPersistedAffectationDemande(returnedAffectationDemande)
        );

        insertedAffectationDemande = returnedAffectationDemande;
    }

    @Test
    @Transactional
    void createAffectationDemandeWithExistingId() throws Exception {
        // Create the AffectationDemande with an existing ID
        insertedAffectationDemande = affectationDemandeRepository.saveAndFlush(affectationDemande);
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffectationDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationDemandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateAffectationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        affectationDemande.setDateAffectation(null);

        // Create the AffectationDemande, which fails.
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        restAffectationDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationDemandeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAffectationDemandes() throws Exception {
        // Initialize the database
        insertedAffectationDemande = affectationDemandeRepository.saveAndFlush(affectationDemande);

        // Get all the affectationDemandeList
        restAffectationDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectationDemande.getId().toString())))
            .andExpect(jsonPath("$.[*].dateAffectation").value(hasItem(DEFAULT_DATE_AFFECTATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAffectationDemandesWithEagerRelationshipsIsEnabled() throws Exception {
        when(affectationDemandeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restAffectationDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(affectationDemandeServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAffectationDemandesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(affectationDemandeServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restAffectationDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(affectationDemandeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAffectationDemande() throws Exception {
        // Initialize the database
        insertedAffectationDemande = affectationDemandeRepository.saveAndFlush(affectationDemande);

        // Get the affectationDemande
        restAffectationDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, affectationDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affectationDemande.getId().toString()))
            .andExpect(jsonPath("$.dateAffectation").value(DEFAULT_DATE_AFFECTATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAffectationDemande() throws Exception {
        // Get the affectationDemande
        restAffectationDemandeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAffectationDemande() throws Exception {
        // Initialize the database
        insertedAffectationDemande = affectationDemandeRepository.saveAndFlush(affectationDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationDemande
        AffectationDemande updatedAffectationDemande = affectationDemandeRepository.findById(affectationDemande.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAffectationDemande are not directly saved in db
        em.detach(updatedAffectationDemande);
        updatedAffectationDemande.dateAffectation(UPDATED_DATE_AFFECTATION);
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(updatedAffectationDemande);

        restAffectationDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationDemandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAffectationDemandeToMatchAllProperties(updatedAffectationDemande);
    }

    @Test
    @Transactional
    void putNonExistingAffectationDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationDemande.setId(UUID.randomUUID());

        // Create the AffectationDemande
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffectationDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationDemande.setId(UUID.randomUUID());

        // Create the AffectationDemande
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffectationDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationDemande.setId(UUID.randomUUID());

        // Create the AffectationDemande
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationDemandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffectationDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedAffectationDemande = affectationDemandeRepository.saveAndFlush(affectationDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationDemande using partial update
        AffectationDemande partialUpdatedAffectationDemande = new AffectationDemande();
        partialUpdatedAffectationDemande.setId(affectationDemande.getId());

        restAffectationDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAffectationDemande))
            )
            .andExpect(status().isOk());

        // Validate the AffectationDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAffectationDemandeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAffectationDemande, affectationDemande),
            getPersistedAffectationDemande(affectationDemande)
        );
    }

    @Test
    @Transactional
    void fullUpdateAffectationDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedAffectationDemande = affectationDemandeRepository.saveAndFlush(affectationDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationDemande using partial update
        AffectationDemande partialUpdatedAffectationDemande = new AffectationDemande();
        partialUpdatedAffectationDemande.setId(affectationDemande.getId());

        partialUpdatedAffectationDemande.dateAffectation(UPDATED_DATE_AFFECTATION);

        restAffectationDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAffectationDemande))
            )
            .andExpect(status().isOk());

        // Validate the AffectationDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAffectationDemandeUpdatableFieldsEquals(
            partialUpdatedAffectationDemande,
            getPersistedAffectationDemande(partialUpdatedAffectationDemande)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAffectationDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationDemande.setId(UUID.randomUUID());

        // Create the AffectationDemande
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affectationDemandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(affectationDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffectationDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationDemande.setId(UUID.randomUUID());

        // Create the AffectationDemande
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(affectationDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffectationDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationDemande.setId(UUID.randomUUID());

        // Create the AffectationDemande
        AffectationDemandeDTO affectationDemandeDTO = affectationDemandeMapper.toDto(affectationDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationDemandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(affectationDemandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffectationDemande() throws Exception {
        // Initialize the database
        insertedAffectationDemande = affectationDemandeRepository.saveAndFlush(affectationDemande);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the affectationDemande
        restAffectationDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, affectationDemande.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return affectationDemandeRepository.count();
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

    protected AffectationDemande getPersistedAffectationDemande(AffectationDemande affectationDemande) {
        return affectationDemandeRepository.findById(affectationDemande.getId()).orElseThrow();
    }

    protected void assertPersistedAffectationDemandeToMatchAllProperties(AffectationDemande expectedAffectationDemande) {
        assertAffectationDemandeAllPropertiesEquals(expectedAffectationDemande, getPersistedAffectationDemande(expectedAffectationDemande));
    }

    protected void assertPersistedAffectationDemandeToMatchUpdatableProperties(AffectationDemande expectedAffectationDemande) {
        assertAffectationDemandeAllUpdatablePropertiesEquals(
            expectedAffectationDemande,
            getPersistedAffectationDemande(expectedAffectationDemande)
        );
    }
}
