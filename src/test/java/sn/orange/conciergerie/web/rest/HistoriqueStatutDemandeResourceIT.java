package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.HistoriqueStatutDemandeAsserts.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.IntegrationTest;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.HistoriqueStatutDemande;
import sn.orange.conciergerie.domain.StatutDemande;
import sn.orange.conciergerie.repository.HistoriqueStatutDemandeRepository;
import sn.orange.conciergerie.service.HistoriqueStatutDemandeService;
import sn.orange.conciergerie.service.dto.HistoriqueStatutDemandeDTO;
import sn.orange.conciergerie.service.mapper.HistoriqueStatutDemandeMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link HistoriqueStatutDemandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueStatutDemandeResourceIT {

    private static final Instant DEFAULT_DATE_CHANGEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CHANGEMENT = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/historique-statut-demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriqueStatutDemandeRepository historiqueStatutDemandeRepository;

    @Mock
    private HistoriqueStatutDemandeRepository historiqueStatutDemandeRepositoryMock;

    @Autowired
    private HistoriqueStatutDemandeMapper historiqueStatutDemandeMapper;

    @Mock
    private HistoriqueStatutDemandeService historiqueStatutDemandeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueStatutDemandeMockMvc;

    private HistoriqueStatutDemande historiqueStatutDemande;

    private HistoriqueStatutDemande insertedHistoriqueStatutDemande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueStatutDemande createEntity(EntityManager em) {
        HistoriqueStatutDemande historiqueStatutDemande = new HistoriqueStatutDemande().dateChangement(DEFAULT_DATE_CHANGEMENT);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        historiqueStatutDemande.setDemande(demande);
        // Add required entity
        StatutDemande statutDemande;
        if (TestUtil.findAll(em, StatutDemande.class).isEmpty()) {
            statutDemande = StatutDemandeResourceIT.createEntity();
            em.persist(statutDemande);
            em.flush();
        } else {
            statutDemande = TestUtil.findAll(em, StatutDemande.class).getFirst();
        }
        historiqueStatutDemande.setStatut(statutDemande);
        return historiqueStatutDemande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueStatutDemande createUpdatedEntity(EntityManager em) {
        HistoriqueStatutDemande updatedHistoriqueStatutDemande = new HistoriqueStatutDemande().dateChangement(UPDATED_DATE_CHANGEMENT);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createUpdatedEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        updatedHistoriqueStatutDemande.setDemande(demande);
        // Add required entity
        StatutDemande statutDemande;
        if (TestUtil.findAll(em, StatutDemande.class).isEmpty()) {
            statutDemande = StatutDemandeResourceIT.createUpdatedEntity();
            em.persist(statutDemande);
            em.flush();
        } else {
            statutDemande = TestUtil.findAll(em, StatutDemande.class).getFirst();
        }
        updatedHistoriqueStatutDemande.setStatut(statutDemande);
        return updatedHistoriqueStatutDemande;
    }

    @BeforeEach
    void initTest() {
        historiqueStatutDemande = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriqueStatutDemande != null) {
            historiqueStatutDemandeRepository.delete(insertedHistoriqueStatutDemande);
            insertedHistoriqueStatutDemande = null;
        }
    }

    @Test
    @Transactional
    void createHistoriqueStatutDemande() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoriqueStatutDemande
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);
        var returnedHistoriqueStatutDemandeDTO = om.readValue(
            restHistoriqueStatutDemandeMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueStatutDemandeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriqueStatutDemandeDTO.class
        );

        // Validate the HistoriqueStatutDemande in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoriqueStatutDemande = historiqueStatutDemandeMapper.toEntity(returnedHistoriqueStatutDemandeDTO);
        assertHistoriqueStatutDemandeUpdatableFieldsEquals(
            returnedHistoriqueStatutDemande,
            getPersistedHistoriqueStatutDemande(returnedHistoriqueStatutDemande)
        );

        insertedHistoriqueStatutDemande = returnedHistoriqueStatutDemande;
    }

    @Test
    @Transactional
    void createHistoriqueStatutDemandeWithExistingId() throws Exception {
        // Create the HistoriqueStatutDemande with an existing ID
        insertedHistoriqueStatutDemande = historiqueStatutDemandeRepository.saveAndFlush(historiqueStatutDemande);
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueStatutDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueStatutDemandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateChangementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueStatutDemande.setDateChangement(null);

        // Create the HistoriqueStatutDemande, which fails.
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        restHistoriqueStatutDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueStatutDemandeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriqueStatutDemandes() throws Exception {
        // Initialize the database
        insertedHistoriqueStatutDemande = historiqueStatutDemandeRepository.saveAndFlush(historiqueStatutDemande);

        // Get all the historiqueStatutDemandeList
        restHistoriqueStatutDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueStatutDemande.getId().toString())))
            .andExpect(jsonPath("$.[*].dateChangement").value(hasItem(DEFAULT_DATE_CHANGEMENT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoriqueStatutDemandesWithEagerRelationshipsIsEnabled() throws Exception {
        when(historiqueStatutDemandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoriqueStatutDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(historiqueStatutDemandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoriqueStatutDemandesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(historiqueStatutDemandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoriqueStatutDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(historiqueStatutDemandeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHistoriqueStatutDemande() throws Exception {
        // Initialize the database
        insertedHistoriqueStatutDemande = historiqueStatutDemandeRepository.saveAndFlush(historiqueStatutDemande);

        // Get the historiqueStatutDemande
        restHistoriqueStatutDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueStatutDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueStatutDemande.getId().toString()))
            .andExpect(jsonPath("$.dateChangement").value(DEFAULT_DATE_CHANGEMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueStatutDemande() throws Exception {
        // Get the historiqueStatutDemande
        restHistoriqueStatutDemandeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriqueStatutDemande() throws Exception {
        // Initialize the database
        insertedHistoriqueStatutDemande = historiqueStatutDemandeRepository.saveAndFlush(historiqueStatutDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueStatutDemande
        HistoriqueStatutDemande updatedHistoriqueStatutDemande = historiqueStatutDemandeRepository
            .findById(historiqueStatutDemande.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedHistoriqueStatutDemande are not directly saved in db
        em.detach(updatedHistoriqueStatutDemande);
        updatedHistoriqueStatutDemande.dateChangement(UPDATED_DATE_CHANGEMENT);
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(updatedHistoriqueStatutDemande);

        restHistoriqueStatutDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueStatutDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueStatutDemandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriqueStatutDemandeToMatchAllProperties(updatedHistoriqueStatutDemande);
    }

    @Test
    @Transactional
    void putNonExistingHistoriqueStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueStatutDemande.setId(UUID.randomUUID());

        // Create the HistoriqueStatutDemande
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueStatutDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueStatutDemandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueStatutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriqueStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueStatutDemande.setId(UUID.randomUUID());

        // Create the HistoriqueStatutDemande
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueStatutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriqueStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueStatutDemande.setId(UUID.randomUUID());

        // Create the HistoriqueStatutDemande
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueStatutDemandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueStatutDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueStatutDemande = historiqueStatutDemandeRepository.saveAndFlush(historiqueStatutDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueStatutDemande using partial update
        HistoriqueStatutDemande partialUpdatedHistoriqueStatutDemande = new HistoriqueStatutDemande();
        partialUpdatedHistoriqueStatutDemande.setId(historiqueStatutDemande.getId());

        partialUpdatedHistoriqueStatutDemande.dateChangement(UPDATED_DATE_CHANGEMENT);

        restHistoriqueStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueStatutDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueStatutDemande))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueStatutDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueStatutDemandeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoriqueStatutDemande, historiqueStatutDemande),
            getPersistedHistoriqueStatutDemande(historiqueStatutDemande)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriqueStatutDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueStatutDemande = historiqueStatutDemandeRepository.saveAndFlush(historiqueStatutDemande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueStatutDemande using partial update
        HistoriqueStatutDemande partialUpdatedHistoriqueStatutDemande = new HistoriqueStatutDemande();
        partialUpdatedHistoriqueStatutDemande.setId(historiqueStatutDemande.getId());

        partialUpdatedHistoriqueStatutDemande.dateChangement(UPDATED_DATE_CHANGEMENT);

        restHistoriqueStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueStatutDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueStatutDemande))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueStatutDemande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueStatutDemandeUpdatableFieldsEquals(
            partialUpdatedHistoriqueStatutDemande,
            getPersistedHistoriqueStatutDemande(partialUpdatedHistoriqueStatutDemande)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoriqueStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueStatutDemande.setId(UUID.randomUUID());

        // Create the HistoriqueStatutDemande
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiqueStatutDemandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueStatutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriqueStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueStatutDemande.setId(UUID.randomUUID());

        // Create the HistoriqueStatutDemande
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueStatutDemandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriqueStatutDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueStatutDemande.setId(UUID.randomUUID());

        // Create the HistoriqueStatutDemande
        HistoriqueStatutDemandeDTO historiqueStatutDemandeDTO = historiqueStatutDemandeMapper.toDto(historiqueStatutDemande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueStatutDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historiqueStatutDemandeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueStatutDemande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriqueStatutDemande() throws Exception {
        // Initialize the database
        insertedHistoriqueStatutDemande = historiqueStatutDemandeRepository.saveAndFlush(historiqueStatutDemande);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historiqueStatutDemande
        restHistoriqueStatutDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiqueStatutDemande.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiqueStatutDemandeRepository.count();
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

    protected HistoriqueStatutDemande getPersistedHistoriqueStatutDemande(HistoriqueStatutDemande historiqueStatutDemande) {
        return historiqueStatutDemandeRepository.findById(historiqueStatutDemande.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriqueStatutDemandeToMatchAllProperties(HistoriqueStatutDemande expectedHistoriqueStatutDemande) {
        assertHistoriqueStatutDemandeAllPropertiesEquals(
            expectedHistoriqueStatutDemande,
            getPersistedHistoriqueStatutDemande(expectedHistoriqueStatutDemande)
        );
    }

    protected void assertPersistedHistoriqueStatutDemandeToMatchUpdatableProperties(
        HistoriqueStatutDemande expectedHistoriqueStatutDemande
    ) {
        assertHistoriqueStatutDemandeAllUpdatablePropertiesEquals(
            expectedHistoriqueStatutDemande,
            getPersistedHistoriqueStatutDemande(expectedHistoriqueStatutDemande)
        );
    }
}
