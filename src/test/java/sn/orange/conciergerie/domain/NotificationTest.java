package sn.orange.conciergerie.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static sn.orange.conciergerie.domain.ClientTestSamples.*;
import static sn.orange.conciergerie.domain.DemandeTestSamples.*;
import static sn.orange.conciergerie.domain.NotificationTestSamples.*;

import org.junit.jupiter.api.Test;
import sn.orange.conciergerie.web.rest.TestUtil;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void clientTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        notification.setClient(clientBack);
        assertThat(notification.getClient()).isEqualTo(clientBack);

        notification.client(null);
        assertThat(notification.getClient()).isNull();
    }

    @Test
    void demandeTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        notification.setDemande(demandeBack);
        assertThat(notification.getDemande()).isEqualTo(demandeBack);

        notification.demande(null);
        assertThat(notification.getDemande()).isNull();
    }
}
