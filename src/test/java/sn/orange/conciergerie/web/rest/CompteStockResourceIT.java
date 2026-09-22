package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.CompteStockAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.orange.conciergerie.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import sn.orange.conciergerie.domain.CompteStock;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.repository.CompteStockRepository;
import sn.orange.conciergerie.service.CompteStockService;
import sn.orange.conciergerie.service.dto.CompteStockDTO;
import sn.orange.conciergerie.service.mapper.CompteStockMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CompteStockResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompteStockResourceIT {

    private static final BigDecimal DEFAULT_SOLDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SOLDE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/compte-stocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompteStockRepository compteStockRepository;

    @Mock
    private CompteStockRepository compteStockRepositoryMock;

    @Autowired
    private CompteStockMapper compteStockMapper;

    @Mock
    private CompteStockService compteStockServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteStockMockMvc;

    private CompteStock compteStock;

    private CompteStock insertedCompteStock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteStock createEntity(EntityManager em) {
        CompteStock compteStock = new CompteStock().solde(DEFAULT_SOLDE);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        compteStock.setService(serviceConciergerie);
        return compteStock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteStock createUpdatedEntity(EntityManager em) {
        CompteStock updatedCompteStock = new CompteStock().solde(UPDATED_SOLDE);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createUpdatedEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        updatedCompteStock.setService(serviceConciergerie);
        return updatedCompteStock;
    }

    @BeforeEach
    void initTest() {
        compteStock = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCompteStock != null) {
            compteStockRepository.delete(insertedCompteStock);
            insertedCompteStock = null;
        }
    }

    @Test
    @Transactional
    void createCompteStock() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompteStock
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);
        var returnedCompteStockDTO = om.readValue(
            restCompteStockMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteStockDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompteStockDTO.class
        );

        // Validate the CompteStock in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompteStock = compteStockMapper.toEntity(returnedCompteStockDTO);
        assertCompteStockUpdatableFieldsEquals(returnedCompteStock, getPersistedCompteStock(returnedCompteStock));

        insertedCompteStock = returnedCompteStock;
    }

    @Test
    @Transactional
    void createCompteStockWithExistingId() throws Exception {
        // Create the CompteStock with an existing ID
        insertedCompteStock = compteStockRepository.saveAndFlush(compteStock);
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteStockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSoldeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compteStock.setSolde(null);

        // Create the CompteStock, which fails.
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        restCompteStockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteStockDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompteStocks() throws Exception {
        // Initialize the database
        insertedCompteStock = compteStockRepository.saveAndFlush(compteStock);

        // Get all the compteStockList
        restCompteStockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteStock.getId().toString())))
            .andExpect(jsonPath("$.[*].solde").value(hasItem(sameNumber(DEFAULT_SOLDE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompteStocksWithEagerRelationshipsIsEnabled() throws Exception {
        when(compteStockServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCompteStockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(compteStockServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompteStocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(compteStockServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCompteStockMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(compteStockRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCompteStock() throws Exception {
        // Initialize the database
        insertedCompteStock = compteStockRepository.saveAndFlush(compteStock);

        // Get the compteStock
        restCompteStockMockMvc
            .perform(get(ENTITY_API_URL_ID, compteStock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compteStock.getId().toString()))
            .andExpect(jsonPath("$.solde").value(sameNumber(DEFAULT_SOLDE)));
    }

    @Test
    @Transactional
    void getNonExistingCompteStock() throws Exception {
        // Get the compteStock
        restCompteStockMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompteStock() throws Exception {
        // Initialize the database
        insertedCompteStock = compteStockRepository.saveAndFlush(compteStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compteStock
        CompteStock updatedCompteStock = compteStockRepository.findById(compteStock.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompteStock are not directly saved in db
        em.detach(updatedCompteStock);
        updatedCompteStock.solde(UPDATED_SOLDE);
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(updatedCompteStock);

        restCompteStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteStockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteStockDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompteStockToMatchAllProperties(updatedCompteStock);
    }

    @Test
    @Transactional
    void putNonExistingCompteStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compteStock.setId(UUID.randomUUID());

        // Create the CompteStock
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteStockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompteStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compteStock.setId(UUID.randomUUID());

        // Create the CompteStock
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteStockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compteStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompteStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compteStock.setId(UUID.randomUUID());

        // Create the CompteStock
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteStockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compteStockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteStockWithPatch() throws Exception {
        // Initialize the database
        insertedCompteStock = compteStockRepository.saveAndFlush(compteStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compteStock using partial update
        CompteStock partialUpdatedCompteStock = new CompteStock();
        partialUpdatedCompteStock.setId(compteStock.getId());

        partialUpdatedCompteStock.solde(UPDATED_SOLDE);

        restCompteStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompteStock))
            )
            .andExpect(status().isOk());

        // Validate the CompteStock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteStockUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompteStock, compteStock),
            getPersistedCompteStock(compteStock)
        );
    }

    @Test
    @Transactional
    void fullUpdateCompteStockWithPatch() throws Exception {
        // Initialize the database
        insertedCompteStock = compteStockRepository.saveAndFlush(compteStock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compteStock using partial update
        CompteStock partialUpdatedCompteStock = new CompteStock();
        partialUpdatedCompteStock.setId(compteStock.getId());

        partialUpdatedCompteStock.solde(UPDATED_SOLDE);

        restCompteStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteStock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompteStock))
            )
            .andExpect(status().isOk());

        // Validate the CompteStock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompteStockUpdatableFieldsEquals(partialUpdatedCompteStock, getPersistedCompteStock(partialUpdatedCompteStock));
    }

    @Test
    @Transactional
    void patchNonExistingCompteStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compteStock.setId(UUID.randomUUID());

        // Create the CompteStock
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteStockDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompteStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compteStock.setId(UUID.randomUUID());

        // Create the CompteStock
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteStockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compteStockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompteStock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compteStock.setId(UUID.randomUUID());

        // Create the CompteStock
        CompteStockDTO compteStockDTO = compteStockMapper.toDto(compteStock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteStockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compteStockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteStock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompteStock() throws Exception {
        // Initialize the database
        insertedCompteStock = compteStockRepository.saveAndFlush(compteStock);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compteStock
        restCompteStockMockMvc
            .perform(delete(ENTITY_API_URL_ID, compteStock.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compteStockRepository.count();
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

    protected CompteStock getPersistedCompteStock(CompteStock compteStock) {
        return compteStockRepository.findById(compteStock.getId()).orElseThrow();
    }

    protected void assertPersistedCompteStockToMatchAllProperties(CompteStock expectedCompteStock) {
        assertCompteStockAllPropertiesEquals(expectedCompteStock, getPersistedCompteStock(expectedCompteStock));
    }

    protected void assertPersistedCompteStockToMatchUpdatableProperties(CompteStock expectedCompteStock) {
        assertCompteStockAllUpdatablePropertiesEquals(expectedCompteStock, getPersistedCompteStock(expectedCompteStock));
    }
}
