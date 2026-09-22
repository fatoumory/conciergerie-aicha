package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.FactureAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.orange.conciergerie.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.Facture;
import sn.orange.conciergerie.repository.FactureRepository;
import sn.orange.conciergerie.service.dto.FactureDTO;
import sn.orange.conciergerie.service.mapper.FactureMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link FactureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactureResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(1);

    private static final Instant DEFAULT_DATE_GENERATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_GENERATION = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureMapper factureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureMockMvc;

    private Facture facture;

    private Facture insertedFacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture().numero(DEFAULT_NUMERO).montant(DEFAULT_MONTANT).dateGeneration(DEFAULT_DATE_GENERATION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        facture.setDemande(demande);
        return facture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity(EntityManager em) {
        Facture updatedFacture = new Facture().numero(UPDATED_NUMERO).montant(UPDATED_MONTANT).dateGeneration(UPDATED_DATE_GENERATION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createUpdatedEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        updatedFacture.setDemande(demande);
        return updatedFacture;
    }

    @BeforeEach
    void initTest() {
        facture = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFacture != null) {
            factureRepository.delete(insertedFacture);
            insertedFacture = null;
        }
    }

    @Test
    @Transactional
    void createFacture() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);
        var returnedFactureDTO = om.readValue(
            restFactureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactureDTO.class
        );

        // Validate the Facture in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFacture = factureMapper.toEntity(returnedFactureDTO);
        assertFactureUpdatableFieldsEquals(returnedFacture, getPersistedFacture(returnedFacture));

        insertedFacture = returnedFacture;
    }

    @Test
    @Transactional
    void createFactureWithExistingId() throws Exception {
        // Create the Facture with an existing ID
        insertedFacture = factureRepository.saveAndFlush(facture);
        FactureDTO factureDTO = factureMapper.toDto(facture);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setNumero(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setMontant(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateGenerationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        facture.setDateGeneration(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(facture);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactures() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(sameNumber(DEFAULT_MONTANT))))
            .andExpect(jsonPath("$.[*].dateGeneration").value(hasItem(DEFAULT_DATE_GENERATION.toString())));
    }

    @Test
    @Transactional
    void getFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.montant").value(sameNumber(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.dateGeneration").value(DEFAULT_DATE_GENERATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture.numero(UPDATED_NUMERO).montant(UPDATED_MONTANT).dateGeneration(UPDATED_DATE_GENERATION);
        FactureDTO factureDTO = factureMapper.toDto(updatedFacture);

        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactureToMatchAllProperties(updatedFacture);
    }

    @Test
    @Transactional
    void putNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(UUID.randomUUID());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(UUID.randomUUID());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(UUID.randomUUID());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture.numero(UPDATED_NUMERO).montant(UPDATED_MONTANT);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFacture, facture), getPersistedFacture(facture));
    }

    @Test
    @Transactional
    void fullUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        Facture partialUpdatedFacture = new Facture();
        partialUpdatedFacture.setId(facture.getId());

        partialUpdatedFacture.numero(UPDATED_NUMERO).montant(UPDATED_MONTANT).dateGeneration(UPDATED_DATE_GENERATION);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacture.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFacture))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureUpdatableFieldsEquals(partialUpdatedFacture, getPersistedFacture(partialUpdatedFacture));
    }

    @Test
    @Transactional
    void patchNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(UUID.randomUUID());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(UUID.randomUUID());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        facture.setId(UUID.randomUUID());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(facture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacture() throws Exception {
        // Initialize the database
        insertedFacture = factureRepository.saveAndFlush(facture);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facture
        restFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, facture.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return factureRepository.count();
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

    protected Facture getPersistedFacture(Facture facture) {
        return factureRepository.findById(facture.getId()).orElseThrow();
    }

    protected void assertPersistedFactureToMatchAllProperties(Facture expectedFacture) {
        assertFactureAllPropertiesEquals(expectedFacture, getPersistedFacture(expectedFacture));
    }

    protected void assertPersistedFactureToMatchUpdatableProperties(Facture expectedFacture) {
        assertFactureAllUpdatablePropertiesEquals(expectedFacture, getPersistedFacture(expectedFacture));
    }
}
