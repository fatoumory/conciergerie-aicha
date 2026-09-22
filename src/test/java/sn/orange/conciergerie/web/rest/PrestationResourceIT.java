package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.PrestationAsserts.*;
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
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.Prestation;
import sn.orange.conciergerie.repository.PrestationRepository;
import sn.orange.conciergerie.service.PrestationService;
import sn.orange.conciergerie.service.dto.PrestationDTO;
import sn.orange.conciergerie.service.mapper.PrestationMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PrestationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrestationResourceIT {

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.ofEpochMilli(1701862474321L);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/prestations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PrestationRepository prestationRepository;

    @Mock
    private PrestationRepository prestationRepositoryMock;

    @Autowired
    private PrestationMapper prestationMapper;

    @Mock
    private PrestationService prestationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestationMockMvc;

    private Prestation prestation;

    private Prestation insertedPrestation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestation createEntity(EntityManager em) {
        Prestation prestation = new Prestation().dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        prestation.setDemande(demande);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        prestation.setPartenaire(partenaire);
        return prestation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestation createUpdatedEntity(EntityManager em) {
        Prestation updatedPrestation = new Prestation().dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createUpdatedEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        updatedPrestation.setDemande(demande);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createUpdatedEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        updatedPrestation.setPartenaire(partenaire);
        return updatedPrestation;
    }

    @BeforeEach
    void initTest() {
        prestation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPrestation != null) {
            prestationRepository.delete(insertedPrestation);
            insertedPrestation = null;
        }
    }

    @Test
    @Transactional
    void createPrestation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);
        var returnedPrestationDTO = om.readValue(
            restPrestationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PrestationDTO.class
        );

        // Validate the Prestation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPrestation = prestationMapper.toEntity(returnedPrestationDTO);
        assertPrestationUpdatableFieldsEquals(returnedPrestation, getPersistedPrestation(returnedPrestation));

        insertedPrestation = returnedPrestation;
    }

    @Test
    @Transactional
    void createPrestationWithExistingId() throws Exception {
        // Create the Prestation with an existing ID
        insertedPrestation = prestationRepository.saveAndFlush(prestation);
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrestations() throws Exception {
        // Initialize the database
        insertedPrestation = prestationRepository.saveAndFlush(prestation);

        // Get all the prestationList
        restPrestationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestation.getId().toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prestationServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPrestationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prestationServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrestationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prestationServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPrestationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(prestationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPrestation() throws Exception {
        // Initialize the database
        insertedPrestation = prestationRepository.saveAndFlush(prestation);

        // Get the prestation
        restPrestationMockMvc
            .perform(get(ENTITY_API_URL_ID, prestation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestation.getId().toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPrestation() throws Exception {
        // Get the prestation
        restPrestationMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPrestation() throws Exception {
        // Initialize the database
        insertedPrestation = prestationRepository.saveAndFlush(prestation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestation
        Prestation updatedPrestation = prestationRepository.findById(prestation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPrestation are not directly saved in db
        em.detach(updatedPrestation);
        updatedPrestation.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        PrestationDTO prestationDTO = prestationMapper.toDto(updatedPrestation);

        restPrestationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPrestationToMatchAllProperties(updatedPrestation);
    }

    @Test
    @Transactional
    void putNonExistingPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestation.setId(UUID.randomUUID());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestation.setId(UUID.randomUUID());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestation.setId(UUID.randomUUID());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestationWithPatch() throws Exception {
        // Initialize the database
        insertedPrestation = prestationRepository.saveAndFlush(prestation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestation using partial update
        Prestation partialUpdatedPrestation = new Prestation();
        partialUpdatedPrestation.setId(prestation.getId());

        partialUpdatedPrestation.dateDebut(UPDATED_DATE_DEBUT);

        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestation))
            )
            .andExpect(status().isOk());

        // Validate the Prestation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPrestation, prestation),
            getPersistedPrestation(prestation)
        );
    }

    @Test
    @Transactional
    void fullUpdatePrestationWithPatch() throws Exception {
        // Initialize the database
        insertedPrestation = prestationRepository.saveAndFlush(prestation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prestation using partial update
        Prestation partialUpdatedPrestation = new Prestation();
        partialUpdatedPrestation.setId(prestation.getId());

        partialUpdatedPrestation.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPrestation))
            )
            .andExpect(status().isOk());

        // Validate the Prestation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPrestationUpdatableFieldsEquals(partialUpdatedPrestation, getPersistedPrestation(partialUpdatedPrestation));
    }

    @Test
    @Transactional
    void patchNonExistingPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestation.setId(UUID.randomUUID());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestation.setId(UUID.randomUUID());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prestationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prestation.setId(UUID.randomUUID());

        // Create the Prestation
        PrestationDTO prestationDTO = prestationMapper.toDto(prestation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prestationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestation() throws Exception {
        // Initialize the database
        insertedPrestation = prestationRepository.saveAndFlush(prestation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prestation
        restPrestationMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestation.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prestationRepository.count();
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

    protected Prestation getPersistedPrestation(Prestation prestation) {
        return prestationRepository.findById(prestation.getId()).orElseThrow();
    }

    protected void assertPersistedPrestationToMatchAllProperties(Prestation expectedPrestation) {
        assertPrestationAllPropertiesEquals(expectedPrestation, getPersistedPrestation(expectedPrestation));
    }

    protected void assertPersistedPrestationToMatchUpdatableProperties(Prestation expectedPrestation) {
        assertPrestationAllUpdatablePropertiesEquals(expectedPrestation, getPersistedPrestation(expectedPrestation));
    }
}
