package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.MouvementStockAsserts.*;
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
import sn.orange.conciergerie.domain.CompteStock;
import sn.orange.conciergerie.domain.MouvementStock;
import sn.orange.conciergerie.domain.enumeration.TypeMouvementStock;
import sn.orange.conciergerie.repository.MouvementStockRepository;
import sn.orange.conciergerie.service.dto.MouvementStockDTO;
import sn.orange.conciergerie.service.mapper.MouvementStockMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link MouvementStockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MouvementStockResourceIT {

    private static final TypeMouvementStock DEFAULT_TYPE = TypeMouvementStock.CREDIT;
    private static final TypeMouvementStock UPDATED_TYPE = TypeMouvementStock.DEBIT;

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(0);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(1);

    private static final Instant DEFAULT_DATE_TRANSACTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TRANSACTION = Instant.ofEpochMilli(1701862474321L);

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mouvement-stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MouvementStockRepository mouvementStockRepository;

    @Autowired
    private MouvementStockMapper mouvementStockMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMouvementStockMockMvc;

    private MouvementStock mouvementStock;

    private MouvementStock insertedMouvementStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MouvementStock createEntity(EntityManager em) {
        MouvementStock mouvementStock = new MouvementStock()
            .type(DEFAULT_TYPE)
            .quantite(DEFAULT_QUANTITE)
            .dateTransaction(DEFAULT_DATE_TRANSACTION)
            .motif(DEFAULT_MOTIF);
        // Add required entity
        CompteStock compteStock;
        if (TestUtil.findAll(em, CompteStock.class).isEmpty()) {
            compteStock = CompteStockResourceIT.createEntity(em);
            em.persist(compteStock);
            em.flush();
        } else {
            compteStock = TestUtil.findAll(em, CompteStock.class).getFirst();
        }
        mouvementStock.setCompteStock(compteStock);
        return mouvementStock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MouvementStock createUpdatedEntity(EntityManager em) {
        MouvementStock updatedMouvementStock = new MouvementStock()
            .type(UPDATED_TYPE)
            .quantite(UPDATED_QUANTITE)
            .dateTransaction(UPDATED_DATE_TRANSACTION)
            .motif(UPDATED_MOTIF);
        // Add required entity
        CompteStock compteStock;
        if (TestUtil.findAll(em, CompteStock.class).isEmpty()) {
            compteStock = CompteStockResourceIT.createUpdatedEntity(em);
            em.persist(compteStock);
            em.flush();
        } else {
            compteStock = TestUtil.findAll(em, CompteStock.class).getFirst();
        }
        updatedMouvementStock.setCompteStock(compteStock);
        return updatedMouvementStock;
    }

    @BeforeEach
    void initTest() {
        mouvementStock = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMouvementStock != null) {
            mouvementStockRepository.delete(insertedMouvementStock);
            insertedMouvementStock = null;
        }
    }

    @Test
    @Transactional
    void createMouvementStock() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MouvementStock
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);
        var returnedMouvementStockDTO = om.readValue(
            restMouvementStockMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementStockDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MouvementStockDTO.class
        );

        // Validate the MouvementStock in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMouvementStock = mouvementStockMapper.toEntity(returnedMouvementStockDTO);
        assertMouvementStockUpdatableFieldsEquals(returnedMouvementStock, getPersistedMouvementStock(returnedMouvementStock));

        insertedMouvementStock = returnedMouvementStock;
    }

    @Test
    @Transactional
    void createMouvementStockWithExistingId() throws Exception {
        // Create the MouvementStock with an existing ID
        insertedMouvementStock = mouvementStockRepository.saveAndFlush(mouvementStock);
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMouvementStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mouvementStock.setType(null);

        // Create the MouvementStock, which fails.
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        restMouvementStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementStockDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mouvementStock.setQuantite(null);

        // Create the MouvementStock, which fails.
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        restMouvementStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementStockDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTransactionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mouvementStock.setDateTransaction(null);

        // Create the MouvementStock, which fails.
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        restMouvementStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementStockDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMouvementStocks() throws Exception {
        // Initialize the database
        insertedMouvementStock = mouvementStockRepository.saveAndFlush(mouvementStock);

        // Get all the mouvementStockList
        restMouvementStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mouvementStock.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(sameNumber(DEFAULT_QUANTITE))))
            .andExpect(jsonPath("$.[*].dateTransaction").value(hasItem(DEFAULT_DATE_TRANSACTION.toString())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)));
    }

    @Test
    @Transactional
    void getMouvementStock() throws Exception {
        // Initialize the database
        insertedMouvementStock = mouvementStockRepository.saveAndFlush(mouvementStock);

        // Get the mouvementStock
        restMouvementStockMockMvc
            .perform(get(ENTITY_API_URL_ID, mouvementStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mouvementStock.getId().toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.quantite").value(sameNumber(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.dateTransaction").value(DEFAULT_DATE_TRANSACTION.toString()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF));
    }

    @Test
    @Transactional
    void getNonExistingMouvementStock() throws Exception {
        // Get the mouvementStock
        restMouvementStockMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMouvementStock() throws Exception {
        // Initialize the database
        insertedMouvementStock = mouvementStockRepository.saveAndFlush(mouvementStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mouvementStock
        MouvementStock updatedMouvementStock = mouvementStockRepository.findById(mouvementStock.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMouvementStock are not directly saved in db
        em.detach(updatedMouvementStock);
        updatedMouvementStock.type(UPDATED_TYPE).quantite(UPDATED_QUANTITE).dateTransaction(UPDATED_DATE_TRANSACTION).motif(UPDATED_MOTIF);
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(updatedMouvementStock);

        restMouvementStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mouvementStockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mouvementStockDTO))
            )
            .andExpect(status().isOk());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMouvementStockToMatchAllProperties(updatedMouvementStock);
    }

    @Test
    @Transactional
    void putNonExistingMouvementStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvementStock.setId(UUID.randomUUID());

        // Create the MouvementStock
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMouvementStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mouvementStockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mouvementStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMouvementStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvementStock.setId(UUID.randomUUID());

        // Create the MouvementStock
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mouvementStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMouvementStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvementStock.setId(UUID.randomUUID());

        // Create the MouvementStock
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementStockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mouvementStockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMouvementStockWithPatch() throws Exception {
        // Initialize the database
        insertedMouvementStock = mouvementStockRepository.saveAndFlush(mouvementStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mouvementStock using partial update
        MouvementStock partialUpdatedMouvementStock = new MouvementStock();
        partialUpdatedMouvementStock.setId(mouvementStock.getId());

        partialUpdatedMouvementStock.motif(UPDATED_MOTIF);

        restMouvementStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMouvementStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMouvementStock))
            )
            .andExpect(status().isOk());

        // Validate the MouvementStock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMouvementStockUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMouvementStock, mouvementStock),
            getPersistedMouvementStock(mouvementStock)
        );
    }

    @Test
    @Transactional
    void fullUpdateMouvementStockWithPatch() throws Exception {
        // Initialize the database
        insertedMouvementStock = mouvementStockRepository.saveAndFlush(mouvementStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mouvementStock using partial update
        MouvementStock partialUpdatedMouvementStock = new MouvementStock();
        partialUpdatedMouvementStock.setId(mouvementStock.getId());

        partialUpdatedMouvementStock
            .type(UPDATED_TYPE)
            .quantite(UPDATED_QUANTITE)
            .dateTransaction(UPDATED_DATE_TRANSACTION)
            .motif(UPDATED_MOTIF);

        restMouvementStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMouvementStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMouvementStock))
            )
            .andExpect(status().isOk());

        // Validate the MouvementStock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMouvementStockUpdatableFieldsEquals(partialUpdatedMouvementStock, getPersistedMouvementStock(partialUpdatedMouvementStock));
    }

    @Test
    @Transactional
    void patchNonExistingMouvementStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvementStock.setId(UUID.randomUUID());

        // Create the MouvementStock
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMouvementStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mouvementStockDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mouvementStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMouvementStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvementStock.setId(UUID.randomUUID());

        // Create the MouvementStock
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mouvementStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMouvementStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mouvementStock.setId(UUID.randomUUID());

        // Create the MouvementStock
        MouvementStockDTO mouvementStockDTO = mouvementStockMapper.toDto(mouvementStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMouvementStockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mouvementStockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MouvementStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMouvementStock() throws Exception {
        // Initialize the database
        insertedMouvementStock = mouvementStockRepository.saveAndFlush(mouvementStock);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mouvementStock
        restMouvementStockMockMvc
            .perform(delete(ENTITY_API_URL_ID, mouvementStock.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mouvementStockRepository.count();
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

    protected MouvementStock getPersistedMouvementStock(MouvementStock mouvementStock) {
        return mouvementStockRepository.findById(mouvementStock.getId()).orElseThrow();
    }

    protected void assertPersistedMouvementStockToMatchAllProperties(MouvementStock expectedMouvementStock) {
        assertMouvementStockAllPropertiesEquals(expectedMouvementStock, getPersistedMouvementStock(expectedMouvementStock));
    }

    protected void assertPersistedMouvementStockToMatchUpdatableProperties(MouvementStock expectedMouvementStock) {
        assertMouvementStockAllUpdatablePropertiesEquals(expectedMouvementStock, getPersistedMouvementStock(expectedMouvementStock));
    }
}
