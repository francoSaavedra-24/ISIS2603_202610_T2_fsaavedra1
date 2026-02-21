package co.edu.uniandes.dse.TallerPruebas.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.TallerPruebas.entities.AccountEntity;
import co.edu.uniandes.dse.TallerPruebas.entities.PocketEntity;
import co.edu.uniandes.dse.TallerPruebas.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.TallerPruebas.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.TallerPruebas.repositories.AccountRepository;
import co.edu.uniandes.dse.TallerPruebas.repositories.PocketRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PocketRepository pocketRepository;

    /**
     * con esto movemos dinero desde una cuenta hacia un bolsillo
     */
    @Transactional
    public void moveMoneyToPocket(Long accountId, Long pocketId, Double monto)
            throws EntityNotFoundException, BusinessLogicException {

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("La cuenta no existe"));

        PocketEntity pocket = pocketRepository.findById(pocketId)
                .orElseThrow(() -> new EntityNotFoundException("El bolsillo no existe"));

        if (!account.getEstado().equals("ACTIVA")) {
            throw new BusinessLogicException("La cuenta está bloqueada");
        }

        if (!pocket.getAccount().getId().equals(account.getId())) {
            throw new BusinessLogicException("El bolsillo no pertenece a la cuenta");
        }

        if (monto == null || monto <= 0) {
            throw new BusinessLogicException("Monto inválido");
        }

        if (account.getSaldo() < monto) {
            throw new BusinessLogicException("Fondos insuficientes");
        }

        account.setSaldo(account.getSaldo() - monto);
        pocket.setSaldo(pocket.getSaldo() + monto);

        accountRepository.save(account);
        pocketRepository.save(pocket);
    }

    /**
     * hacemos Transferencias entre cuentas
     */
    @Transactional
    public void transfer(Long originId, Long destinationId, Double monto)
            throws EntityNotFoundException, BusinessLogicException {

        AccountEntity origin = accountRepository.findById(originId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta origen no existe"));

        AccountEntity destination = accountRepository.findById(destinationId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta destino no existe"));

        if (origin.getId().equals(destination.getId())) {
            throw new BusinessLogicException("No se puede transferir a la misma cuenta");
        }

        if (!origin.getEstado().equals("ACTIVA") || !destination.getEstado().equals("ACTIVA")) {
            throw new BusinessLogicException("Una de las cuentas está bloqueada");
        }

        if (monto == null || monto <= 0) {
            throw new BusinessLogicException("Monto inválido");
        }

        if (origin.getSaldo() < monto) {
            throw new BusinessLogicException("Fondos insuficientes");
        }

        origin.setSaldo(origin.getSaldo() - monto);
        destination.setSaldo(destination.getSaldo() + monto);

        accountRepository.save(origin);
        accountRepository.save(destination);
    }
}
