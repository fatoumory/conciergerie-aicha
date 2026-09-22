package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.CodePromoAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.orange.conciergerie.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import sn.orange.conciergerie.domain.CodePromo;
import sn.orange.conciergerie.repository.CodePromoRepository;
import sn.orange.conciergerie.service.dto.CodePromoDTO;
import sn.orange.conciergerie.service.mapper.CodePromoMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CodePromoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodePromoResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALEUR = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALEUR = new BigDecimal(1);

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.parse("2023-12-06");

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.parse("2023-12-06");

    private static final String ENTITY_API_URL = "/api/code-promos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CodePromoRepository codePromoRepository;

    @Autowired
    private CodePromoMapper codePromoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodePromoMockMvc;

    private CodePromo codePromo;

    private CodePromo insertedCodePromo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodePromo createEntity() {
        return new CodePromo().code(DEFAULT_CODE).valeur(DEFAULT_VALEUR).dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodePromo createUpdatedEntity() {
        return new CodePromo().code(UPDATED_CODE).valeur(UPDATED_VALEUR).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
    }

    @BeforeEach
    void initTest() {
        codePromo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCodePromo != null) {
            codePromoRepository.delete(insertedCodePromo);
            insertedCodePromo = null;
        }
    }

    @Test
    @Transactional
    void createCodePromo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CodePromo
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);
        var returnedCodePromoDTO = om.readValue(
            restCodePromoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codePromoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CodePromoDTO.class
        );

        // Validate the CodePromo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCodePromo = codePromoMapper.toEntity(returnedCodePromoDTO);
        assertCodePromoUpdatableFieldsEquals(returnedCodePromo, getPersistedCodePromo(returnedCodePromo));

        insertedCodePromo = returnedCodePromo;
    }

    @Test
    @Transactional
    void createCodePromoWithExistingId() throws Exception {
        // Create the CodePromo with an existing ID
        insertedCodePromo = codePromoRepository.saveAndFlush(codePromo);
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodePromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codePromoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codePromo.setCode(null);

        // Create the CodePromo, which fails.
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        restCodePromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codePromoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValeurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codePromo.setValeur(null);

        // Create the CodePromo, which fails.
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        restCodePromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codePromoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codePromo.setDateDebut(null);

        // Create the CodePromo, which fails.
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        restCodePromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codePromoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codePromo.setDateFin(null);

        // Create the CodePromo, which fails.
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        restCodePromoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codePromoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCodePromos() throws Exception {
        // Initialize the database
        insertedCodePromo = codePromoRepository.saveAndFlush(codePromo);

        // Get all the codePromoList
        restCodePromoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codePromo.getId().toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(sameNumber(DEFAULT_VALEUR))))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getCodePromo() throws Exception {
        // Initialize the database
        insertedCodePromo = codePromoRepository.saveAndFlush(codePromo);

        // Get the codePromo
        restCodePromoMockMvc
            .perform(get(ENTITY_API_URL_ID, codePromo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codePromo.getId().toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.valeur").value(sameNumber(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCodePromo() throws Exception {
        // Get the codePromo
        restCodePromoMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCodePromo() throws Exception {
        // Initialize the database
        insertedCodePromo = codePromoRepository.saveAndFlush(codePromo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codePromo
        CodePromo updatedCodePromo = codePromoRepository.findById(codePromo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCodePromo are not directly saved in db
        em.detach(updatedCodePromo);
        updatedCodePromo.code(UPDATED_CODE).valeur(UPDATED_VALEUR).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(updatedCodePromo);

        restCodePromoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codePromoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codePromoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCodePromoToMatchAllProperties(updatedCodePromo);
    }

    @Test
    @Transactional
    void putNonExistingCodePromo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codePromo.setId(UUID.randomUUID());

        // Create the CodePromo
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodePromoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codePromoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codePromoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodePromo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codePromo.setId(UUID.randomUUID());

        // Create the CodePromo
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodePromoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codePromoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodePromo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codePromo.setId(UUID.randomUUID());

        // Create the CodePromo
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodePromoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codePromoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodePromoWithPatch() throws Exception {
        // Initialize the database
        insertedCodePromo = codePromoRepository.saveAndFlush(codePromo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codePromo using partial update
        CodePromo partialUpdatedCodePromo = new CodePromo();
        partialUpdatedCodePromo.setId(codePromo.getId());

        partialUpdatedCodePromo.code(UPDATED_CODE).valeur(UPDATED_VALEUR).dateDebut(UPDATED_DATE_DEBUT);

        restCodePromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodePromo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodePromo))
            )
            .andExpect(status().isOk());

        // Validate the CodePromo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodePromoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCodePromo, codePromo),
            getPersistedCodePromo(codePromo)
        );
    }

    @Test
    @Transactional
    void fullUpdateCodePromoWithPatch() throws Exception {
        // Initialize the database
        insertedCodePromo = codePromoRepository.saveAndFlush(codePromo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codePromo using partial update
        CodePromo partialUpdatedCodePromo = new CodePromo();
        partialUpdatedCodePromo.setId(codePromo.getId());

        partialUpdatedCodePromo.code(UPDATED_CODE).valeur(UPDATED_VALEUR).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restCodePromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodePromo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodePromo))
            )
            .andExpect(status().isOk());

        // Validate the CodePromo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodePromoUpdatableFieldsEquals(partialUpdatedCodePromo, getPersistedCodePromo(partialUpdatedCodePromo));
    }

    @Test
    @Transactional
    void patchNonExistingCodePromo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codePromo.setId(UUID.randomUUID());

        // Create the CodePromo
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodePromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codePromoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codePromoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodePromo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codePromo.setId(UUID.randomUUID());

        // Create the CodePromo
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodePromoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codePromoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodePromo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codePromo.setId(UUID.randomUUID());

        // Create the CodePromo
        CodePromoDTO codePromoDTO = codePromoMapper.toDto(codePromo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodePromoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(codePromoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodePromo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodePromo() throws Exception {
        // Initialize the database
        insertedCodePromo = codePromoRepository.saveAndFlush(codePromo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the codePromo
        restCodePromoMockMvc
            .perform(delete(ENTITY_API_URL_ID, codePromo.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return codePromoRepository.count();
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

    protected CodePromo getPersistedCodePromo(CodePromo codePromo) {
        return codePromoRepository.findById(codePromo.getId()).orElseThrow();
    }

    protected void assertPersistedCodePromoToMatchAllProperties(CodePromo expectedCodePromo) {
        assertCodePromoAllPropertiesEquals(expectedCodePromo, getPersistedCodePromo(expectedCodePromo));
    }

    protected void assertPersistedCodePromoToMatchUpdatableProperties(CodePromo expectedCodePromo) {
        assertCodePromoAllUpdatablePropertiesEquals(expectedCodePromo, getPersistedCodePromo(expectedCodePromo));
    }
}
