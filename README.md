# Back del proyecto TallerPruebas

## Enlaces de interés

* [BookstoreBack](https://github.com/Uniandes-isis2603/bookstore-back) -> Repositorio de referencia para el Back

* [Jenkins](http://157.253.238.75:8080/jenkins-isis2603/) -> Autentíquese con sus credencias de GitHub
* [SonarQube](http://157.253.238.75:8080/sonar-isis2603/) -> No requiere autenticació

 ESCENARIOS REGLA 1
 1 EXITO: Se mueve dinero al bolsillo cuando.el saldo de la cuenta es mayor o igual al monto
 fallo : El saldo a mover desde la cuenta es menor al monto
 fallo: Que el bolsillo no exista
 fallo que la cuenta no exista.
 Fallo  ingresar un caracter no numerico 
 

 ESCENERIO REGLA 2
 EXITO:  que la cuenta origen y destino existan
 Exito confirmar que la cuenta origen y la destino no sena iguales.
 FALLO: Que la cuenta origen o la cuenta destino no existan
 Fallo: que la cuenta origen y la destina sean iguales
 exito : que los fondos sean suficientes en el origen 

REGLA 1
|Escenario| Estado Inicial (BD) | Acción (Input) | Resultado Esperado(Output/BD) |
|---------|---------------------|----------------|-------------------------------|
|Exito:se mueve dinero el bolsillo cuando el saldo de la cuenta es mayor o igual al monto|Existe Cuenta 123 (Saldo: 1000, Activa). Existe Bolsillo "Ahorro" asociado a Cuenta 123 (Saldo: 200)|Mover 300 desde Cuenta 123 al Bolsillo "Ahorro"|1. Se descuenta 300 del saldo de la cuenta (queda 700). 2. Se suma 300 al bolsillo (queda 500). 3. Se guardan los cambios en la BD. 4. Retorna confirmación de operación exitosa.|

|fallo:El saldo a mover desde la cuenta es menor al monto|Existe Cuenta 123 (Saldo: 100). Existe Bolsillo "Ahorro"|Mover 300 desde Cuenta 123 al Bolsillo "Ahorro"|Lanza Excepción: BusinessLogicException ("Fondos insuficientes"). No se modifican saldos.|

|Fallo: Bolsillo inexistente|Existe Cuenta 123 (Saldo: 1000). No existe Bolsillo "Viaje"|Mover 200 al Bolsillo "Viaje"|Lanza Excepción: EntityNotFoundException ("El bolsillo no existe"). No se realizan cambios en BD.|

|Fallo: Cuenta inexistente|No existe Cuenta 999|Mover 200 al Bolsillo "Ahorro" en Cuenta 999|Lanza Excepción: EntityNotFoundException ("La cuenta no existe").|

|Fallo: Monto inválido|Existe Cuenta 123 (Saldo: 1000). Existe Bolsillo "Ahorro"| Mover "abc" al Bolsillo "Ahorro" | Lanza Excepción: IllegalArgumentException o ValidationException ("El monto debe ser numérico"). No se realizan cambios.|
REGLA 2
|Escenario| Estado Inicial (BD) | Acción (Input) | Resultado Esperado(Output/BD) |
|---------|---------------------|----------------|-------------------------------|
|Éxito: Transferencia válida|Existe Cuenta Origen 123 (Saldo: 1000). Existe Cuenta Destino 456 (Saldo: 500).|Transferir 300 de Cuenta 123 a Cuenta 456| 1. Se descuentan 300 de la cuenta origen (queda 700). 2. Se suman 300 a la cuenta destino (queda 800). 3. Se guardan cambios en BD. 4. Retorna confirmación exitosa.|

|Éxito: Cuentas distintas| Existe Cuenta 123 y Cuenta 456|Transferir 100 de 123 a 456|Se valida que las cuentas son diferentes. La operación continúa normalmente.|

|Fallo: Cuenta inexistente|Existe Cuenta#123. No existe Cuenta 999|Transferir 200 de 123 a 999|Lanza Excepción: EntityNotFoundException ("Cuenta origen o destino no existe"). No hay cambios en BD.|

|Fallo: Misma cuenta|Existe Cuenta 123 (Saldo: 1000)|Transferir 200 de 123 a 123|Lanza Excepción: BusinessLogicException ("La cuenta origen y destino no pueden ser la misma"). No hay cambios|

|Éxito: Fondos suficientes|Existe Cuenta Origen 123 (Saldo: 1000). Existe Cuenta Destino 456 (Saldo: 500)| Transferir 200 de 123 a 456| Validación de fondos correcta. Se realiza la transferencia y se actualizan los saldos.|