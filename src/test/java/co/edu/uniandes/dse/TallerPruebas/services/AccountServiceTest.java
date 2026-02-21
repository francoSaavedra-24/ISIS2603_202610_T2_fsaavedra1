package co.edu.uniandes.dse.TallerPruebas.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.TallerPruebas.entities.AccountEntity;
import co.edu.uniandes.dse.TallerPruebas.entities.PocketEntity;
import co.edu.uniandes.dse.TallerPruebas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.TallerPruebas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.TallerPruebas.services.AccountService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AccountService.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AccountEntity> accountList = new ArrayList<>();
    private List<PocketEntity> pocketList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PocketEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AccountEntity").executeUpdate();
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {
            AccountEntity account = factory.manufacturePojo(AccountEntity.class);
            account.setSaldo(1000.0);
            account.setEstado("ACTIVA");
            entityManager.persist(account);
            accountList.add(account);
        }

        for (int i = 0; i < 2; i++) {
            PocketEntity pocket = factory.manufacturePojo(PocketEntity.class);
            pocket.setSaldo(200.0);
            pocket.setAccount(accountList.get(0));
            entityManager.persist(pocket);
            pocketList.add(pocket);
        }

        accountList.get(0).setPockets(pocketList);
    }

    /**
     * Regla 1 - Éxito mover dinero a bolsillo
     */
    @Test
    void testMoveMoneyToPocket() throws EntityNotFoundException, BusinessLogicException {

        AccountEntity account = accountList.get(0);
        PocketEntity pocket = pocketList.get(0);

        accountService.moveMoneyToPocket(account.getId(), pocket.getId(), 300.0);

        AccountEntity updatedAccount = entityManager.find(AccountEntity.class, account.getId());
        PocketEntity updatedPocket = entityManager.find(PocketEntity.class, pocket.getId());

        assertEquals(700.0, updatedAccount.getSaldo());
        assertEquals(500.0, updatedPocket.getSaldo());
    }

    /**
     * Regla 1 - Fallo saldo insuficiente
     */
    @Test
    void testMoveMoneyInsufficientFunds() {

        assertThrows(BusinessLogicException.class, () -> {
            AccountEntity account = accountList.get(0);
            PocketEntity pocket = pocketList.get(0);
            accountService.moveMoneyToPocket(account.getId(), pocket.getId(), 5000.0);
        });
    }

    /**
     * Regla 2 - Éxito transferencia entre cuentas
     */
    @Test
    void testTransfer() throws EntityNotFoundException, BusinessLogicException {

        AccountEntity origin = accountList.get(0);
        AccountEntity destination = accountList.get(1);

        accountService.transfer(origin.getId(), destination.getId(), 400.0);

        AccountEntity updatedOrigin = entityManager.find(AccountEntity.class, origin.getId());
        AccountEntity updatedDestination = entityManager.find(AccountEntity.class, destination.getId());

        assertEquals(600.0, updatedOrigin.getSaldo());
        assertEquals(1400.0, updatedDestination.getSaldo());
    }

    /**
     * Regla 2 - Fallo misma cuenta
     */
    @Test
    void testTransferSameAccount() {

        assertThrows(BusinessLogicException.class, () -> {
            AccountEntity account = accountList.get(0);
            accountService.transfer(account.getId(), account.getId(), 100.0);
        });
    }

    /**
     * Regla 2 - Fallo cuenta inexistente
     */
    @Test
    void testTransferInvalidAccount() {

        assertThrows(EntityNotFoundException.class, () -> {
            AccountEntity account = accountList.get(0);
            accountService.transfer(account.getId(), 0L, 100.0);
        });
    }
}