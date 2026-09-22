package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.TransactionPaiementAsserts.*;
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
import sn.orange.conciergerie.domain.TransactionPaiement;
import sn.orange.conciergerie.domain.enumeration.ModePaiement;
import sn.orange.conciergerie.domain.enumeration.StatutTransaction;
import sn.orange.conciergerie.repository.TransactionPaiementRepository;
import sn.orange.conciergerie.service.dto.TransactionPaiementDTO;
import sn.orange.conciergerie.service.mapper.TransactionPaiementMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TransactionPaiementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionPaiementResourceIT {

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(1);
    private static final BigDecimal SMALLER_MONTANT = new BigDecimal(0 - 1);

    private static final ModePaiement DEFAULT_MODE_PAIEMENT = ModePaiement.ORANGE_MONEY;
    private static final ModePaiement UPDATED_MODE_PAIEMENT = ModePaiement.ORANGE_MONEY;

    private static final StatutTransaction DEFAULT_STATUT = StatutTransaction.EN_ATTENTE;
    private static final StatutTransaction UPDATED_STATUT = StatutTransaction.SUCCES;

    private static final String DEFAULT_REFERENCE_EXTERNE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_EXTERNE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_TRANSACTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TRANSACTION = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/transaction-paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionPaiementRepository transactionPaiementRepository;

    @Autowired
    private TransactionPaiementMapper transactionPaiementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionPaiementMockMvc;

    private TransactionPaiement transactionPaiement;

    private TransactionPaiement insertedTransactionPaiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionPaiement createEntity(EntityManager em) {
        TransactionPaiement transactionPaiement = new TransactionPaiement()
            .montant(DEFAULT_MONTANT)
            .modePaiement(DEFAULT_MODE_PAIEMENT)
            .statut(DEFAULT_STATUT)
            .referenceExterne(DEFAULT_REFERENCE_EXTERNE)
            .dateTransaction(DEFAULT_DATE_TRANSACTION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        transactionPaiement.setDemande(demande);
        return transactionPaiement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionPaiement createUpdatedEntity(EntityManager em) {
        TransactionPaiement updatedTransactionPaiement = new TransactionPaiement()
            .montant(UPDATED_MONTANT)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .statut(UPDATED_STATUT)
            .referenceExterne(UPDATED_REFERENCE_EXTERNE)
            .dateTransaction(UPDATED_DATE_TRANSACTION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createUpdatedEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        updatedTransactionPaiement.setDemande(demande);
        return updatedTransactionPaiement;
    }

    @BeforeEach
    void initTest() {
        transactionPaiement = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTransactionPaiement != null) {
            transactionPaiementRepository.delete(insertedTransactionPaiement);
            insertedTransactionPaiement = null;
        }
    }

    @Test
    @Transactional
    void createTransactionPaiement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransactionPaiement
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);
        var returnedTransactionPaiementDTO = om.readValue(
            restTransactionPaiementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionPaiementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionPaiementDTO.class
        );

        // Validate the TransactionPaiement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransactionPaiement = transactionPaiementMapper.toEntity(returnedTransactionPaiementDTO);
        assertTransactionPaiementUpdatableFieldsEquals(
            returnedTransactionPaiement,
            getPersistedTransactionPaiement(returnedTransactionPaiement)
        );

        insertedTransactionPaiement = returnedTransactionPaiement;
    }

    @Test
    @Transactional
    void createTransactionPaiementWithExistingId() throws Exception {
        // Create the TransactionPaiement with an existing ID
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionPaiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionPaiement.setMontant(null);

        // Create the TransactionPaiement, which fails.
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        restTransactionPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionPaiementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModePaiementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionPaiement.setModePaiement(null);

        // Create the TransactionPaiement, which fails.
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        restTransactionPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionPaiementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionPaiement.setStatut(null);

        // Create the TransactionPaiement, which fails.
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        restTransactionPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionPaiementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateTransactionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionPaiement.setDateTransaction(null);

        // Create the TransactionPaiement, which fails.
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        restTransactionPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionPaiementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionPaiements() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList
        restTransactionPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionPaiement.getId().toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(sameNumber(DEFAULT_MONTANT))))
            .andExpect(jsonPath("$.[*].modePaiement").value(hasItem(DEFAULT_MODE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].referenceExterne").value(hasItem(DEFAULT_REFERENCE_EXTERNE)))
            .andExpect(jsonPath("$.[*].dateTransaction").value(hasItem(DEFAULT_DATE_TRANSACTION.toString())));
    }

    @Test
    @Transactional
    void getTransactionPaiement() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get the transactionPaiement
        restTransactionPaiementMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionPaiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionPaiement.getId().toString()))
            .andExpect(jsonPath("$.montant").value(sameNumber(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.modePaiement").value(DEFAULT_MODE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.referenceExterne").value(DEFAULT_REFERENCE_EXTERNE))
            .andExpect(jsonPath("$.dateTransaction").value(DEFAULT_DATE_TRANSACTION.toString()));
    }

    @Test
    @Transactional
    void getTransactionPaiementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        UUID id = transactionPaiement.getId();

        defaultTransactionPaiementFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where montant equals to
        defaultTransactionPaiementFiltering("montant.equals=" + DEFAULT_MONTANT, "montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where montant in
        defaultTransactionPaiementFiltering("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT, "montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where montant is not null
        defaultTransactionPaiementFiltering("montant.specified=true", "montant.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where montant is greater than or equal to
        defaultTransactionPaiementFiltering(
            "montant.greaterThanOrEqual=" + DEFAULT_MONTANT,
            "montant.greaterThanOrEqual=" + UPDATED_MONTANT
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where montant is less than or equal to
        defaultTransactionPaiementFiltering("montant.lessThanOrEqual=" + DEFAULT_MONTANT, "montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where montant is less than
        defaultTransactionPaiementFiltering("montant.lessThan=" + UPDATED_MONTANT, "montant.lessThan=" + DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where montant is greater than
        defaultTransactionPaiementFiltering("montant.greaterThan=" + SMALLER_MONTANT, "montant.greaterThan=" + DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByModePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where modePaiement equals to
        defaultTransactionPaiementFiltering("modePaiement.equals=" + DEFAULT_MODE_PAIEMENT, "modePaiement.equals=" + UPDATED_MODE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByModePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where modePaiement in
        defaultTransactionPaiementFiltering(
            "modePaiement.in=" + DEFAULT_MODE_PAIEMENT + "," + UPDATED_MODE_PAIEMENT,
            "modePaiement.in=" + UPDATED_MODE_PAIEMENT
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByModePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where modePaiement is not null
        defaultTransactionPaiementFiltering("modePaiement.specified=true", "modePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where statut equals to
        defaultTransactionPaiementFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where statut in
        defaultTransactionPaiementFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where statut is not null
        defaultTransactionPaiementFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByReferenceExterneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where referenceExterne equals to
        defaultTransactionPaiementFiltering(
            "referenceExterne.equals=" + DEFAULT_REFERENCE_EXTERNE,
            "referenceExterne.equals=" + UPDATED_REFERENCE_EXTERNE
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByReferenceExterneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where referenceExterne in
        defaultTransactionPaiementFiltering(
            "referenceExterne.in=" + DEFAULT_REFERENCE_EXTERNE + "," + UPDATED_REFERENCE_EXTERNE,
            "referenceExterne.in=" + UPDATED_REFERENCE_EXTERNE
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByReferenceExterneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where referenceExterne is not null
        defaultTransactionPaiementFiltering("referenceExterne.specified=true", "referenceExterne.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByReferenceExterneContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where referenceExterne contains
        defaultTransactionPaiementFiltering(
            "referenceExterne.contains=" + DEFAULT_REFERENCE_EXTERNE,
            "referenceExterne.contains=" + UPDATED_REFERENCE_EXTERNE
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByReferenceExterneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where referenceExterne does not contain
        defaultTransactionPaiementFiltering(
            "referenceExterne.doesNotContain=" + UPDATED_REFERENCE_EXTERNE,
            "referenceExterne.doesNotContain=" + DEFAULT_REFERENCE_EXTERNE
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByDateTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where dateTransaction equals to
        defaultTransactionPaiementFiltering(
            "dateTransaction.equals=" + DEFAULT_DATE_TRANSACTION,
            "dateTransaction.equals=" + UPDATED_DATE_TRANSACTION
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByDateTransactionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where dateTransaction in
        defaultTransactionPaiementFiltering(
            "dateTransaction.in=" + DEFAULT_DATE_TRANSACTION + "," + UPDATED_DATE_TRANSACTION,
            "dateTransaction.in=" + UPDATED_DATE_TRANSACTION
        );
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByDateTransactionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        // Get all the transactionPaiementList where dateTransaction is not null
        defaultTransactionPaiementFiltering("dateTransaction.specified=true", "dateTransaction.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionPaiementsByDemandeIsEqualToSomething() throws Exception {
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            transactionPaiementRepository.saveAndFlush(transactionPaiement);
            demande = DemandeResourceIT.createEntity(em);
        } else {
            demande = TestUtil.findAll(em, Demande.class).getFirst();
        }
        em.persist(demande);
        em.flush();
        transactionPaiement.setDemande(demande);
        transactionPaiementRepository.saveAndFlush(transactionPaiement);
        UUID demandeId = demande.getId();
        // Get all the transactionPaiementList where demande equals to demandeId
        defaultTransactionPaiementShouldBeFound("demandeId.equals=" + demandeId);

        // Get all the transactionPaiementList where demande equals to UUID.randomUUID()
        defaultTransactionPaiementShouldNotBeFound("demandeId.equals=" + UUID.randomUUID());
    }

    private void defaultTransactionPaiementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransactionPaiementShouldBeFound(shouldBeFound);
        defaultTransactionPaiementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionPaiementShouldBeFound(String filter) throws Exception {
        restTransactionPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionPaiement.getId().toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(sameNumber(DEFAULT_MONTANT))))
            .andExpect(jsonPath("$.[*].modePaiement").value(hasItem(DEFAULT_MODE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].referenceExterne").value(hasItem(DEFAULT_REFERENCE_EXTERNE)))
            .andExpect(jsonPath("$.[*].dateTransaction").value(hasItem(DEFAULT_DATE_TRANSACTION.toString())));

        // Check, that the count call also returns 1
        restTransactionPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionPaiementShouldNotBeFound(String filter) throws Exception {
        restTransactionPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionPaiement() throws Exception {
        // Get the transactionPaiement
        restTransactionPaiementMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionPaiement() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionPaiement
        TransactionPaiement updatedTransactionPaiement = transactionPaiementRepository.findById(transactionPaiement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionPaiement are not directly saved in db
        em.detach(updatedTransactionPaiement);
        updatedTransactionPaiement
            .montant(UPDATED_MONTANT)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .statut(UPDATED_STATUT)
            .referenceExterne(UPDATED_REFERENCE_EXTERNE)
            .dateTransaction(UPDATED_DATE_TRANSACTION);
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(updatedTransactionPaiement);

        restTransactionPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionPaiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionPaiementDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionPaiementToMatchAllProperties(updatedTransactionPaiement);
    }

    @Test
    @Transactional
    void putNonExistingTransactionPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionPaiement.setId(UUID.randomUUID());

        // Create the TransactionPaiement
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionPaiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionPaiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionPaiement.setId(UUID.randomUUID());

        // Create the TransactionPaiement
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionPaiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionPaiement.setId(UUID.randomUUID());

        // Create the TransactionPaiement
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionPaiementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionPaiementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionPaiementWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionPaiement using partial update
        TransactionPaiement partialUpdatedTransactionPaiement = new TransactionPaiement();
        partialUpdatedTransactionPaiement.setId(transactionPaiement.getId());

        partialUpdatedTransactionPaiement
            .montant(UPDATED_MONTANT)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .referenceExterne(UPDATED_REFERENCE_EXTERNE);

        restTransactionPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionPaiement))
            )
            .andExpect(status().isOk());

        // Validate the TransactionPaiement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionPaiementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionPaiement, transactionPaiement),
            getPersistedTransactionPaiement(transactionPaiement)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionPaiementWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionPaiement using partial update
        TransactionPaiement partialUpdatedTransactionPaiement = new TransactionPaiement();
        partialUpdatedTransactionPaiement.setId(transactionPaiement.getId());

        partialUpdatedTransactionPaiement
            .montant(UPDATED_MONTANT)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .statut(UPDATED_STATUT)
            .referenceExterne(UPDATED_REFERENCE_EXTERNE)
            .dateTransaction(UPDATED_DATE_TRANSACTION);

        restTransactionPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionPaiement))
            )
            .andExpect(status().isOk());

        // Validate the TransactionPaiement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionPaiementUpdatableFieldsEquals(
            partialUpdatedTransactionPaiement,
            getPersistedTransactionPaiement(partialUpdatedTransactionPaiement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransactionPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionPaiement.setId(UUID.randomUUID());

        // Create the TransactionPaiement
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionPaiementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionPaiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionPaiement.setId(UUID.randomUUID());

        // Create the TransactionPaiement
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionPaiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionPaiement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionPaiement.setId(UUID.randomUUID());

        // Create the TransactionPaiement
        TransactionPaiementDTO transactionPaiementDTO = transactionPaiementMapper.toDto(transactionPaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionPaiementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionPaiement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionPaiement() throws Exception {
        // Initialize the database
        insertedTransactionPaiement = transactionPaiementRepository.saveAndFlush(transactionPaiement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transactionPaiement
        restTransactionPaiementMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionPaiement.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionPaiementRepository.count();
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

    protected TransactionPaiement getPersistedTransactionPaiement(TransactionPaiement transactionPaiement) {
        return transactionPaiementRepository.findById(transactionPaiement.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionPaiementToMatchAllProperties(TransactionPaiement expectedTransactionPaiement) {
        assertTransactionPaiementAllPropertiesEquals(
            expectedTransactionPaiement,
            getPersistedTransactionPaiement(expectedTransactionPaiement)
        );
    }

    protected void assertPersistedTransactionPaiementToMatchUpdatableProperties(TransactionPaiement expectedTransactionPaiement) {
        assertTransactionPaiementAllUpdatablePropertiesEquals(
            expectedTransactionPaiement,
            getPersistedTransactionPaiement(expectedTransactionPaiement)
        );
    }
}
